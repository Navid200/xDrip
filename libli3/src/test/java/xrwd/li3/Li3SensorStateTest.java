package xrwd.li3;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import xrwd.li3.data.Lifecycle;
import xrwd.li3.nfc.PatchInfo;
import xrwd.li3.pairing.ReceiverId;
import xrwd.li3.util.Hex;

/**
 * Tests for {@link Li3SensorState}: what is kept about one sensor between connections. Vectors are from
 * RoundWhiteDiscKit 7dd6396 {@code *SensorStateLoaderTests.swift}; the fields xDrip adds beyond it are checked in
 * the round trip.
 *
 * @author Asbjørn Aarrestad - 2026.10
 */
@RunWith(RobolectricTestRunner.class)
public class Li3SensorStateTest {

    private static final String SERIAL = "0RRC989AQ";
    private static final String ADDRESS = "CC:22:DF:B8:F9:58";
    private static final byte[] PIN = {0x32, 0x25, (byte) 0xec, 0x72};
    private static final String KAUTH_HEX = "00112233445566778899aabbccddeeff";
    private static final String ACTIVE = "00a50001000200010060541e020401040c04305252433938394151c6ca";
    private static final String SENSOR_KEY_HEX = "04" + "11111111111111111111111111111111111111111111111111111111111111111111111111111111"
            + "111111111111111111111111111111111111111111111111";

    // =================================================== fromJson ====================================================

    /** *SensorStateLoaderTests.swift:5-20: a hex PIN and a little-endian receiver ID are read back as stored. */
    @Test
    public void fromJson_hexPin_readsFields() {
        // :: Setup
        String json = "{\"serialNumber\":\"0RRC989AQ\",\"bleAddress\":\"CC:22:DF:B8:F9:58\",\"blePIN\":\"3225ec72\","
                + "\"receiverID\":\"78830d6f\",\"lastGlucoseLifeCount\":1073,\"lastGlucoseMgDL\":151}";

        // :: Act
        Li3SensorState state = Li3SensorState.fromJson(json);

        // :: Verify
        assertThat(state.serial()).isEqualTo(SERIAL);
        assertThat(state.bleAddress()).isEqualTo(ADDRESS);
        assertThat(Hex.of(state.blePin())).isEqualTo("3225ec72");
        assertThat(state.receiverId().value()).isEqualTo(0x6f0d8378L);
        assertThat(state.receiverId().littleEndianHex()).isEqualTo("78830d6f");
        assertThat(state.lastLifeCount()).isEqualTo(1073);
        assertThat(state.lastMgdl()).isEqualTo(151);
    }

    /** *SensorStateLoaderTests.swift:29-35: a PIN that is not 4 bytes is refused. */
    @Test
    public void fromJson_wrongPinSize_throws() {
        // :: Setup
        String json = "{\"serialNumber\":\"0RRC989AQ\",\"bleAddress\":\"CC:22:DF:B8:F9:58\",\"blePIN\":\"3225ec\"}";

        // :: Act
        ThrowingRunnable load = () -> Li3SensorState.fromJson(json);

        // :: Verify
        assertThrows(IllegalArgumentException.class, load);
    }

    /** Text that is not JSON is refused with the same exception as a bad field, not a JSON-library one. */
    @Test
    public void fromJson_notJson_throws() {
        // :: Act
        ThrowingRunnable load = () -> Li3SensorState.fromJson("not json");

        // :: Verify
        assertThrows(IllegalArgumentException.class, load);
    }

    /** *SensorStateLoaderTests.swift:88-97: without a stored cycle the default warm-up applies, and no wear end. */
    @Test
    public void lifecycle_noStoredCycle_usesDefaultWarmup() {
        // :: Setup
        Li3SensorState state = new Li3SensorState(SERIAL, ADDRESS, PIN);

        // :: Act
        Lifecycle lifecycle = state.lifecycle();

        // :: Verify
        assertThat(state.warmupMinutes()).isNull();
        assertThat(state.wearMinutes()).isNull();
        assertThat(lifecycle.usable(Lifecycle.DEFAULT_WARMUP_MINUTES - 1)).isFalse();
        assertThat(lifecycle.usable(Lifecycle.DEFAULT_WARMUP_MINUTES)).isTrue();
        assertThat(lifecycle.usable(1_000_000)).isTrue();
    }

    // ==================================================== toJson =====================================================

    /** *SensorStateLoaderTests.swift:37-56: every field, including the ones xDrip adds, survives a round trip. */
    @Test
    public void toJson_allFields_roundTrips() {
        // :: Setup
        Li3SensorState state = new Li3SensorState(SERIAL, ADDRESS, PIN);
        state.setReceiverId(new ReceiverId(0x6f0d8378L));
        state.setFold(ReceiverId.Fold.MULTI_SENSOR_APP);
        state.setActivationMs(1_761_800_000_000L);
        state.applySensorCycle(PatchInfo.parse(Hex.toBytes(ACTIVE)));
        state.setIdentityLabel("a");
        state.setKAuth(Hex.toBytes(KAUTH_HEX));
        state.updateLastGlucose(1073, 151);
        state.setRejectedLifeCounts(new HashSet<>(Arrays.asList(1070, 1071)));
        state.setSensorKey(Hex.toBytes(SENSOR_KEY_HEX));

        // :: Act
        String json = state.toJson();
        Li3SensorState restored = Li3SensorState.fromJson(json);

        // :: Verify
        assertThat(json).contains("\"blePIN\":\"3225ec72\"");
        assertThat(json).contains("\"receiverID\":\"78830d6f\"");
        assertThat(json).contains("\"lastGlucoseLifeCount\":1073");
        assertThat(json).contains("\"lastGlucoseMgDL\":151");
        assertThat(restored.serial()).isEqualTo(SERIAL);
        assertThat(restored.bleAddress()).isEqualTo(ADDRESS);
        assertThat(restored.blePin()).isEqualTo(PIN);
        assertThat(restored.receiverId()).isEqualTo(new ReceiverId(0x6f0d8378L));
        assertThat(restored.fold()).isEqualTo(ReceiverId.Fold.MULTI_SENSOR_APP);
        assertThat(restored.activationMs()).isEqualTo(1_761_800_000_000L);
        assertThat(restored.warmupMinutes()).isEqualTo(60);
        assertThat(restored.wearMinutes()).isEqualTo(21600);
        assertThat(restored.productType()).isEqualTo(4);
        assertThat(restored.generation()).isEqualTo(1);
        assertThat(restored.region()).isEqualTo(PatchInfo.Region.US);
        assertThat(restored.identityLabel()).isEqualTo("a");
        assertThat(Hex.of(restored.kAuth())).isEqualTo(KAUTH_HEX);
        assertThat(restored.lastLifeCount()).isEqualTo(1073);
        assertThat(restored.lastMgdl()).isEqualTo(151);
        assertThat(restored.rejectedLifeCounts()).containsExactly(1070, 1071);
        assertThat(Hex.of(restored.sensorKey())).isEqualTo(SENSOR_KEY_HEX);
    }

    /** A freshly activated sensor with only the required fields round-trips with the rest left unknown. */
    @Test
    public void toJson_requiredOnly_roundTripsWithUnknowns() {
        // :: Setup
        Li3SensorState state = new Li3SensorState(SERIAL, ADDRESS, PIN);

        // :: Act
        Li3SensorState restored = Li3SensorState.fromJson(state.toJson());

        // :: Verify
        assertThat(restored.blePin()).isEqualTo(PIN);
        assertThat(restored.receiverId()).isNull();
        assertThat(restored.fold()).isNull();
        assertThat(restored.activationMs()).isEqualTo(0L);
        assertThat(restored.region()).isNull();
        assertThat(restored.kAuth()).isNull();
        assertThat(restored.lastLifeCount()).isNull();
        assertThat(restored.rejectedLifeCounts()).isEmpty();
        assertThat(restored.sensorKey()).isNull();
    }

    // ================================================= Last glucose ==================================================

    /** *SensorStateLoaderTests.swift:58-72: the last reading is updated and nothing else changes. */
    @Test
    public void updateLastGlucose_keepsIdentity() {
        // :: Setup
        Li3SensorState state = new Li3SensorState(SERIAL, ADDRESS, PIN);

        // :: Act
        state.updateLastGlucose(1080, 149);

        // :: Verify
        assertThat(state.serial()).isEqualTo(SERIAL);
        assertThat(state.blePin()).isEqualTo(PIN);
        assertThat(state.lastLifeCount()).isEqualTo(1080);
        assertThat(state.lastMgdl()).isEqualTo(149);
    }

    // ============================================= Rejected life counts ==============================================

    /** A refused life count more than a day older than the newest one is dropped, so the set stays small. */
    @Test
    public void addRejectedLifeCount_dayOlderThanNewest_dropped() {
        // :: Setup
        Li3SensorState state = new Li3SensorState(SERIAL, ADDRESS, PIN);

        // :: Act
        state.addRejectedLifeCount(1000);
        state.addRejectedLifeCount(1001);
        state.addRejectedLifeCount(2441);

        // :: Verify
        assertThat(state.rejectedLifeCounts()).containsExactly(1001, 2441);
    }

    /** A refused life count older than the newest one, but within a day, is kept. */
    @Test
    public void addRejectedLifeCount_olderWithinDay_kept() {
        // :: Setup
        Li3SensorState state = new Li3SensorState(SERIAL, ADDRESS, PIN);
        state.addRejectedLifeCount(2441);

        // :: Act
        state.addRejectedLifeCount(1001);

        // :: Verify
        assertThat(state.rejectedLifeCounts()).containsExactly(1001, 2441);
    }

    /** The refused life counts handed out are a copy, so a reader is not hit by a later add on another thread. */
    @Test
    public void rejectedLifeCounts_laterAdd_notInEarlierCopy() {
        // :: Setup
        Li3SensorState state = new Li3SensorState(SERIAL, ADDRESS, PIN);
        state.addRejectedLifeCount(1001);
        Set<Integer> before = state.rejectedLifeCounts();

        // :: Act
        state.addRejectedLifeCount(1002);

        // :: Verify
        assertThat(before).containsExactly(1001);
    }

    // ================================================== Sensor key ===================================================

    /** A sensor key that is not an uncompressed 65-byte point is refused, so a broken one is never pinned. */
    @Test
    public void setSensorKey_wrongSize_throws() {
        // :: Setup
        Li3SensorState state = new Li3SensorState(SERIAL, ADDRESS, PIN);

        // :: Act
        ThrowingRunnable set = () -> state.setSensorKey(new byte[64]);

        // :: Verify
        assertThrows(IllegalArgumentException.class, set);
        assertThat(state.sensorKey()).isNull();
    }

    // ================================================= Sensor cycle ==================================================

    /** *SensorStateLoaderTests.swift:74-86: the cycle read over NFC is kept, so a reconnect knows it. */
    @Test
    public void applySensorCycle_persistsAndRestores() {
        // :: Setup
        Li3SensorState state = new Li3SensorState(SERIAL, ADDRESS, PIN);

        // :: Act
        state.applySensorCycle(PatchInfo.parse(Hex.toBytes(ACTIVE)));
        Li3SensorState restored = Li3SensorState.fromJson(state.toJson());

        // :: Verify
        assertThat(restored.warmupMinutes()).isEqualTo(60);
        assertThat(restored.wearMinutes()).isEqualTo(21600);
        assertThat(restored.lifecycle().usable(21599)).isTrue();
        assertThat(restored.lifecycle().usable(21600)).isFalse();
    }

    // =================================================== toString ====================================================

    /** The PIN and kAuth never reach a log line through {@code toString()}. */
    @Test
    public void toString_omitsSecrets() {
        // :: Setup
        Li3SensorState state = new Li3SensorState(SERIAL, ADDRESS, PIN);
        state.setKAuth(Hex.toBytes(KAUTH_HEX));

        // :: Act
        String text = state.toString();

        // :: Verify
        assertThat(text).contains(SERIAL);
        assertThat(text.toLowerCase()).doesNotContain("3225ec72");
        assertThat(text.toLowerCase()).doesNotContain(KAUTH_HEX);
    }
}
