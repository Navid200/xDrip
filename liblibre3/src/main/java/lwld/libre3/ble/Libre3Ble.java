package lwld.libre3.ble;

import static android.bluetooth.BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import lwld.libre3.Libre3SensorState;
import lwld.libre3.crypto.AppIdentity;
import lwld.libre3.data.DataPlaneCrypto;
import lwld.libre3.data.HistoricalPage;
import lwld.libre3.data.PatchControl;
import lwld.libre3.data.PatchStatus;
import lwld.libre3.data.RealtimeReading;
import lwld.libre3.pairing.EphemeralExchange;
import lwld.libre3.pairing.Libre3Transport;
import lwld.libre3.pairing.PairingFlow;
import lwld.libre3.pairing.PairingResult;
import no.nordicsemi.android.ble.BleManager;
import no.nordicsemi.android.ble.exception.BluetoothDisabledException;
import no.nordicsemi.android.ble.exception.DeviceDisconnectedException;
import no.nordicsemi.android.ble.exception.InvalidRequestException;
import no.nordicsemi.android.ble.exception.RequestFailedException;
import no.nordicsemi.android.ble.observer.ConnectionObserver;
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

/**
 * The Bluetooth side of one Libre 3 sensor: finds it, runs the pairing handshake on a single worker thread, then
 * decrypts the data plane and hands readings, status and history to a {@link Listener}. It connects by address and
 * scans for the sensor's local name (its address in upper-case hex) on alternate attempts, and backs off up to five
 * minutes between them. Values on the security characteristics never reach the log; see {@link #redact}. Untested on
 * hardware. Shaped like libglupro {@code GluProBle}; the post-handshake notification enable follows RoundWhiteDiscKit
 * {@code SensorSession.refreshDataPlaneNotifications} (MIT).
 *
 * @author Asbjørn Aarrestad - 2026.10
 */
public class Libre3Ble extends BleManager implements Libre3Transport {

    private static final String TAG = "Libre3Ble";

    private static final long SCAN_TIMEOUT_MS = 30_000;
    private static final long POLL_SLICE_MS = 250;
    private static final long MAX_RECONNECT_DELAY_MS = 300_000;
    private static final long RECONNECT_STEP_MS = 5_000;
    /** xDrip4iOS waits this long between historic backfill requests. */
    private static final long BACKFILL_INTERVAL_MS = 22 * 60_000;

    private static final List<UUID> REQUIRED = Arrays.asList(Gatt.SEC_CERT_DATA, Gatt.SEC_CHALLENGE_DATA,
            Gatt.SEC_COMMAND_RESPONSE, Gatt.PATCH_CONTROL, Gatt.PATCH_STATUS, Gatt.GLUCOSE_DATA, Gatt.HISTORIC_DATA);
    /** Enabled only after Phase 6: the sensor does not stream until each gets a fresh CCCD write. */
    private static final List<UUID> AFTER_PAIRING;

    static {
        List<UUID> afterPairing = new ArrayList<>(Gatt.DATA_PLANE_NOTIFYING);
        afterPairing.add(Gatt.HISTORIC_DATA);
        AFTER_PAIRING = afterPairing;
    }

    /** What the collector hears from the sensor. Called on the worker thread. */
    public interface Listener {
        void onStage(String stage);

        /** The state now holds the kAuth and identity label that worked; persist it. */
        void onPaired(Libre3SensorState state);

        void onReading(RealtimeReading reading);

        void onStatus(PatchStatus status);

        void onHistory(List<HistoricalPage.Entry> entries);

        void onError(String message);
    }

    private final Libre3SensorState state;
    private final List<AppIdentity> identities;
    private final Listener listener;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final ExecutorService worker = Executors.newSingleThreadExecutor();
    private final SecureRandom random = new SecureRandom();
    private final Map<UUID, BluetoothGattCharacteristic> chars = new ConcurrentHashMap<>();
    private final Map<UUID, BlockingQueue<byte[]>> inbox = new ConcurrentHashMap<>();
    private final GlucoseNotifyAssembler assembler = new GlucoseNotifyAssembler();

    private volatile boolean running;
    private volatile DataPlaneCrypto crypto;
    private int reconnectAttempt;
    private int identityIndex;
    /** Worker thread only. */
    private int outboundSequence;
    /** Worker thread only; 0 when none was sent on this connection. */
    private long lastBackfillAt;

    /**
     * @param identities the app identities to pair with, in file order; the one named in the state is tried first
     */
    public Libre3Ble(@NonNull Context context, Libre3SensorState state, List<AppIdentity> identities,
                     Listener listener) {
        super(context);
        this.state = state;
        this.identities = new ArrayList<>(identities);
        this.listener = listener;
        setConnectionObserver(connectionObserver);
    }

    public void start() {
        running = true;
        attempt();
    }

    /** Disconnects for good; a stopped instance is not restarted. */
    public void stop() {
        running = false;
        handler.removeCallbacksAndMessages(null);
        stopScan();
        disconnect().enqueue();
        worker.shutdownNow();
        close();
    }

    /**
     * Strips the value from a Nordic log line about a security characteristic, and from every
     * {@code characteristic.setValue(..)} line, which does not say which characteristic it is for.
     */
    public static String redact(String nordicMessage) {
        if (nordicMessage.startsWith("characteristic.setValue(")) {
            return "characteristic.setValue(redacted)";
        }
        for (UUID characteristic : Gatt.HANDSHAKE_NOTIFYING) {
            if (nordicMessage.contains(characteristic.toString())) {
                int at = nordicMessage.indexOf(", value");
                return at < 0 ? nordicMessage : nordicMessage.substring(0, at) + ", value redacted";
            }
        }
        return nordicMessage;
    }

    @Override
    public void log(int priority, @NonNull String message) {
        Log.println(priority, TAG, redact(message));
    }

    // ----------------------------------------------- connect and scan ------------------------------------------------

    private String localName() {
        return state.bleAddress().replace(":", "").toUpperCase(Locale.ROOT);
    }

    @SuppressLint("MissingPermission")
    private void attempt() {
        if (!running) {
            return;
        }
        boolean byAddress = reconnectAttempt % 2 == 0 && BluetoothAdapter.checkBluetoothAddress(state.bleAddress());
        if (byAddress) {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            if (adapter == null) {
                listener.onError("No Bluetooth adapter");
                return;
            }
            connectTo(adapter.getRemoteDevice(state.bleAddress()));
        } else {
            startScan();
        }
    }

    private void startScan() {
        List<ScanFilter> filters = new ArrayList<>();
        filters.add(new ScanFilter.Builder().setDeviceName(localName()).build());
        if (BluetoothAdapter.checkBluetoothAddress(state.bleAddress())) {
            filters.add(new ScanFilter.Builder().setDeviceAddress(state.bleAddress()).build());
        }
        ScanSettings settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();
        try {
            listener.onStage("Scanning for " + localName());
            handler.postDelayed(scanTimeout, SCAN_TIMEOUT_MS);
            BluetoothLeScannerCompat.getScanner().startScan(filters, settings, scanCallback);
        } catch (SecurityException e) {
            listener.onError("No permission to scan: " + e.getMessage());
        } catch (RuntimeException e) {
            listener.onError("Scan failed: " + e);
            scheduleReconnect("scan failed");
        }
    }

    private void stopScan() {
        handler.removeCallbacks(scanTimeout);
        try {
            BluetoothLeScannerCompat.getScanner().stopScan(scanCallback);
        } catch (RuntimeException ignored) {
            // not scanning, or Bluetooth is off
        }
    }

    private final Runnable scanTimeout = () -> {
        stopScan();
        scheduleReconnect("sensor not seen");
    };

    private final ScanCallback scanCallback = new ScanCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onScanResult(int callbackType, @NonNull ScanResult result) {
            BluetoothDevice device = result.getDevice();
            String name = result.getScanRecord() != null ? result.getScanRecord().getDeviceName() : null;
            if (localName().equals(name) || state.bleAddress().equalsIgnoreCase(device.getAddress())) {
                stopScan();
                connectTo(device);
            }
        }
    };

    private void connectTo(BluetoothDevice device) {
        if (!running) {
            return;
        }
        listener.onStage("Connecting");
        try {
            connect(device).useAutoConnect(false).retry(3, 100).enqueue();
        } catch (SecurityException e) {
            listener.onError("No permission to connect: " + e.getMessage());
        }
    }

    private void scheduleReconnect(String reason) {
        if (!running) {
            return;
        }
        reconnectAttempt++;
        long delay = Math.min(MAX_RECONNECT_DELAY_MS, reconnectAttempt * RECONNECT_STEP_MS);
        listener.onStage("Reconnecting in " + delay / 1000 + " s: " + reason);
        handler.removeCallbacks(attemptRunnable);
        handler.postDelayed(attemptRunnable, delay);
    }

    private final Runnable attemptRunnable = this::attempt;

    private final ConnectionObserver connectionObserver = new ConnectionObserver() {
        @Override
        public void onDeviceConnecting(@NonNull BluetoothDevice device) {
        }

        @Override
        public void onDeviceConnected(@NonNull BluetoothDevice device) {
            listener.onStage("Connected");
        }

        @Override
        public void onDeviceFailedToConnect(@NonNull BluetoothDevice device, int reason) {
            scheduleReconnect("connect failed, reason " + reason);
        }

        @Override
        public void onDeviceReady(@NonNull BluetoothDevice device) {
            if (running) {
                worker.execute(Libre3Ble.this::session);
            }
        }

        @Override
        public void onDeviceDisconnecting(@NonNull BluetoothDevice device) {
        }

        @Override
        public void onDeviceDisconnected(@NonNull BluetoothDevice device, int reason) {
            crypto = null;
            if (running) {
                worker.execute(assembler::reset);
            }
            scheduleReconnect("disconnected, reason " + reason);
        }
    };

    // --------------------------------------------------- GATT ----------------------------------------------------

    @Override
    protected boolean isRequiredServiceSupported(@NonNull BluetoothGatt gatt) {
        chars.clear();
        for (BluetoothGattService service : gatt.getServices()) {
            for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                chars.put(characteristic.getUuid(), characteristic);
            }
        }
        return chars.keySet().containsAll(REQUIRED);
    }

    @Override
    protected void initialize() {
        inbox.clear();
        for (UUID uuid : Gatt.HANDSHAKE_NOTIFYING) {
            BluetoothGattCharacteristic characteristic = chars.get(uuid);
            setNotificationCallback(characteristic).with((device, data) ->
                    queue(uuid).offer(data.getValue() != null ? data.getValue() : new byte[0]));
            enableNotifications(characteristic).enqueue();
        }
        for (UUID uuid : AFTER_PAIRING) {
            BluetoothGattCharacteristic characteristic = chars.get(uuid);
            if (characteristic != null) {
                setNotificationCallback(characteristic).with((device, data) -> {
                    byte[] value = data.getValue();
                    if (value != null && running) {
                        worker.execute(() -> onDataPlane(uuid, value));
                    }
                });
            }
        }
    }

    @Override
    protected void onServicesInvalidated() {
        chars.clear();
        crypto = null;
    }

    private BlockingQueue<byte[]> queue(UUID characteristic) {
        BlockingQueue<byte[]> queue = inbox.get(characteristic);
        if (queue == null) {
            inbox.putIfAbsent(characteristic, new LinkedBlockingQueue<>());
            queue = inbox.get(characteristic);
        }
        return queue;
    }

    @Override
    public void write(UUID characteristic, byte[] value) throws IOException {
        BluetoothGattCharacteristic target = chars.get(characteristic);
        if (target == null) {
            throw new IOException("Characteristic " + characteristic + " is not available");
        }
        try {
            writeCharacteristic(target, value, WRITE_TYPE_DEFAULT).await();
        } catch (RequestFailedException e) {
            throw new IOException("Write failed with status " + e.getStatus());
        } catch (DeviceDisconnectedException | BluetoothDisabledException | InvalidRequestException e) {
            throw new IOException("Write failed: " + e.getClass().getSimpleName());
        }
    }

    @Override
    public byte[] awaitNotify(UUID characteristic, long timeoutMs) throws IOException {
        BlockingQueue<byte[]> queue = queue(characteristic);
        long deadline = SystemClock.elapsedRealtime() + timeoutMs;
        while (true) {
            if (!isConnected()) {
                throw new IOException("Disconnected while waiting for a notify");
            }
            long left = deadline - SystemClock.elapsedRealtime();
            if (left <= 0) {
                throw new IOException("No notify within " + timeoutMs + " ms");
            }
            try {
                byte[] value = queue.poll(Math.min(left, POLL_SLICE_MS), TimeUnit.MILLISECONDS);
                if (value != null) {
                    return value;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Interrupted while waiting for a notify");
            }
        }
    }

    // ------------------------------------------------- session ---------------------------------------------------

    /** The identity named in the state first, then the rest in file order; advanced after a failed first pair. */
    private AppIdentity currentIdentity() {
        List<AppIdentity> ordered = new ArrayList<>();
        for (AppIdentity identity : identities) {
            if (identity.label().equals(state.identityLabel())) {
                ordered.add(identity);
            }
        }
        for (AppIdentity identity : identities) {
            if (!ordered.contains(identity)) {
                ordered.add(identity);
            }
        }
        return ordered.isEmpty() ? null : ordered.get(identityIndex % ordered.size());
    }

    private void session() {
        crypto = null;
        outboundSequence = 0;
        lastBackfillAt = 0;
        AppIdentity identity = currentIdentity();
        if (identity == null) {
            listener.onError("No Libre 3 app identity loaded");
            running = false;
            disconnect().enqueue();
            return;
        }
        byte[] cached = identity.label().equals(state.identityLabel()) ? state.kAuth() : null;
        PairingFlow flow = new PairingFlow(this, identity, () -> EphemeralExchange.generate(random));
        try {
            listener.onStage("Pairing with identity " + identity.label() + (cached != null ? ", cached key" : ""));
            PairingResult result = cached != null
                    ? flow.reconnect(cached, state.blePin())
                    : flow.firstPair(state.blePin());
            state.setKAuth(result.kAuth());
            state.setIdentityLabel(identity.label());
            listener.onStage("Paired at " + result.lastStage() + (result.usedCache() ? ", cached key" : ", fresh key"));
            listener.onPaired(state);
            reconnectAttempt = 0;
            crypto = new DataPlaneCrypto(result.kEnc(), result.ivEnc());
            for (UUID uuid : AFTER_PAIRING) {
                BluetoothGattCharacteristic characteristic = chars.get(uuid);
                if (characteristic != null) {
                    enableNotifications(characteristic).await();
                }
            }
            listener.onStage("Streaming");
        } catch (GeneralSecurityException | IOException e) {
            if (cached == null) {
                identityIndex++;
            } else if (e instanceof GeneralSecurityException) {
                // the sensor no longer holds this key; the next connection pairs afresh
                state.setKAuth(null);
            }
            listener.onError(e.getMessage());
            disconnect().enqueue();
        } catch (RequestFailedException | DeviceDisconnectedException | BluetoothDisabledException
                 | InvalidRequestException e) {
            listener.onError("Enabling data notifications failed: " + e.getClass().getSimpleName());
            disconnect().enqueue();
        }
    }

    /** Worker thread. */
    private void onDataPlane(UUID characteristic, byte[] value) {
        DataPlaneCrypto keys = crypto;
        DataPlaneCrypto.Channel channel = DataPlaneCrypto.Channel.forCharacteristic(characteristic);
        if (keys == null || channel == null) {
            return;
        }
        byte[] frame = channel == DataPlaneCrypto.Channel.GLUCOSE_DATA ? assembler.add(value) : value;
        if (frame == null) {
            return;
        }
        try {
            byte[] plain = keys.decrypt(channel, frame);
            switch (channel) {
                case PATCH_STATUS:
                    listener.onStatus(PatchStatus.parse(plain));
                    break;
                case GLUCOSE_DATA:
                    RealtimeReading reading = RealtimeReading.parse(plain);
                    requestBackfillAfterGap(keys, reading.lifeCount());
                    listener.onReading(reading);
                    break;
                case HISTORIC_DATA:
                    listener.onHistory(HistoricalPage.parse(plain));
                    break;
            }
        } catch (GeneralSecurityException | IllegalArgumentException e) {
            listener.onError("Dropped a " + channel + " frame: " + e.getMessage());
        }
    }

    /** Worker thread. Asks for history after the last stored reading when this one does not follow it. */
    private void requestBackfillAfterGap(DataPlaneCrypto keys, int lifeCount) {
        Integer last = state.lastLifeCount();
        if (last == null || lifeCount <= last + 1) {
            return;
        }
        long now = SystemClock.elapsedRealtime();
        if (lastBackfillAt != 0 && now - lastBackfillAt < BACKFILL_INTERVAL_MS) {
            return;
        }
        lastBackfillAt = now;
        outboundSequence++;
        try {
            write(Gatt.PATCH_CONTROL, keys.encrypt(PatchControl.WRITE_KIND, outboundSequence,
                    PatchControl.backfillFrom(last + 1)));
            listener.onStage("Backfill requested from life count " + (last + 1));
        } catch (GeneralSecurityException | IOException e) {
            listener.onError("Backfill request failed: " + e.getMessage());
        }
    }
}
