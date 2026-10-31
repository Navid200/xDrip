package com.eveningoutpost.dexdrip.cgm.libre3;

import static com.eveningoutpost.dexdrip.models.JoH.dateTimeText;
import static com.eveningoutpost.dexdrip.models.JoH.tsl;
import static com.eveningoutpost.dexdrip.utilitymodels.Constants.DAY_IN_MS;
import static com.eveningoutpost.dexdrip.utilitymodels.Constants.HOUR_IN_MS;
import static com.eveningoutpost.dexdrip.utilitymodels.Constants.MINUTE_IN_MS;

import android.content.Context;
import android.content.Intent;

import com.eveningoutpost.dexdrip.models.Sensor;
import com.eveningoutpost.dexdrip.models.UserError;
import com.eveningoutpost.dexdrip.utilitymodels.PersistentStore;
import com.eveningoutpost.dexdrip.utilitymodels.Pref;
import com.eveningoutpost.dexdrip.utils.UniqueId;
import com.eveningoutpost.dexdrip.xdrip;

import lwld.libre3.Libre3SensorState;
import lwld.libre3.pairing.ReceiverId;

/**
 * Settings and stored state for the native Libre 3 collector: the account and fold the user chose, the one sensor
 * xDrip currently talks to, and the xDrip {@link Sensor} record its readings belong to.
 *
 * @author Asbjørn Aarrestad - 2026.10
 */
public final class Libre3 {

    private static final String TAG = "Libre3Native";

    public static final String PREF_ACCOUNT_ID = "libre3_account_id";
    public static final String PREF_FOLD = "libre3_fold";
    public static final String PREF_SMOOTHING = "libre3_smoothing";

    private static final String STATE_KEY = "libre3-sensor-state";
    private static final String RECORD_SERIAL_KEY = "libre3-record-serial";
    private static final String RECORD_UUID_KEY = "libre3-record-uuid";

    private static final long DEFAULT_WEAR_MS = 15 * DAY_IN_MS;

    private Libre3() {
    }

    public static String accountId() {
        return Pref.getString(PREF_ACCOUNT_ID, "").trim();
    }

    public static ReceiverId.Fold fold() {
        try {
            return ReceiverId.Fold.valueOf(Pref.getString(PREF_FOLD, ReceiverId.Fold.FREESTYLE_LIBRE3.name()));
        } catch (IllegalArgumentException e) {
            return ReceiverId.Fold.FREESTYLE_LIBRE3;
        }
    }

    /** Per-install ID behind an accountless start; it is gone with the install, and so is the sensor. */
    public static String uniqueId() {
        return UniqueId.get(32);
    }

    /** The sensor xDrip talks to, or null when none was started or taken over. */
    public static Libre3SensorState loadState() {
        String json = PersistentStore.getString(STATE_KEY, "");
        if (json.isEmpty()) {
            return null;
        }
        try {
            return Libre3SensorState.fromJson(json);
        } catch (IllegalArgumentException e) {
            UserError.Log.e(TAG, "Stored Libre 3 sensor state is unreadable: " + e.getMessage());
            return null;
        }
    }

    public static void saveState(Libre3SensorState state) {
        PersistentStore.setString(STATE_KEY, state.toJson());
    }

    /** Drops the stored sensor, so the next one needs an NFC start or takeover. */
    public static void forgetSensor() {
        PersistentStore.removeItem(STATE_KEY);
        PersistentStore.removeItem(RECORD_SERIAL_KEY);
        PersistentStore.removeItem(RECORD_UUID_KEY);
    }

    /** Activation time of the stored sensor, or 0 when unknown. */
    public static long getStart() {
        Libre3SensorState state = loadState();
        return state == null ? 0 : state.activationMs();
    }

    public static long getWearMs() {
        Libre3SensorState state = loadState();
        return state == null || state.wearMinutes() == null || state.wearMinutes() <= 0
                ? DEFAULT_WEAR_MS : state.wearMinutes() * MINUTE_IN_MS;
    }

    public static long getWarmupMs() {
        Libre3SensorState state = loadState();
        return state == null || state.warmupMinutes() == null ? HOUR_IN_MS : state.warmupMinutes() * MINUTE_IN_MS;
    }

    /**
     * Makes the current xDrip {@link Sensor} the one for this Libre 3. A different serial, or no current sensor,
     * stops the old one and starts a new one at the activation time, never later than now. The same serial on the
     * sensor it was given keeps it.
     */
    public static synchronized void ensureSensor(Libre3SensorState state) {
        Sensor current = Sensor.currentSensor();
        if (current != null
                && state.serial().equals(PersistentStore.getString(RECORD_SERIAL_KEY, ""))
                && current.uuid.equals(PersistentStore.getString(RECORD_UUID_KEY, ""))) {
            return;
        }
        long now = tsl();
        long start = state.activationMs() > 0 ? Math.min(state.activationMs(), now) : now;
        Sensor sensor = Sensor.create(start);
        PersistentStore.setString(RECORD_SERIAL_KEY, state.serial());
        PersistentStore.setString(RECORD_UUID_KEY, sensor.uuid);
        UserError.Log.uel(TAG, "Started xDrip sensor for Libre 3 " + state.serial() + " at "
                + dateTimeText(sensor.started_at));
    }

    static void startControlActivity() {
        Context context = xdrip.getAppContext();
        Intent intent = new Intent(context, Libre3Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
