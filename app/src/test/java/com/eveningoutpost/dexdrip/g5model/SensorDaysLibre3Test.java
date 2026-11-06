package com.eveningoutpost.dexdrip.g5model;

import static com.eveningoutpost.dexdrip.utilitymodels.Constants.DAY_IN_MS;
import static com.eveningoutpost.dexdrip.utilitymodels.Constants.MINUTE_IN_MS;
import static com.google.common.truth.Truth.assertThat;

import com.eveningoutpost.dexdrip.RobolectricTestWithConfig;
import com.eveningoutpost.dexdrip.cgm.libre3.Libre3;
import com.eveningoutpost.dexdrip.utils.DexCollectionType;
import com.eveningoutpost.dexdrip.xdrip;

import org.junit.After;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import xrwd.libre3.Libre3SensorState;
import xrwd.libre3.nfc.PatchInfo;
import xrwd.libre3.util.Hex;

/**
 * Tests for {@link SensorDays} with the Libre 3 collector: the start and wear period come from what the sensor itself
 * reported at activation, not from the xDrip sensor record or a Libre 2 default.
 *
 * @author Asbjørn Aarrestad - 2026.10
 */
public class SensorDaysLibre3Test extends RobolectricTestWithConfig {

    private static final byte[] PIN = {0x32, 0x25, (byte) 0xec, 0x72};
    private static final String ACTIVE = "00a50001000200010060541e020401040c04305252433938394151c6ca";

    @Override
    public void setUp() {
        super.setUp();
        xdrip.setContextAlways(RuntimeEnvironment.application);
        clean();
    }

    @After
    public void tearDown() {
        clean();
    }

    // ===================================================== start =====================================================

    /** The start is the activation time stored for the sensor. */
    @Test
    public void getStart_storedSensor_isActivationTime() {
        // :: Setup
        Libre3.saveState(state(1_800_000_000_000L));

        // :: Act
        long start = SensorDays.get(DexCollectionType.Libre3Native, "").getStart();

        // :: Verify
        assertThat(start).isEqualTo(1_800_000_000_000L);
    }

    // ==================================================== period =====================================================

    /** The period is the wear time the sensor reported, and the warm-up is its own. */
    @Test
    public void getPeriod_storedSensor_isWearMinutes() {
        // :: Setup
        Libre3.saveState(state(1_800_000_000_000L));

        // :: Act
        SensorDays days = SensorDays.get(DexCollectionType.Libre3Native, "");

        // :: Verify
        assertThat(days.getPeriod()).isEqualTo(21600 * MINUTE_IN_MS);
        assertThat(days.getWarmupMs()).isEqualTo(60 * MINUTE_IN_MS);
    }

    /** Before any sensor is stored, the period is the 15 days a Libre 3 lasts. */
    @Test
    public void getPeriod_noSensor_fifteenDays() {
        // :: Act
        SensorDays days = SensorDays.get(DexCollectionType.Libre3Native, "");

        // :: Verify
        assertThat(days.getPeriod()).isEqualTo(15 * DAY_IN_MS);
    }

    private static Libre3SensorState state(long activationMs) {
        Libre3SensorState state = new Libre3SensorState("0RRC989AQ", "CC:22:DF:B8:F9:58", PIN);
        state.setActivationMs(activationMs);
        state.applySensorCycle(PatchInfo.parse(Hex.toBytes(ACTIVE)));
        return state;
    }

    private static void clean() {
        Libre3.forgetSensor();
        SensorDays.clearCache();
    }
}
