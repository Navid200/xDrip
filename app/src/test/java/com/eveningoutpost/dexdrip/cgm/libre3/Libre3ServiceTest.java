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
