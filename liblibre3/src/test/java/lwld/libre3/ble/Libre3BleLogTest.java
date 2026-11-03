package lwld.libre3.ble;

import static com.google.common.truth.Truth.assertThat;

import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lwld.libre3.Libre3SensorState;
import lwld.libre3.data.HistoricalPage;
import lwld.libre3.data.PatchStatus;
import lwld.libre3.data.RealtimeReading;

/**
 * Tests for the Bluetooth log of {@link Libre3Ble}: it goes to the listener, redacted, so the collector can keep it
 * where a tester without adb can send it.
 *
 * @author Asbjørn Aarrestad - 2026.11
 */
@RunWith(RobolectricTestRunner.class)
public class Libre3BleLogTest {

    private final Recorder recorder = new Recorder();
    private Libre3Ble ble;

    @Before
    public void setUp() {
        Libre3SensorState state = new Libre3SensorState("0RRC989AQ", "", new byte[]{1, 2, 3, 4});
        ble = new Libre3Ble(RuntimeEnvironment.getApplication(), state, Collections.emptyList(), recorder);
    }

    @After
    public void tearDown() {
        ble.stop();
    }

    // ====================================================== log ======================================================

    /** A line with nothing to hide reaches the listener as it is, with its priority. */
    @Test
    public void log_plainLine_reachesListener() {
        // :: Act
        ble.log(Log.DEBUG, "gatt.connect()");

        // :: Verify
        assertThat(recorder.logs).containsExactly("3 gatt.connect()");
    }

    /** A value written to a characteristic never reaches the listener. */
    @Test
    public void log_setValueLine_reachesListenerRedacted() {
        // :: Act
        ble.log(Log.DEBUG, "characteristic.setValue(0x0102)");

        // :: Verify
        assertThat(recorder.logs).containsExactly("3 characteristic.setValue(redacted)");
    }

    /** Debug lines are logged too: they show each Bluetooth step, which is where a pairing failure is found. */
    @Test
    public void minLogPriority_isDebug() {
        // :: Act
        int priority = ble.getMinLogPriority();

        // :: Verify
        assertThat(priority).isEqualTo(Log.DEBUG);
    }

    // =================================================== reasonText ===================================================

    /** A connect failure names its reason in words, so a report says "timeout" and not only "10". */
    @Test
    public void reasonText_timeout_namesIt() {
        // :: Act
        String text = Libre3Ble.reasonText(10);

        // :: Verify
        assertThat(text).isEqualTo("reason 10 (timeout)");
    }

    /** A link loss, the usual cause when the phone goes out of range, is named. */
    @Test
    public void reasonText_linkLoss_namesIt() {
        // :: Act
        String text = Libre3Ble.reasonText(3);

        // :: Verify
        assertThat(text).isEqualTo("reason 3 (link loss)");
    }

    /** A code the library does not name keeps only its number. */
    @Test
    public void reasonText_unnamedCode_numberOnly() {
        // :: Act
        String text = Libre3Ble.reasonText(42);

        // :: Verify
        assertThat(text).isEqualTo("reason 42");
    }

    // ==================================================== helpers ====================================================

    /** Keeps the log lines the collector would store. */
    private static final class Recorder implements Libre3Ble.Listener {
        final List<String> logs = new ArrayList<>();

        @Override
        public void onLog(int priority, String message) {
            logs.add(priority + " " + message);
        }

        @Override
        public void onStage(String stage) {
        }

        @Override
        public void onPaired(Libre3SensorState state) {
        }

        @Override
        public void onReading(RealtimeReading reading) {
        }

        @Override
        public void onStatus(PatchStatus status) {
        }

        @Override
        public void onHistory(List<HistoricalPage.Entry> entries) {
        }

        @Override
        public void onError(String message) {
        }
    }
}
