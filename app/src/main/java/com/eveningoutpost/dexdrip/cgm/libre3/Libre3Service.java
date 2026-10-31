package com.eveningoutpost.dexdrip.cgm.libre3;

import static com.eveningoutpost.dexdrip.models.JoH.dateTimeText;
import static com.eveningoutpost.dexdrip.models.JoH.msSince;
import static com.eveningoutpost.dexdrip.models.JoH.niceTimeScalar;
import static com.eveningoutpost.dexdrip.models.JoH.tsl;
import static com.eveningoutpost.dexdrip.utilitymodels.Constants.MINUTE_IN_MS;
import static com.eveningoutpost.dexdrip.utilitymodels.Constants.SECOND_IN_MS;
import static com.eveningoutpost.dexdrip.xdrip.gs;

import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.text.SpannableString;

import androidx.annotation.Nullable;

import com.eveningoutpost.dexdrip.R;
import com.eveningoutpost.dexdrip.models.JoH;
import com.eveningoutpost.dexdrip.models.UserError;
import com.eveningoutpost.dexdrip.utilitymodels.Constants;
import com.eveningoutpost.dexdrip.utilitymodels.StatusItem;
import com.eveningoutpost.dexdrip.utils.DexCollectionType;
import com.eveningoutpost.dexdrip.utils.framework.ForegroundService;
import com.eveningoutpost.dexdrip.utils.framework.WakeLockTrampoline;
import com.eveningoutpost.dexdrip.xdrip;

import java.util.ArrayList;
import java.util.List;

import lwld.libre3.Libre3SensorState;
import lwld.libre3.ble.Libre3Ble;
import lwld.libre3.crypto.AppIdentity;
import lwld.libre3.data.HistoricalPage;
import lwld.libre3.data.PatchStatus;
import lwld.libre3.data.RealtimeReading;

/**
 * Keeps a Bluetooth connection to the Libre 3 that was started or taken over by NFC. Without a stored sensor or a
 * downloaded credential it waits; a new sensor stored by NFC replaces the connection on the next wake-up.
 *
 * @author Asbjørn Aarrestad - 2026.10
 */
public class Libre3Service extends ForegroundService {

    private static final String TAG = "Libre3Native";

    private static volatile String stage = "";
    private static volatile String lastError = "";
    private static volatile long lastReadingAt;
    private static volatile Libre3Readings readings;

    private Libre3Ble ble;
    private String bleSerial;
    private long wakeupTime;

    private final Libre3Ble.Listener listener = new Libre3Ble.Listener() {
        @Override
        public void onStage(String newStage) {
            stage = newStage;
            UserError.Log.d(TAG, "Stage: " + newStage);
        }

        @Override
        public void onPaired(Libre3SensorState state) {
            if (!Libre3.updateState(state)) {
                UserError.Log.e(TAG, "Libre 3 " + state.serial() + " paired but is no longer the stored sensor");
                return;
            }
            UserError.Log.uel(TAG, "Libre 3 " + state.serial() + " paired");
        }

        @Override
        public void onReading(RealtimeReading reading) {
            Libre3Readings current = readings;
            if (current != null && current.onRealtime(reading, tsl())) {
                lastReadingAt = tsl();
            }
        }

        @Override
        public void onStatus(PatchStatus status) {
            UserError.Log.d(TAG, "Status: life " + status.currentLifeCount() + " state " + status.state());
            Libre3Readings current = readings;
            if (current != null) {
                current.onStatus(status);
            }
        }

        @Override
        public void onHistory(List<HistoricalPage.Entry> entries) {
            UserError.Log.d(TAG, "History entries: " + entries.size());
            Libre3Readings current = readings;
            if (current != null) {
                current.onHistory(entries, tsl());
            }
        }

        @Override
        public void onError(String message) {
            lastError = message;
            UserError.Log.uel(TAG, "Libre 3 error: " + message);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final PowerManager.WakeLock wl = JoH.getWakeLock(TAG + "-osc", 60_000);
        try {
            if (!shouldServiceRun()) {
                UserError.Log.d(TAG, "Stopping service due to shouldServiceRun() result");
                stopSelf();
                return START_NOT_STICKY;
            }
            startIfNeeded();
            scheduleWakeUp();
        } finally {
            JoH.releaseWakeLock(wl);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopBle();
        super.onDestroy();
    }

    private void startIfNeeded() {
        Libre3SensorState state = Libre3.loadState();
        if (state == null) {
            stopBle();
            stage = gs(R.string.libre3_stage_no_sensor);
            return;
        }
        if (ble != null && state.serial().equals(bleSerial)) {
            return;
        }
        List<AppIdentity> identities = Libre3Credential.get(this).load();
        if (identities.isEmpty()) {
            stopBle();
            stage = gs(R.string.libre3_stage_no_credential);
            return;
        }
        stopBle();
        Libre3.ensureSensor(state);
        readings = new Libre3Readings(state);
        ble = new Libre3Ble(xdrip.getAppContext(), state, identities, listener);
        bleSerial = state.serial();
        UserError.Log.uel(TAG, "Connecting to Libre 3 " + state.serial());
        ble.start();
    }

    private void stopBle() {
        if (ble != null) {
            ble.stop();
            ble = null;
            bleSerial = null;
            readings = null;
        }
    }

    private void scheduleWakeUp() {
        long next = tsl() + MINUTE_IN_MS * 5;
        if (Math.abs(next - wakeupTime) > SECOND_IN_MS * 10) {
            wakeupTime = next;
            JoH.wakeUpIntent(xdrip.getAppContext(), JoH.msTill(next),
                    WakeLockTrampoline.getPendingIntent(Libre3Service.class, Constants.LIBRE3_SERVICE_FAILOVER_ID));
        }
    }

    private static boolean shouldServiceRun() {
        return DexCollectionType.getDexCollectionType() == DexCollectionType.Libre3Native;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /** MegaStatus rows: the sensor, where the connection is, and a button to the Libre 3 screen. */
    public static List<StatusItem> megaStatus() {
        List<StatusItem> si = new ArrayList<>();
        Libre3SensorState state = Libre3.loadState();
        Libre3Readings current = readings;
        if (current != null) {
            si.addAll(current.megaStatus());
        } else {
            si.add(new StatusItem(gs(R.string.libre3_sensor), state == null ? gs(R.string.libre3_none) : state.serial()));
        }
        if (!stage.isEmpty()) {
            si.add(new StatusItem(gs(R.string.libre3_stage), stage));
        }
        if (!lastError.isEmpty()) {
            si.add(new StatusItem(gs(R.string.libre3_last_error), lastError, StatusItem.Highlight.BAD));
        }
        if (state != null && state.activationMs() > 0) {
            si.add(new StatusItem(gs(R.string.libre3_started), dateTimeText(state.activationMs())));
            long left = state.activationMs() + Libre3.getWearMs() - tsl();
            si.add(new StatusItem(gs(R.string.libre3_remaining), left > 0 ? niceTimeScalar(left) : gs(R.string.libre3_expired)));
        }
        if (lastReadingAt > 0) {
            si.add(new StatusItem(gs(R.string.libre3_last_reading), niceTimeScalar(msSince(lastReadingAt))));
        }
        boolean credential = !Libre3Credential.get(xdrip.getAppContext()).load().isEmpty();
        si.add(new StatusItem(gs(R.string.libre3_credential),
                credential ? gs(R.string.libre3_credential_ready) : gs(R.string.libre3_none),
                credential ? StatusItem.Highlight.NORMAL : StatusItem.Highlight.BAD));
        si.add(new StatusItem(gs(R.string.libre3_title), gs(R.string.libre3_open), StatusItem.Highlight.NORMAL,
                "button-press", Libre3::startControlActivity));
        return si;
    }

    // accessed via reflection
    public static boolean isCollecting() {
        return lastReadingAt > 0 && msSince(lastReadingAt) < MINUTE_IN_MS * 12;
    }

    // accessed via reflection
    public static SpannableString nanoStatus() {
        return stage.isEmpty() || isCollecting() ? null : new SpannableString(stage);
    }
}
