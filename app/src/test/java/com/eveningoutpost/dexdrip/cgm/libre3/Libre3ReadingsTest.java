package com.eveningoutpost.dexdrip.cgm.libre3;

import static com.eveningoutpost.dexdrip.models.JoH.tsl;
import static com.eveningoutpost.dexdrip.utilitymodels.Constants.DAY_IN_MS;
import static com.eveningoutpost.dexdrip.utilitymodels.Constants.MINUTE_IN_MS;
import static com.google.common.truth.Truth.assertThat;

import com.eveningoutpost.dexdrip.RobolectricTestWithConfig;
import com.eveningoutpost.dexdrip.models.BgReading;
import com.eveningoutpost.dexdrip.models.UserError;
import com.eveningoutpost.dexdrip.utilitymodels.Pref;
import com.eveningoutpost.dexdrip.utilitymodels.StatusItem;
import com.eveningoutpost.dexdrip.xdrip;

import org.junit.After;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import java.util.List;

import lwld.libre3.Libre3SensorState;
import lwld.libre3.data.HistoricalPage;
import lwld.libre3.data.PatchStatus;
import lwld.libre3.data.RealtimeReading;
import lwld.libre3.nfc.PatchInfo;
import lwld.libre3.util.Hex;

/**
 * Tests for {@link Libre3Readings}: which realtime and historic Libre 3 values become xDrip readings, at which time,
 * and what the status page shows about them.
 *
 * @author Asbjørn Aarrestad - 2026.10
 */
public class Libre3ReadingsTest extends RobolectricTestWithConfig {

    /** A US Libre 3 Plus: 60 minutes warm-up, 21600 minutes wear. */
    private static final String PATCH_INFO = "00a50001000200010060541e020401040c0130525243393839415151ff17";
    private static final byte[] PIN = {0x32, 0x25, (byte) 0xec, 0x72};
    private static final byte[] KAUTH = Hex.toBytes("00112233445566778899aabbccddeeff");
    private static final byte[] FAST_DATA = Hex.toBytes("0102030405a0b0c0");

    private long now;
    private Libre3SensorState state;
    private Libre3Readings readings;

    @Override
    public void setUp() {
        super.setUp();
        xdrip.setContextAlways(RuntimeEnvironment.application);
        Libre3.forgetSensor();
        now = tsl();
        state = new Libre3SensorState("0RRC989AQ", "CC:22:DF:B8:F9:58", PIN);
        state.setActivationMs(now - DAY_IN_MS);
        state.applySensorCycle(PatchInfo.parse(Hex.toBytes(PATCH_INFO)));
        state.setKAuth(KAUTH);
        Libre3.ensureSensor(state);
        readings = new Libre3Readings(state);
    }

    @After
    public void tearDown() {
        Pref.removeItem("libre_one_minute");
        Pref.removeItem("libre_filter_length");
        Pref.removeItem(Libre3.PREF_SMOOTHING);
        UserError.ExtraLogTags.readPreference("");
        Libre3.forgetSensor();
    }

    // ================================================== onRealtime ===================================================

    /** With one-minute readings on, readings a minute apart are both stored at activation + life count minutes. */
    @Test
    public void onRealtime_oneMinuteOn_storesEveryMinute() {
        // :: Setup
        Pref.setBoolean("libre_one_minute", true);

        // :: Act
        readings.onRealtime(reading(1001, 120), now);
        readings.onRealtime(reading(1002, 125), now);

        // :: Verify
        assertThat(storedAt(1001).calculated_value).isEqualTo(120.0);
        assertThat(storedAt(1002).calculated_value).isEqualTo(125.0);
    }

    /** With one-minute readings off, only the reading on a five-minute life count is stored. */
    @Test
    public void onRealtime_oneMinuteOff_storesOnlyFiveMinuteLifeCount() {
        // :: Setup
        Pref.setBoolean("libre_one_minute", false);

        // :: Act
        readings.onRealtime(reading(999, 120), now);
        readings.onRealtime(reading(1000, 125), now);

        // :: Verify
        assertThat(storedAt(999)).isNull();
        assertThat(storedAt(1000).calculated_value).isEqualTo(125.0);
    }

    /** A reading with no value, a zero value or LO is not stored, and its life count is remembered as rejected. */
    @Test
    public void onRealtime_noValueZeroOrLow_notStoredAndRejected() {
        // :: Act
        readings.onRealtime(reading(1000, null), now);
        readings.onRealtime(reading(1005, 0), now);
        readings.onRealtime(reading(1010, 39), now);

        // :: Verify
        assertThat(storedAt(1000)).isNull();
        assertThat(storedAt(1005)).isNull();
        assertThat(storedAt(1010)).isNull();
        assertThat(state.rejectedLifeCounts()).containsAtLeast(1000, 1005, 1010);
    }

    /** HI is not stored either. */
    @Test
    public void onRealtime_high_notStored() {
        // :: Act
        readings.onRealtime(reading(1000, 501), now);

        // :: Verify
        assertThat(storedAt(1000)).isNull();
    }

    /** A reading during warm-up is not stored. */
    @Test
    public void onRealtime_warmup_notStored() {
        // :: Act
        readings.onRealtime(reading(55, 120), now);

        // :: Verify
        assertThat(storedAt(55)).isNull();
    }

    /** A reading at or after the end of wear is not stored. */
    @Test
    public void onRealtime_pastWear_notStored() {
        // :: Setup
        state.setActivationMs(now - 16 * DAY_IN_MS);

        // :: Act
        readings.onRealtime(reading(21600, 120), now);

        // :: Verify
        assertThat(storedAt(21600)).isNull();
    }

    /** A reading the sensor flags as not displayable is not stored. */
    @Test
    public void onRealtime_notDisplayable_notStored() {
        // :: Act
        readings.onRealtime(reading(1000, 120, 0x8001), now);

        // :: Verify
        assertThat(storedAt(1000)).isNull();
    }

    /** A repeated life count does not replace the stored value, and a lower one is not stored. */
    @Test
    public void onRealtime_repeatedOrLowerLifeCount_notStoredAgain() {
        // :: Setup
        readings.onRealtime(reading(1000, 120), now);

        // :: Act
        readings.onRealtime(reading(1000, 180), now);
        readings.onRealtime(reading(995, 180), now);

        // :: Verify
        assertThat(storedAt(1000).calculated_value).isEqualTo(120.0);
        assertThat(storedAt(995)).isNull();
    }

    /** A reading whose time is more than a minute ahead of now is not stored. */
    @Test
    public void onRealtime_inFuture_notStored() {
        // :: Act
        readings.onRealtime(reading(1445, 120), now);

        // :: Verify
        assertThat(storedAt(1445)).isNull();
    }

    /** A stored reading becomes the last life count and value, which the next backfill starts after. */
    @Test
    public void onRealtime_stored_updatesLastLifeCount() {
        // :: Act
        readings.onRealtime(reading(1000, 120), now);

        // :: Verify
        assertThat(state.lastLifeCount()).isEqualTo(1000);
        assertThat(state.lastMgdl()).isEqualTo(120);
    }

    // =================================================== onHistory ===================================================

    /** A history entry at an existing reading's time leaves that reading's value as it was. */
    @Test
    public void onHistory_existingReading_unchanged() {
        // :: Setup
        readings.onRealtime(reading(1000, 120), now);

        // :: Act
        readings.onHistory(entries(1000, 180), now);

        // :: Verify
        assertThat(storedAt(1000).calculated_value).isEqualTo(120.0);
    }

    /** A history entry for a life count whose realtime reading was rejected is not stored. */
    @Test
    public void onHistory_rejectedLifeCount_notStored() {
        // :: Setup
        readings.onRealtime(reading(990, null), now);
        readings.onRealtime(reading(1000, 120), now);

        // :: Act
        readings.onHistory(entries(990, 130), now);

        // :: Verify
        assertThat(storedAt(990)).isNull();
    }

    /** A history entry beyond the current life count is not stored. */
    @Test
    public void onHistory_beyondCurrentLifeCount_notStored() {
        // :: Setup
        readings.onRealtime(reading(1000, 120), now);

        // :: Act
        readings.onHistory(entries(1005, 130), now);

        // :: Verify
        assertThat(storedAt(1005)).isNull();
    }

    /** A valid entry in a gap is stored with the sensor's value, on a life count off the five-minute grid too. */
    @Test
    public void onHistory_gapEntry_stored() {
        // :: Setup
        Pref.setBoolean("libre_one_minute", false);
        readings.onRealtime(reading(1000, 120), now);

        // :: Act
        readings.onHistory(entries(982, 130), now);

        // :: Verify
        assertThat(storedAt(982).calculated_value).isEqualTo(130.0);
        assertThat(storedAt(987).calculated_value).isEqualTo(131.0);
    }

    // =================================================== smoothing ===================================================

    /** Smoothing is off by default, so the stored value is the sensor's value. */
    @Test
    public void smoothing_offByDefault_storesSensorValue() {
        // :: Setup
        Pref.setBoolean("libre_one_minute", true);
        readings.onRealtime(reading(1000, 100), now);

        // :: Act
        readings.onRealtime(reading(1001, 130), now);

        // :: Verify
        assertThat(storedAt(1001).calculated_value).isEqualTo(130.0);
    }

    /** With smoothing on, the stored value is the LibreReceiver weighted average over the filter length. */
    @Test
    public void smoothing_on_storesWeightedAverage() {
        // :: Setup
        Pref.setBoolean("libre_one_minute", true);
        Pref.setBoolean(Libre3.PREF_SMOOTHING, true);
        Pref.setString("libre_filter_length", "25");
        readings.onRealtime(reading(1000, 100), now);

        // :: Act
        readings.onRealtime(reading(1001, 130), now);

        // :: Verify
        // (100 × (1 − 1/25) + 130 × 1) / (0.96 + 1) = 115.3
        assertThat(storedAt(1001).calculated_value).isEqualTo(115.0);
    }

    // =================================================== fastData ====================================================

    /** The undecoded fast data of each reading goes to the log as hex. */
    @Test
    public void fastData_loggedAsHex() {
        // :: Setup
        UserError.ExtraLogTags.readPreference("Libre3Native:d");

        // :: Act
        readings.onRealtime(reading(1000, 120), now);

        // :: Verify
        boolean logged = false;
        for (UserError entry : UserError.all()) {
            logged |= entry.message.contains("0102030405a0b0c0");
        }
        assertThat(logged).isTrue();
    }

    // ================================================== megaStatus ===================================================

    /** The status rows show the sensor, its product, state, last reading, backfill count and fast data. */
    @Test
    public void megaStatus_listsSensorAndReadings() {
        // :: Setup
        readings.onStatus(PatchStatus.parse(Hex.toBytes("0000000000000004e8030000")));
        readings.onRealtime(reading(1000, 120), now);
        readings.onHistory(entries(982, 130), now);

        // :: Act
        String text = text(readings.megaStatus());

        // :: Verify
        assertThat(text).contains("0RRC989AQ");
        assertThat(text).contains("Libre 3 Plus");
        assertThat(text).contains("1000");
        assertThat(text).contains("0102030405a0b0c0");
        assertThat(text).contains("Backfilled: 4");
        assertThat(text).contains("Sensor state: 4");
    }

    /** The status rows never show the BLE PIN or kAuth. */
    @Test
    public void megaStatus_noSecrets() {
        // :: Setup
        readings.onRealtime(reading(1000, 120), now);

        // :: Act
        String text = text(readings.megaStatus()).toLowerCase();

        // :: Verify
        assertThat(text).doesNotContain(Hex.of(PIN));
        assertThat(text).doesNotContain(Hex.of(KAUTH));
    }

    /** The service status says whether the app credential is downloaded. */
    @Test
    public void megaStatus_service_showsMissingCredential() {
        // :: Act
        String text = text(Libre3Service.megaStatus());

        // :: Verify
        assertThat(text).contains("App credential: none");
    }

    // ==================================================== helpers ====================================================

    private BgReading storedAt(int lifeCount) {
        return BgReading.getForPreciseTimestamp(state.activationMs() + lifeCount * MINUTE_IN_MS, 1000);
    }

    private static RealtimeReading reading(int lifeCount, Integer mgdl) {
        return reading(lifeCount, mgdl, 0);
    }

    private static RealtimeReading reading(int lifeCount, Integer mgdl, int qualityWord) {
        byte[] b = new byte[RealtimeReading.SIZE];
        putU16(b, 0, lifeCount);
        putU16(b, 2, qualityWord);
        b[14] = 3;
        putU16(b, 15, mgdl == null ? 0 : mgdl);
        System.arraycopy(FAST_DATA, 0, b, 21, FAST_DATA.length);
        return RealtimeReading.parse(b);
    }

    /** One historic page from {@code start}, values {@code first}, {@code first + 1}, … five minutes apart. */
    private static List<HistoricalPage.Entry> entries(int start, int first) {
        byte[] b = new byte[HistoricalPage.SIZE];
        putU16(b, 0, start);
        for (int i = 0; i < 6; i++) {
            putU16(b, 2 + 2 * i, first + i);
        }
        return HistoricalPage.parse(b);
    }

    private static void putU16(byte[] b, int offset, int value) {
        b[offset] = (byte) value;
        b[offset + 1] = (byte) (value >> 8);
    }

    private static String text(List<StatusItem> items) {
        StringBuilder sb = new StringBuilder();
        for (StatusItem item : items) {
            sb.append(item.name).append(": ").append(item.value).append('\n');
        }
        return sb.toString();
    }
}
