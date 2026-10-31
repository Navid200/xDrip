package com.eveningoutpost.dexdrip.cgm.libre3;

import static com.eveningoutpost.dexdrip.models.JoH.dateTimeText;
import static com.eveningoutpost.dexdrip.utilitymodels.Constants.MINUTE_IN_MS;
import static com.eveningoutpost.dexdrip.xdrip.gs;

import com.eveningoutpost.dexdrip.R;
import com.eveningoutpost.dexdrip.models.BgReading;
import com.eveningoutpost.dexdrip.models.UserError;
import com.eveningoutpost.dexdrip.utilitymodels.Pref;
import com.eveningoutpost.dexdrip.utilitymodels.StatusItem;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import lwld.libre3.Libre3SensorState;
import lwld.libre3.data.HistoricalPage;
import lwld.libre3.data.PatchStatus;
import lwld.libre3.data.RealtimeReading;
import lwld.libre3.util.Hex;

/**
 * Turns the Libre 3's realtime and historic values into xDrip readings. A reading's time is the sensor start plus
 * its life count in minutes. Realtime values are the sensor's own mg/dL, stored every minute with
 * {@code libre_one_minute} on and every five minutes otherwise, and smoothed only when the user asks. History fills
 * gaps and never replaces a reading or brings back one that was refused.
 *
 * @author Asbjørn Aarrestad - 2026.10
 */
public final class Libre3Readings {

    private static final String TAG = "Libre3Native";
    private static final String SOURCE = "Libre3";

    // LO and HI arrive as 39 and 501, which xDrip shows as LOW and HIGH
    private static final int MIN_MGDL = 39;
    private static final int MAX_MGDL = 501;
    private static final long KEEP_RECENT_MS = 60 * MINUTE_IN_MS;
    /** A realtime reading is the sensor's current minute; this allows for the start time being off either way. */
    private static final long MAX_CLOCK_OFFSET_MS = 15 * MINUTE_IN_MS;

    private final Libre3SensorState state;
    /** Recent accepted realtime values as {timestamp, mg/dL}, oldest first, for smoothing. */
    private final ArrayDeque<long[]> recent = new ArrayDeque<>();

    private PatchStatus lastStatus;
    private String lastFastData;
    private int backfilled;

    public Libre3Readings(Libre3SensorState state) {
        this.state = state;
    }

    /**
     * Stores a realtime reading when it is valid, new and on the chosen interval. A refused reading's life count is
     * remembered, so history does not store it later.
     *
     * @return true when a reading was stored
     */
    public synchronized boolean onRealtime(RealtimeReading reading, long nowMs) {
        int lifeCount = reading.lifeCount();
        lastFastData = Hex.of(reading.fastData());
        UserError.Log.d(TAG, "Realtime life count " + lifeCount + " fastData " + lastFastData);
        Integer last = state.lastLifeCount();
        if (last != null && lifeCount <= last) {
            UserError.Log.d(TAG, "Libre 3 reading at life count " + lifeCount + " not after " + last);
            return false;
        }
        String reason = refusal(lifeCount, reading.mgdl(), nowMs);
        if (reason == null && !reading.displayable()) {
            reason = "not displayable";
        }
        if (reason == null && timestamp(lifeCount) < nowMs - MAX_CLOCK_OFFSET_MS) {
            reason = "time is more than 15 minutes ago, start time looks wrong";
        }
        if (reason != null) {
            state.addRejectedLifeCount(lifeCount);
            Libre3.updateState(state);
            UserError.Log.uel(TAG, "Libre 3 reading at life count " + lifeCount + " not stored: " + reason);
            return false;
        }
        int mgdl = reading.mgdl();
        long timestamp = timestamp(lifeCount);
        state.updateLastGlucose(lifeCount, mgdl);
        if (!Libre3.updateState(state)) {
            UserError.Log.e(TAG, "Libre 3 " + state.serial() + " is no longer the stored sensor, reading not stored");
            return false;
        }
        double value = Pref.getBooleanDefaultFalse(Libre3.PREF_SMOOTHING) ? smoothed(timestamp, mgdl) : mgdl;
        remember(timestamp, mgdl);
        if (!Pref.getBooleanDefaultFalse("libre_one_minute") && lifeCount % 5 != 0) {
            return false;
        }
        return BgReading.bgReadingInsertFromGluPro(value, timestamp, SOURCE) != null;
    }

    /**
     * Stores the history entries that fill a gap: valid, not after the last realtime reading, not refused before, and
     * with no reading within a minute of their time.
     *
     * @return how many were stored
     */
    public synchronized int onHistory(List<HistoricalPage.Entry> entries, long nowMs) {
        if (!Libre3.isStored(state)) {
            UserError.Log.e(TAG, "Libre 3 " + state.serial() + " is no longer the stored sensor, history not stored");
            return 0;
        }
        Integer current = state.lastLifeCount();
        int stored = 0;
        for (HistoricalPage.Entry entry : entries) {
            String reason = current == null || entry.lifeCount > current ? "after the last reading"
                    : state.rejectedLifeCounts().contains(entry.lifeCount) ? "refused before"
                    : refusal(entry.lifeCount, entry.mgdl, nowMs);
            if (reason != null) {
                UserError.Log.d(TAG, "History at life count " + entry.lifeCount + " skipped: " + reason);
                continue;
            }
            long timestamp = timestamp(entry.lifeCount);
            if (BgReading.getForPreciseTimestamp(timestamp, MINUTE_IN_MS) != null) {
                continue;
            }
            if (BgReading.bgReadingInsertFromGluPro(entry.mgdl, timestamp, SOURCE) != null) {
                stored++;
            }
        }
        if (stored > 0) {
            backfilled += stored;
            UserError.Log.uel(TAG, "Libre 3 backfilled " + stored + " readings");
        }
        return stored;
    }

    public synchronized void onStatus(PatchStatus status) {
        lastStatus = status;
    }

    /** Status rows for this sensor. Never shows the PIN or kAuth. */
    public synchronized List<StatusItem> megaStatus() {
        List<StatusItem> si = new ArrayList<>();
        si.add(new StatusItem(gs(R.string.libre3_sensor), state.serial()));
        Integer wear = state.wearMinutes();
        if (wear != null && wear > 0) {
            // A 3 Plus is worn 15 days, a 3 for 14; the generation field is 1 on a US 3 Plus too
            si.add(new StatusItem(gs(R.string.libre3_product), wear > 14 * 24 * 60 ? "Libre 3 Plus" : "Libre 3"));
        }
        if (lastStatus != null) {
            PatchStatus.Attention attention = lastStatus.attention();
            si.add(new StatusItem(gs(R.string.libre3_sensor_state), lastStatus.state()
                    + (attention == PatchStatus.Attention.NONE ? "" : " (" + attention + ")"),
                    attention == PatchStatus.Attention.NONE ? StatusItem.Highlight.NORMAL : StatusItem.Highlight.BAD));
        }
        Integer last = state.lastLifeCount();
        if (last != null) {
            si.add(new StatusItem(gs(R.string.libre3_last_life_count), last + " (" + dateTimeText(timestamp(last))
                    + ", " + state.lastMgdl() + " mg/dL)"));
        }
        si.add(new StatusItem(gs(R.string.libre3_backfilled), Integer.toString(backfilled)));
        if (lastFastData != null) {
            si.add(new StatusItem(gs(R.string.libre3_fast_data), lastFastData));
        }
        return si;
    }

    /** Why a value at this life count is not stored, or null when it can be. */
    private String refusal(int lifeCount, Integer mgdl, long nowMs) {
        if (mgdl == null || mgdl < MIN_MGDL || mgdl > MAX_MGDL) {
            return "value " + mgdl + " outside " + MIN_MGDL + "-" + MAX_MGDL;
        }
        if (!state.lifecycle().usable(lifeCount)) {
            return state.lifecycle().phase(lifeCount).name().toLowerCase(Locale.ROOT);
        }
        if (state.activationMs() <= 0) {
            return "start time unknown";
        }
        if (timestamp(lifeCount) > nowMs + MAX_CLOCK_OFFSET_MS) {
            return "time is more than 15 minutes ahead, start time looks wrong";
        }
        return null;
    }

    private long timestamp(int lifeCount) {
        return state.activationMs() + lifeCount * MINUTE_IN_MS;
    }

    private void remember(long timestamp, int mgdl) {
        recent.addLast(new long[]{timestamp, mgdl});
        Iterator<long[]> it = recent.iterator();
        while (it.hasNext() && it.next()[0] < timestamp - KEEP_RECENT_MS) {
            it.remove();
        }
    }

    /** LibreReceiver's weighted average: over {@code libre_filter_length} minutes, 20 when it is the default 25. */
    private double smoothed(long timestamp, int mgdl) {
        long minutes = Math.max(1, Pref.getStringToInt("libre_filter_length", 25));
        long window = (minutes == 25 ? 20 : minutes) * MINUTE_IN_MS;
        double duration = minutes * MINUTE_IN_MS;
        double sum = mgdl;
        double weights = 1;
        for (long[] value : recent) {
            if (value[0] >= timestamp - window && value[0] < timestamp) {
                double weight = 1 - (timestamp - value[0]) / duration;
                sum += value[1] * weight;
                weights += weight;
            }
        }
        return Math.round(sum / weights);
    }
}
