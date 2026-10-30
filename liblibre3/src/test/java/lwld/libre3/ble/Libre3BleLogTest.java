package lwld.libre3.ble;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/**
 * Tests for {@link Libre3Ble#redact}: the Nordic library logs every value it writes and receives, and on the security
 * characteristics those values are the app certificate, the ephemeral key and the challenge. The message shapes are
 * the ones Nordic BLE 2.6.1 {@code BleManagerHandler} builds.
 *
 * @author Asbjørn Aarrestad - 2026.10
 */
public class Libre3BleLogTest {

    private static final String SECRET = "(0x) 00-00-03-00-01-02-03-04-05-06-07-08-09-0A-0B-0C-0D-0E-0F-10";

    // ==================================================== redact =====================================================

    /** A certificate write keeps the characteristic but loses the bytes. */
    @Test
    public void redact_securityWrite_dropsValue() {
        // :: Act
        String line = Libre3Ble.redact("Data written to " + Gatt.SEC_CERT_DATA + ", value: " + SECRET);

        // :: Verify
        assertThat(line).contains(Gatt.SEC_CERT_DATA.toString());
        assertThat(line).doesNotContain("03-00-01-02");
    }

    /** A challenge notify keeps the characteristic but loses the bytes. */
    @Test
    public void redact_securityNotify_dropsValue() {
        // :: Act
        String line = Libre3Ble.redact("Notification received from " + Gatt.SEC_CHALLENGE_DATA + ", value: " + SECRET);

        // :: Verify
        assertThat(line).contains(Gatt.SEC_CHALLENGE_DATA.toString());
        assertThat(line).doesNotContain("03-00-01-02");
    }

    /** The low-level write line also carries the value, after the characteristic. */
    @Test
    public void redact_gattWrite_dropsValue() {
        // :: Act
        String line = Libre3Ble.redact("gatt.writeCharacteristic(" + Gatt.SEC_CERT_DATA
                + ", value=0x0000030001020304, WRITE REQUEST)");

        // :: Verify
        assertThat(line).doesNotContain("0x0000030001020304");
    }

    /** Nordic logs the value it is about to write without naming the characteristic, so it is never shown. */
    @Test
    public void redact_setValue_dropsValue() {
        // :: Act
        String line = Libre3Ble.redact("characteristic.setValue(0x0000030001020304)");

        // :: Verify
        assertThat(line).doesNotContain("0x0000030001020304");
    }

    /** Glucose frames are encrypted on the wire, so their log line is kept whole for debugging. */
    @Test
    public void redact_glucoseNotify_unchanged() {
        // :: Setup
        String message = "Notification received from " + Gatt.GLUCOSE_DATA + ", value: " + SECRET;

        // :: Act
        String line = Libre3Ble.redact(message);

        // :: Verify
        assertThat(line).isEqualTo(message);
    }
}
