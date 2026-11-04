package lwld.libre3.ble;

import static com.google.common.truth.Truth.assertThat;
import static lwld.libre3.pairing.FakeSensor.IV_ENC;
import static lwld.libre3.pairing.FakeSensor.K_ENC;
import static org.robolectric.Shadows.shadowOf;

import android.bluetooth.BluetoothAdapter;
import android.os.Looper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.security.GeneralSecurityException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

import lwld.libre3.Libre3SensorState;
import lwld.libre3.data.DataPlaneCrypto;
import lwld.libre3.data.HistoricalPage;
import lwld.libre3.data.PatchStatus;
import lwld.libre3.data.RealtimeReading;
import lwld.libre3.pairing.FakeSensor;
import lwld.libre3.util.Hex;

/**
 * Tests for a whole {@link Libre3Ble} connection, from connect through pairing to streaming and back, against a fake
 * sensor behind Robolectric's Bluetooth stack. The main looper is run in real time, since the worker pairs while the
 * test waits; the clock is left alone, so no wait inside the handshake runs out.
 *
 * @author Asbjørn Aarrestad - 2026.11
 */
@RunWith(RobolectricTestRunner.class)
public class Libre3BleSessionTest {

    private static final String ADDRESS = "AA:BB:CC:DD:EE:FF";
    private static final byte[] KAUTH = Hex.toBytes("404142434445464748494a4b4c4d4e4f");
    /** The GATT status of a supervision timeout, the usual end of a link when the sensor goes out of range. */
    private static final int GATT_CONN_TIMEOUT = 8;
    private static final long PUMP_TIMEOUT_MS = 10_000;
    /** The first attempt that waits for the sensor in the background, after six active ones. */
    private static final int FIRST_BACKGROUND_ATTEMPT = 6;
    /** As many frames in a row as it takes to disconnect when they fail to decrypt. */
    private static final int MAX_DROPPED_IN_A_ROW = 5;
    private static final String DROPPED_STATUS =
            "Dropped a PATCH_STATUS frame: No descriptor opens the PATCH_STATUS frame";
    private static final String DISCONNECTING = "Disconnecting after 5 dropped frames in a row";
    /** The size of the first of the two notifies a reading arrives in. */
    private static final int GLUCOSE_FIRST_HALF = 15;

    private final FakeSensor sensor = new FakeSensor();
    /** What escaped a thread; on Android it ends the app. */
    private final List<Throwable> uncaught = new CopyOnWriteArrayList<>();
    private RecordingListener recorder = new RecordingListener();
    private Thread.UncaughtExceptionHandler previousHandler;
    private FakeGattSensor gatt;
    private Libre3Ble ble;

    @Before
    public void setUp() {
        previousHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler((thread, e) -> uncaught.add(e));
        shadowOf(BluetoothAdapter.getDefaultAdapter()).setEnabled(true);
        gatt = new FakeGattSensor(ADDRESS, sensor);
    }

    @After
    public void tearDown() {
        if (ble != null) {
            ble.stop();
        }
        Thread.setDefaultUncaughtExceptionHandler(previousHandler);
    }

    // =================================================== streaming ===================================================

    /** A sensor with a cached key pairs again, and its status frames reach the collector. */
    @Test
    public void start_cachedKey_streams() throws Exception {
        // :: Setup
        ble = cachedKeyBle();

        // :: Act
        ble.start();
        stream();

        // :: Verify
        assertThat(recorder.stages).containsAtLeast("Connecting", "Pairing with identity test, cached key",
                "Streaming").inOrder();
        assertThat(recorder.statuses).hasSize(2);
        assertThat(recorder.errors).isEmpty();
    }

    /** A reading arrives in two notifies, and only the joined frame decrypts; the collector gets it once, whole. */
    @Test
    public void reading_inTwoHalves_reachesCollector() throws Exception {
        // :: Setup
        ble = cachedKeyBle();
        ble.start();
        awaitStreaming();

        // :: Act
        notifyReading(1, 1073, 151);
        pumpUntil(() -> !recorder.readings.isEmpty());
        idleMainLooper();

        // :: Verify
        assertThat(recorder.readings).hasSize(1);
        assertThat(recorder.readings.get(0).lifeCount()).isEqualTo(1073);
        assertThat(recorder.readings.get(0).mgdl()).isEqualTo(151);
        assertThat(recorder.errors).isEmpty();
    }

    /** A page of history reaches the collector with the life count and value of each of its six readings. */
    @Test
    public void historicPage_reachesCollector() throws Exception {
        // :: Setup
        ble = cachedKeyBle();
        ble.start();
        awaitStreaming();

        // :: Act
        gatt.notifyData(Gatt.HISTORIC_DATA, frame(DataPlaneCrypto.PacketKind.KIND4, 1, historicPage(1000, 100)));
        pumpUntil(() -> !recorder.history.isEmpty());

        // :: Verify
        assertThat(recorder.history).hasSize(6);
        assertThat(recorder.history.get(0).lifeCount).isEqualTo(1000);
        assertThat(recorder.history.get(0).mgdl).isEqualTo(100);
        assertThat(recorder.history.get(5).lifeCount).isEqualTo(1025);
        assertThat(recorder.history.get(5).mgdl).isEqualTo(105);
        assertThat(recorder.errors).isEmpty();
    }

    // ================================================ dropped frames =================================================

    /**
     * Frames that keep failing to decrypt mean the session keys no longer fit; the fifth in a row disconnects, so the
     * next connection pairs again.
     */
    @Test
    public void fiveUndecryptableFramesInARow_disconnects() throws Exception {
        // :: Setup
        ble = cachedKeyBle();
        ble.start();
        awaitStreaming();
        int before = recorder.stages.size();

        // :: Act
        for (int sequence = 1; sequence <= MAX_DROPPED_IN_A_ROW; sequence++) {
            gatt.notifyData(Gatt.PATCH_STATUS, foreignStatusFrame(sequence));
        }
        pumpUntil(() -> !reconnects(recorder.stages.subList(before, recorder.stages.size())).isEmpty());

        // :: Verify
        List<String> expected = new ArrayList<>(Collections.nCopies(MAX_DROPPED_IN_A_ROW, DROPPED_STATUS));
        expected.add(DISCONNECTING);
        assertThat(recorder.errors).containsExactlyElementsIn(expected).inOrder();
    }

    // ============================================ reconnect after a drop =============================================

    /**
     * A link that streamed and then dropped is most likely a short gap; the sensor is still at its address, so the
     * first retry connects to it at once instead of waiting 5 seconds to scan.
     */
    @Test
    public void drop_afterStreaming_reconnectsAtOnceByAddress() throws Exception {
        // :: Setup
        ble = cachedKeyBle();
        ble.start();
        stream();
        int before = recorder.stages.size();

        // :: Act
        gatt.drop(GATT_CONN_TIMEOUT);
        pumpUntil(() -> recorder.stages.size() > before);
        idleMainLooper();

        // :: Verify
        List<String> after = recorder.stages.subList(before, recorder.stages.size());
        assertThat(after.get(0)).isEqualTo("Reconnecting in 0 s: disconnected, reason 10 (timeout)");
        assertThat(after.get(1)).isEqualTo("Connecting");
        assertThat(gatt.connections()).isEqualTo(2);
    }

    /**
     * A connection made by a background wait is cancelled when it drops, and that cancel reports a disconnect of its
     * own. The retry still goes at once, and the cancel's report does not schedule a second one.
     */
    @Test
    public void drop_afterStreamingInTheBackground_reconnectsOnceAtOnce() throws Exception {
        // :: Setup
        ble = cachedKeyBle();
        ble.start(FIRST_BACKGROUND_ATTEMPT);
        stream();
        int before = recorder.stages.size();

        // :: Act
        gatt.drop(GATT_CONN_TIMEOUT);
        pumpUntil(() -> streamingSince(before));

        // :: Verify
        List<String> after = recorder.stages.subList(before, recorder.stages.size());
        assertThat(after.get(0)).isEqualTo("Reconnecting in 0 s: disconnected, reason 3 (link loss)");
        assertThat(after.get(1)).isEqualTo("Connecting");
        assertThat(reconnects(after)).hasSize(1);
        assertThat(gatt.connections()).isEqualTo(2);
    }

    /**
     * Only the first retry after streaming is immediate. When that connection fails to pair, the next retry waits as
     * usual, so a sensor that has stopped accepting the phone is not hammered.
     */
    @Test
    public void drop_afterStreamingThenFailedPair_backsOff() throws Exception {
        // :: Setup
        ble = cachedKeyBle();
        ble.start();
        stream();
        sensor.acceptedKAuth = null;
        int before = recorder.stages.size();

        // :: Act
        gatt.drop(GATT_CONN_TIMEOUT);
        pumpUntil(() -> reconnects(recorder.stages.subList(before, recorder.stages.size())).size() == 2);

        // :: Verify
        assertThat(reconnects(recorder.stages.subList(before, recorder.stages.size()))).containsExactly(
                "Reconnecting in 0 s: disconnected, reason 10 (timeout)",
                "Reconnecting in 5 s: disconnected, reason 0 (success)").inOrder();
        assertThat(recorder.errors).contains("Cached key refused at ChallengeLoadDone");
    }

    // ============================================== choice of identity ===============================================

    /**
     * When the first identity fails to pair and a later one pairs, a reconnect starts from the one that paired, with
     * its cached key, instead of trying the one that failed again.
     */
    @Test
    public void drop_afterLaterIdentityPaired_reconnectsWithItsCachedKey() throws Exception {
        // :: Setup
        ble = new Libre3Ble(RuntimeEnvironment.getApplication(),
                new Libre3SensorState("0RRC989AQ", ADDRESS, new byte[]{1, 2, 3, 4}),
                Arrays.asList(FakeSensor.unknownIdentity("other"), FakeSensor.identity("test")), recorder);
        ble.start();
        pumpUntil(() -> reconnects(recorder.stages).size() == 1);
        // the retry after 5 s scans and is refused without location permission; the one after 10 s is by address
        shadowOf(Looper.getMainLooper()).idleFor(Duration.ofSeconds(5));
        shadowOf(Looper.getMainLooper()).idleFor(Duration.ofSeconds(10));
        stream();
        int before = recorder.stages.size();

        // :: Act
        gatt.drop(GATT_CONN_TIMEOUT);
        pumpUntil(() -> pairingSince(before) != null);

        // :: Verify
        assertThat(recorder.stages.subList(0, before)).containsAtLeast("Pairing with identity other",
                "Pairing with identity test", "Streaming").inOrder();
        assertThat(pairingSince(before)).isEqualTo("Pairing with identity test, cached key");
    }

    // =============================================== listener failures ===============================================

    /**
     * A status the collector fails to store, say on a full disk, is reported as an error. It does not escape the
     * worker, where it would end the app, and it is not counted as a frame that failed to decrypt.
     */
    @Test
    public void statusListenerThrows_reportsErrorAndStaysConnected() throws Exception {
        // :: Setup
        recorder = new RecordingListener() {
            @Override
            public void onStatus(PatchStatus status) {
                super.onStatus(status);
                throw new IllegalStateException("disk full");
            }
        };
        ble = cachedKeyBle();
        ble.start();
        awaitStreaming();

        // :: Act
        for (int sequence = 1; sequence <= MAX_DROPPED_IN_A_ROW; sequence++) {
            gatt.notifyData(Gatt.PATCH_STATUS, statusFrame(sequence));
        }
        pumpUntil(() -> recorder.errors.size() + uncaught.size() == MAX_DROPPED_IN_A_ROW);
        idleMainLooper();

        // :: Verify
        assertThat(uncaught).isEmpty();
        assertThat(recorder.errors).containsExactlyElementsIn(Collections.nCopies(MAX_DROPPED_IN_A_ROW,
                "Could not handle a PATCH_STATUS frame: java.lang.IllegalStateException: disk full"));
        assertThat(recorder.stages.get(recorder.stages.size() - 1)).isEqualTo("Streaming");
    }

    /**
     * A reading the collector fails with, say on a value it does not expect, is reported as not handled. It is not
     * counted as a frame that failed to decrypt, so five in a row do not disconnect.
     */
    @Test
    public void readingListenerThrowsIllegalArgument_notCountedAsDropped() throws Exception {
        // :: Setup
        recorder = new RecordingListener() {
            @Override
            public void onReading(RealtimeReading reading) {
                super.onReading(reading);
                throw new IllegalArgumentException("unexpected value");
            }
        };
        ble = cachedKeyBle();
        ble.start();
        awaitStreaming();

        // :: Act
        for (int sequence = 1; sequence <= MAX_DROPPED_IN_A_ROW; sequence++) {
            notifyReading(sequence, 1000 + sequence, 100);
        }
        pumpUntil(() -> recorder.errors.size() + uncaught.size() >= MAX_DROPPED_IN_A_ROW);
        idleMainLooper();

        // :: Verify
        assertThat(uncaught).isEmpty();
        assertThat(recorder.errors).containsExactlyElementsIn(Collections.nCopies(MAX_DROPPED_IN_A_ROW,
                "Could not handle a GLUCOSE_DATA frame: java.lang.IllegalArgumentException: unexpected value"));
        assertThat(recorder.stages.get(recorder.stages.size() - 1)).isEqualTo("Streaming");
    }

    /**
     * A frame that decrypts shows the keys still fit, even when the collector then fails with it. The count of frames
     * in a row that failed to decrypt starts over, so four more after it do not disconnect.
     */
    @Test
    public void listenerThrowsBetweenUndecryptableFrames_countStartsOver() throws Exception {
        // :: Setup
        recorder = new RecordingListener() {
            @Override
            public void onStatus(PatchStatus status) {
                super.onStatus(status);
                throw new IllegalStateException("disk full");
            }
        };
        ble = cachedKeyBle();
        ble.start();
        awaitStreaming();

        // :: Act
        for (int sequence = 1; sequence <= 4; sequence++) {
            gatt.notifyData(Gatt.PATCH_STATUS, foreignStatusFrame(sequence));
        }
        gatt.notifyData(Gatt.PATCH_STATUS, statusFrame(5));
        for (int sequence = 6; sequence <= 9; sequence++) {
            gatt.notifyData(Gatt.PATCH_STATUS, foreignStatusFrame(sequence));
        }
        pumpUntil(() -> recorder.errors.size() >= 9 || recorder.errors.contains(DISCONNECTING));
        idleMainLooper();

        // :: Verify
        assertThat(recorder.errors).doesNotContain(DISCONNECTING);
        assertThat(recorder.errors).hasSize(9);
        assertThat(recorder.stages.get(recorder.stages.size() - 1)).isEqualTo("Streaming");
    }

    /**
     * Frames that decrypt show the link works, even when the collector fails with each of them; a drop after them is
     * retried at once, as after any other streaming.
     */
    @Test
    public void listenerThrows_dropAfterwardsReconnectsAtOnce() throws Exception {
        // :: Setup
        recorder = new RecordingListener() {
            @Override
            public void onStatus(PatchStatus status) {
                super.onStatus(status);
                throw new IllegalStateException("disk full");
            }
        };
        ble = cachedKeyBle();
        ble.start();
        stream();
        int before = recorder.stages.size();

        // :: Act
        gatt.drop(GATT_CONN_TIMEOUT);
        pumpUntil(() -> recorder.stages.size() > before);

        // :: Verify
        assertThat(recorder.stages.get(before)).isEqualTo("Reconnecting in 0 s: disconnected, reason 10 (timeout)");
    }

    // ============================================== unanswered requests ==============================================

    /**
     * A write the sensor never answers fails after 5 seconds and the phone disconnects, instead of waiting forever.
     * Nordic's own timer runs in real time and its await's wait by the clock; whichever runs out first names the error.
     */
    @Test
    public void writeNeverAnswered_failsAndDisconnects() {
        // :: Setup
        ble = cachedKeyBle();
        gatt.unansweredWrites = Gatt.SEC_COMMAND_RESPONSE;
        ble.start();
        pumpUntil(() -> recorder.stages.contains("Pairing with identity test, cached key"));

        // :: Act
        pumpUntil(() -> !reconnects(recorder.stages).isEmpty(), Duration.ofSeconds(1));

        // :: Verify
        assertThat(recorder.errors).hasSize(1);
        assertThat(recorder.errors.get(0)).isAnyOf(
                "Libre 3 pairing failed at StartAuthorization: No answer to a write within 5000 ms",
                "Libre 3 pairing failed at StartAuthorization: Write failed with status -5");
    }

    /**
     * Data notifications the sensor never confirms fail after 5 seconds and the phone disconnects, instead of waiting
     * forever without streaming. As with a write, whichever of Nordic's two timeouts runs out first names the error.
     */
    @Test
    public void notificationsNeverConfirmed_failsAndDisconnects() {
        // :: Setup
        ble = cachedKeyBle();
        gatt.unconfirmedNotifications = Gatt.PATCH_STATUS;
        ble.start();
        pumpUntil(() -> recorder.stages.stream().anyMatch(stage -> stage.startsWith("Paired at")));

        // :: Act
        pumpUntil(() -> !reconnects(recorder.stages).isEmpty(), Duration.ofSeconds(1));

        // :: Verify
        assertThat(recorder.errors).hasSize(1);
        assertThat(recorder.errors.get(0)).isAnyOf("Enabling data notifications got no answer within 5000 ms",
                "Enabling data notifications failed: RequestFailedException");
        assertThat(recorder.stages).doesNotContain("Streaming");
    }

    // ==================================================== helpers ====================================================

    /** A sensor at a known address whose cached key it still accepts. */
    private Libre3Ble cachedKeyBle() {
        Libre3SensorState state = new Libre3SensorState("0RRC989AQ", ADDRESS, new byte[]{1, 2, 3, 4});
        state.setKAuth(KAUTH);
        state.setIdentityLabel("test");
        sensor.acceptedKAuth = KAUTH;
        return new Libre3Ble(RuntimeEnvironment.getApplication(), state,
                Collections.singletonList(FakeSensor.identity("test")), recorder);
    }

    /**
     * Waits for the current connection to stream, then sends two status frames and waits for both. The first frame's
     * bookkeeping is posted before the second is read, so it has run by the time the next main-looper task does.
     */
    private void stream() throws GeneralSecurityException {
        awaitStreaming();
        int seen = recorder.statuses.size();
        gatt.notifyData(Gatt.PATCH_STATUS, statusFrame(seen + 1));
        gatt.notifyData(Gatt.PATCH_STATUS, statusFrame(seen + 2));
        pumpUntil(() -> recorder.statuses.size() == seen + 2);
    }

    private void awaitStreaming() {
        pumpUntil(() -> !recorder.stages.isEmpty()
                && recorder.stages.get(recorder.stages.size() - 1).equals("Streaming"));
    }

    /** A patch status frame as the sensor encrypts it with the session keys from {@link FakeSensor}. */
    private static byte[] statusFrame(int sequence) throws GeneralSecurityException {
        return frame(DataPlaneCrypto.PacketKind.KIND2, sequence, new byte[12]);
    }

    /** A patch status frame sealed with a key this session does not hold, so it never decrypts. */
    private static byte[] foreignStatusFrame(int sequence) throws GeneralSecurityException {
        return new DataPlaneCrypto(new byte[16], IV_ENC).encrypt(DataPlaneCrypto.PacketKind.KIND2, sequence,
                new byte[12]);
    }

    /** A frame as the sensor encrypts it with the session keys from {@link FakeSensor}. */
    private static byte[] frame(DataPlaneCrypto.PacketKind kind, int sequence, byte[] plain)
            throws GeneralSecurityException {
        return new DataPlaneCrypto(K_ENC, IV_ENC).encrypt(kind, sequence, plain);
    }

    /** Sends a reading the way the sensor does: the first 15 bytes of the frame in one notify, the rest in the next. */
    private void notifyReading(int sequence, int lifeCount, int mgdl) throws GeneralSecurityException {
        byte[] plain = new byte[RealtimeReading.SIZE];
        putU16(plain, 0, lifeCount);
        putU16(plain, 15, mgdl);
        byte[] wire = frame(DataPlaneCrypto.PacketKind.KIND3, sequence, plain);
        gatt.notifyData(Gatt.GLUCOSE_DATA, Arrays.copyOfRange(wire, 0, GLUCOSE_FIRST_HALF));
        gatt.notifyData(Gatt.GLUCOSE_DATA, Arrays.copyOfRange(wire, GLUCOSE_FIRST_HALF, wire.length));
    }

    /** Six historic readings five minutes apart from {@code lifeCount}, rising 1 mg/dL at a time from {@code mgdl}. */
    private static byte[] historicPage(int lifeCount, int mgdl) {
        byte[] plain = new byte[HistoricalPage.SIZE];
        putU16(plain, 0, lifeCount);
        for (int i = 0; i < 6; i++) {
            putU16(plain, 2 + 2 * i, mgdl + i);
        }
        return plain;
    }

    private static void putU16(byte[] bytes, int offset, int value) {
        bytes[offset] = (byte) value;
        bytes[offset + 1] = (byte) (value >> 8);
    }

    /** Whether a stage after the first {@code before} says the sensor streams again. */
    private boolean streamingSince(int before) {
        return recorder.stages.subList(before, recorder.stages.size()).contains("Streaming");
    }

    /** @return the first pairing stage after the first {@code before}, or null when none came yet */
    private String pairingSince(int before) {
        for (String stage : recorder.stages.subList(before, recorder.stages.size())) {
            if (stage.startsWith("Pairing with identity")) {
                return stage;
            }
        }
        return null;
    }

    private static List<String> reconnects(List<String> stages) {
        List<String> reconnects = new ArrayList<>();
        for (String stage : stages) {
            if (stage.startsWith("Reconnecting")) {
                reconnects.add(stage);
            }
        }
        return reconnects;
    }

    /** Runs the main looper in real time until the condition holds, without moving the clock. */
    private void pumpUntil(BooleanSupplier condition) {
        pumpUntil(condition, Duration.ZERO);
    }

    /** Runs the main looper in real time until the condition holds, moving the clock by {@code step} each round. */
    private void pumpUntil(BooleanSupplier condition, Duration step) {
        long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(PUMP_TIMEOUT_MS);
        while (!condition.getAsBoolean()) {
            if (System.nanoTime() > deadline) {
                throw new AssertionError("Timed out; stages " + recorder.stages + ", errors " + recorder.errors
                        + ", fake sensor failure " + gatt.failure + ", uncaught " + uncaught);
            }
            shadowOf(Looper.getMainLooper()).idleFor(step);
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                throw new AssertionError(e);
            }
        }
    }

    private static void idleMainLooper() {
        shadowOf(Looper.getMainLooper()).idle();
    }
}
