package lwld.libre3.ble;

import static com.google.common.truth.Truth.assertThat;
import static org.robolectric.Shadows.shadowOf;

import android.Manifest;
import android.app.Application;

import org.junit.After;
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
 * Tests for the scan in {@link Libre3Ble}: Android hands an app without location permission no scan results and no
 * error, so the collector has to say why it cannot find the sensor.
 *
 * @author Asbjørn Aarrestad - 2026.11
 */
@RunWith(RobolectricTestRunner.class)
public class Libre3BleScanTest {

    private final Recorder recorder = new Recorder();
    private Libre3Ble ble;

    @After
    public void tearDown() {
        if (ble != null) {
            ble.stop();
        }
    }

    // ===================================================== scan ======================================================

    /** Without location permission the scan is not started; the stage and the error name the permission. */
    @Test
    public void start_noLocationPermission_reportsPermission() {
        // :: Setup
        ble = scanningBle();

        // :: Act
        ble.start();

        // :: Verify
        assertThat(recorder.stages).containsExactly("Reconnecting in 5 s: no location permission");
        assertThat(recorder.errors).containsExactly("Location permission is needed to find the sensor");
    }

    /** With fine location the scan starts. */
    @Test
    public void start_fineLocation_scans() {
        // :: Setup
        grant(Manifest.permission.ACCESS_FINE_LOCATION);
        ble = scanningBle();

        // :: Act
        ble.start();

        // :: Verify
        assertThat(recorder.stages.get(0)).startsWith("Scanning for");
        assertThat(recorder.errors).isEmpty();
    }

    /** Coarse location is enough for an app that targets Android 8. */
    @Test
    public void start_coarseLocation_scans() {
        // :: Setup
        grant(Manifest.permission.ACCESS_COARSE_LOCATION);
        ble = scanningBle();

        // :: Act
        ble.start();

        // :: Verify
        assertThat(recorder.stages.get(0)).startsWith("Scanning for");
        assertThat(recorder.errors).isEmpty();
    }

    // ==================================================== helpers ====================================================

    /** A sensor without a usable Bluetooth address, so the first attempt is a scan by name. */
    private Libre3Ble scanningBle() {
        Libre3SensorState state = new Libre3SensorState("0RRC989AQ", "", new byte[]{1, 2, 3, 4});
        return new Libre3Ble(RuntimeEnvironment.getApplication(), state, Collections.emptyList(), recorder);
    }

    private static void grant(String permission) {
        Application application = RuntimeEnvironment.getApplication();
        shadowOf(application).grantPermissions(permission);
    }

    /** Keeps the stages and errors the collector would show. */
    private static final class Recorder implements Libre3Ble.Listener {
        final List<String> stages = new ArrayList<>();
        final List<String> errors = new ArrayList<>();

        @Override
        public void onStage(String stage) {
            stages.add(stage);
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
            errors.add(message);
        }
    }
}
