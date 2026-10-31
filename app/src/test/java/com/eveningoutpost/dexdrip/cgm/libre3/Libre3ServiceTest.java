package com.eveningoutpost.dexdrip.cgm.libre3;

import static com.eveningoutpost.dexdrip.models.JoH.tsl;
import static com.eveningoutpost.dexdrip.utilitymodels.Constants.MINUTE_IN_MS;
import static com.google.common.truth.Truth.assertThat;

import com.eveningoutpost.dexdrip.RobolectricTestWithConfig;
import com.eveningoutpost.dexdrip.models.BgReading;
import com.eveningoutpost.dexdrip.utilitymodels.Pref;
import com.eveningoutpost.dexdrip.xdrip;

import org.junit.After;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import lwld.libre3.Libre3SensorState;
import lwld.libre3.data.PatchStatus;
import lwld.libre3.data.RealtimeReading;
import lwld.libre3.nfc.PatchInfo;
import lwld.libre3.util.Hex;

/**
 * Tests for {@link Libre3Service.Session}: each Bluetooth session hands its values to the readings of its own sensor.
 *
 * @author Asbjørn Aarrestad - 2026.10
 */
public class Libre3ServiceTest extends RobolectricTestWithConfig {

    /** A US Libre 3 Plus: 60 minutes warm-up, 21600 minutes wear. */
    private static final String PATCH_INFO = "00a50001000200010060541e020401040c0130525243393839415151ff17";

    private long now;

    @Override
    public void setUp() {
        super.setUp();
        xdrip.setContextAlways(RuntimeEnvironment.application);
        Libre3.forgetSensor();
        Pref.setBoolean("libre_one_minute", true);
        now = tsl();
    }

    @After
    public void tearDown() {
        Pref.removeItem("libre_one_minute");
        Libre3.forgetSensor();
    }

    // ==================================================== Session ====================================================

    /**
     * A reading still arriving from the old sensor's session after a new sensor was scanned is not stored, not even
     * at the new sensor's times.
     */
    @Test
    public void onReading_oldSessionAfterNewSensor_notStored() {
        // :: Setup
        Libre3SensorState old = sensor("0RRC989AQ", now - 1010 * MINUTE_IN_MS);
        Libre3Service.Session oldSession = new Libre3Service.Session(new Libre3Readings(old));
        Libre3SensorState replacement = sensor("0RRCNEW00", now - 2000 * MINUTE_IN_MS);
        new Libre3Service.Session(new Libre3Readings(replacement));

        // :: Act
        oldSession.onReading(reading(1000, 120));

        // :: Verify
        assertThat(BgReading.getForPreciseTimestamp(old.activationMs() + 1000 * MINUTE_IN_MS, 1000)).isNull();
        assertThat(BgReading.getForPreciseTimestamp(replacement.activationMs() + 1000 * MINUTE_IN_MS, 1000)).isNull();
    }

    /** The current sensor's session stores its readings. */
    @Test
    public void onReading_currentSession_stored() {
        // :: Setup
        Libre3SensorState current = sensor("0RRC989AQ", now - 1010 * MINUTE_IN_MS);
        Libre3Service.Session session = new Libre3Service.Session(new Libre3Readings(current));

        // :: Act
        session.onReading(reading(1000, 120));

        // :: Verify
        BgReading stored = BgReading.getForPreciseTimestamp(current.activationMs() + 1000 * MINUTE_IN_MS, 1000);
        assertThat(stored.calculated_value).isEqualTo(120.0);
    }

    // ===================================================== Stall =====================================================

    /** A session that never reported a stage or a value is stalled, so the next wake-up restarts it. */
    @Test
    public void stalled_noProgress_true() {
        // :: Setup
        Libre3Service.Session session = new Libre3Service.Session(new Libre3Readings(sensor("0RRC989AQ", now)));

        // :: Act
        boolean stalled = session.stalled(tsl());

        // :: Verify
        assertThat(stalled).isTrue();
    }

    /** A stage reported eleven minutes ago is recent enough: the session is still working. */
    @Test
    public void stalled_stageElevenMinutesAgo_false() {
        // :: Setup
        Libre3Service.Session session = new Libre3Service.Session(new Libre3Readings(sensor("0RRC989AQ", now)));
        session.onStage("Connecting");

        // :: Act
        boolean stalled = session.stalled(tsl() + 11 * MINUTE_IN_MS);

        // :: Verify
        assertThat(stalled).isFalse();
    }

    /** No stage or value for thirteen minutes means the session is stuck, e.g. a retry timer that Doze held back. */
    @Test
    public void stalled_stageThirteenMinutesAgo_true() {
        // :: Setup
        Libre3Service.Session session = new Libre3Service.Session(new Libre3Readings(sensor("0RRC989AQ", now)));
        session.onStage("Reconnecting in 300 s: disconnected, reason 8");

        // :: Act
        boolean stalled = session.stalled(tsl() + 13 * MINUTE_IN_MS);

        // :: Verify
        assertThat(stalled).isTrue();
    }

    /** A reading counts as progress even when it is not stored. */
    @Test
    public void stalled_readingJustNow_false() {
        // :: Setup
        Libre3Service.Session session = new Libre3Service.Session(new Libre3Readings(sensor("0RRC989AQ", now)));
        session.onReading(reading(1, 120));

        // :: Act
        boolean stalled = session.stalled(tsl());

        // :: Verify
        assertThat(stalled).isFalse();
    }

    /** A sensor status counts as progress, as during warm-up when no glucose value is stored. */
    @Test
    public void stalled_statusJustNow_false() {
        // :: Setup
        Libre3Service.Session session = new Libre3Service.Session(new Libre3Readings(sensor("0RRC989AQ", now)));
        session.onStatus(PatchStatus.parse(new byte[PatchStatus.SIZE]));

        // :: Act
        boolean stalled = session.stalled(tsl());

        // :: Verify
        assertThat(stalled).isFalse();
    }

    /** An error alone is not progress: a session that only failed, like one without a Bluetooth adapter, is stalled. */
    @Test
    public void stalled_onlyError_true() {
        // :: Setup
        Libre3Service.Session session = new Libre3Service.Session(new Libre3Readings(sensor("0RRC989AQ", now)));
        session.onError("No Bluetooth adapter");

        // :: Act
        boolean stalled = session.stalled(tsl());

        // :: Verify
        assertThat(stalled).isTrue();
    }

    // ==================================================== helpers ====================================================

    /** Stores a running sensor as the current one, as an NFC scan does. */
    private static Libre3SensorState sensor(String serial, long activationMs) {
        Libre3SensorState state = new Libre3SensorState(serial, "CC:22:DF:B8:F9:58", new byte[]{1, 2, 3, 4});
        state.setActivationMs(activationMs);
        state.applySensorCycle(PatchInfo.parse(Hex.toBytes(PATCH_INFO)));
        Libre3.saveState(state);
        Libre3.ensureSensor(state);
        return state;
    }

    private static RealtimeReading reading(int lifeCount, int mgdl) {
        byte[] b = new byte[RealtimeReading.SIZE];
        b[0] = (byte) lifeCount;
        b[1] = (byte) (lifeCount >> 8);
        b[14] = 3;
        b[15] = (byte) mgdl;
        b[16] = (byte) (mgdl >> 8);
        return RealtimeReading.parse(b);
    }
}
