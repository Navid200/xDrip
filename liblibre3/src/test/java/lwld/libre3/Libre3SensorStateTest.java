package lwld.libre3;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;
import java.util.HashSet;

import lwld.libre3.data.Lifecycle;
import lwld.libre3.nfc.PatchInfo;
import lwld.libre3.pairing.ReceiverId;
import lwld.libre3.util.Hex;

/**
 * Tests for {@link Libre3SensorState}: what is kept about one sensor between connections. Vectors are from
 * RoundWhiteDiscKit 7dd6396 {@code Libre3SensorStateLoaderTests.swift}; the fields xDrip adds beyond it are checked in
 * the round trip.
 *
 * @author Asbjørn Aarrestad - 2026.10
 */
@RunWith(RobolectricTestRunner.class)
public class Libre3SensorStateTest {

    private static final String SERIAL = "0RRC989AQ";
    private static final String ADDRESS = "CC:22:DF:B8:F9:58";
    private static final byte[] PIN = {0x32, 0x25, (byte) 0xec, 0x72};
    private static final String KAUTH_HEX = "00112233445566778899aabbccddeeff";
    private static final String ACTIVE = "00a50001000200010060541e020401040c04305252433938394151c6ca";

    // =================================================== fromJson ====================================================

    /** Libre3SensorStateLoaderTests.swift:5-20: a hex PIN and a little-endian receiver ID are read back as stored. */
    @Test
    public void fromJson_hexPin_readsFields() {
        // :: Setup
        String json = "{\"serialNumber\":\"0RRC989AQ\",\"bleAddress\":\"CC:22:DF:B8:F9:58\",\"blePIN\":\"3225ec72\","
                + "\"receiverID\":\"78830d6f\",\"lastGlucoseLifeCount\":1073,\"lastGlucoseMgDL\":151}";

        // :: Act
        Libre3SensorState state = Libre3SensorState.fromJson(json);

        // :: Verify
        assertThat(state.serial()).isEqualTo(SERIAL);
        assertThat(state.bleAddress()).isEqualTo(ADDRESS);
        assertThat(Hex.of(state.blePin())).isEqualTo("3225ec72");
        assertThat(state.receiverId().value()).isEqualTo(0x6f0d8378L);
        assertThat(state.receiverId().littleEndianHex()).isEqualTo("78830d6f");
        assertThat(state.lastLifeCount()).isEqualTo(1073);
        assertThat(state.lastMgdl()).isEqualTo(151);
    }

    /** Libre3SensorStateLoaderTests.swift:29-35: a PIN that is not 4 bytes is refused. */
    @Test
    public void fromJson_wrongPinSize_throws() {
        // :: Setup
        String json = "{\"serialNumber\":\"0RRC989AQ\",\"bleAddress\":\"CC:22:DF:B8:F9:58\",\"blePIN\":\"3225ec\"}";

        // :: Act
        ThrowingRunnable load = () -> Libre3SensorState.fromJson(json);

        // :: Verify
        assertThrows(IllegalArgumentException.class, load);
    }

    /** Text that is not JSON is refused with the same exception as a bad field, not a JSON-library one. */
    @Test
    public void fromJson_notJson_throws() {
        // :: Act
        ThrowingRunnable load = () -> Libre3SensorState.fromJson("not json");

        // :: Verify
        assertThrows(IllegalArgumentException.class, load);
    }

    /** Libre3SensorStateLoaderTests.swift:88-97: without a stored cycle the default warm-up applies, and no wear end. */
    @Test
    public void lifecycle_noStoredCycle_usesDefaultWarmup() {
        // :: Setup
        Libre3SensorState state = new Libre3SensorState(SERIAL, ADDRESS, PIN);

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

    /** Libre3SensorStateLoaderTests.swift:37-56: every field, including the ones xDrip adds, survives a round trip. */
    @Test
    public void toJson_allFields_roundTrips() {
        // :: Setup
        Libre3SensorState state = new Libre3SensorState(SERIAL, ADDRESS, PIN);
        state.setReceiverId(new ReceiverId(0x6f0d8378L));
        state.setFold(ReceiverId.Fold.LIBRE_BY_ABBOTT);
        state.setActivationMs(1_761_800_000_000L);
        state.applySensorCycle(PatchInfo.parse(Hex.toBytes(ACTIVE)));
        state.setIdentityLabel("a");
        state.setKAuth(Hex.toBytes(KAUTH_HEX));
        state.updateLastGlucose(1073, 151);
        state.setRejectedLifeCounts(new HashSet<>(Arrays.asList(1070, 1071)));

        // :: Act
        String json = state.toJson();
        Libre3SensorState restored = Libre3SensorState.fromJson(json);

        // :: Verify
        assertThat(json).contains("\"blePIN\":\"3225ec72\"");
        assertThat(json).contains("\"receiverID\":\"78830d6f\"");
        assertThat(json).contains("\"lastGlucoseLifeCount\":1073");
        assertThat(json).contains("\"lastGlucoseMgDL\":151");
        assertThat(restored.serial()).isEqualTo(SERIAL);
        assertThat(restored.bleAddress()).isEqualTo(ADDRESS);
        assertThat(restored.blePin()).isEqualTo(PIN);
        assertThat(restored.receiverId()).isEqualTo(new ReceiverId(0x6f0d8378L));
        assertThat(restored.fold()).isEqualTo(ReceiverId.Fold.LIBRE_BY_ABBOTT);
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
    }

    /** A freshly activated sensor with only the required fields round-trips with the rest left unknown. */
    @Test
    public void toJson_requiredOnly_roundTripsWithUnknowns() {
        // :: Setup
        Libre3SensorState state = new Libre3SensorState(SERIAL, ADDRESS, PIN);

        // :: Act
        Libre3SensorState restored = Libre3SensorState.fromJson(state.toJson());

        // :: Verify
        assertThat(restored.blePin()).isEqualTo(PIN);
        assertThat(restored.receiverId()).isNull();
        assertThat(restored.fold()).isNull();
        assertThat(restored.activationMs()).isEqualTo(0L);
        assertThat(restored.region()).isNull();
        assertThat(restored.kAuth()).isNull();
        assertThat(restored.lastLifeCount()).isNull();
        assertThat(restored.rejectedLifeCounts()).isEmpty();
    }

    // ================================================= Last glucose ==================================================

    /** Libre3SensorStateLoaderTests.swift:58-72: the last reading is updated and nothing else changes. */
    @Test
    public void updateLastGlucose_keepsIdentity() {
        // :: Setup
        Libre3SensorState state = new Libre3SensorState(SERIAL, ADDRESS, PIN);

        // :: Act
        state.updateLastGlucose(1080, 149);

        // :: Verify
        assertThat(state.serial()).isEqualTo(SERIAL);
        assertThat(state.blePin()).isEqualTo(PIN);
        assertThat(state.lastLifeCount()).isEqualTo(1080);
        assertThat(state.lastMgdl()).isEqualTo(149);
    }

    // ================================================= Sensor cycle ==================================================

    /** Libre3SensorStateLoaderTests.swift:74-86: the cycle read over NFC is kept, so a reconnect knows it. */
    @Test
    public void applySensorCycle_persistsAndRestores() {
        // :: Setup
        Libre3SensorState state = new Libre3SensorState(SERIAL, ADDRESS, PIN);

        // :: Act
        state.applySensorCycle(PatchInfo.parse(Hex.toBytes(ACTIVE)));
        Libre3SensorState restored = Libre3SensorState.fromJson(state.toJson());

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
        Libre3SensorState state = new Libre3SensorState(SERIAL, ADDRESS, PIN);
        state.setKAuth(Hex.toBytes(KAUTH_HEX));

        // :: Act
        String text = state.toString();

        // :: Verify
        assertThat(text).contains(SERIAL);
        assertThat(text.toLowerCase()).doesNotContain("3225ec72");
        assertThat(text.toLowerCase()).doesNotContain(KAUTH_HEX);
    }
}
