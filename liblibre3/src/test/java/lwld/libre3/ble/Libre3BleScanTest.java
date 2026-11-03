package lwld.libre3.ble;

import static com.google.common.truth.Truth.assertThat;
import static org.robolectric.Shadows.shadowOf;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.location.LocationManager;

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

    /** Without location permission the collector is told, so it can ask the user. */
    @Test
    public void start_noLocationPermission_asksForLocation() {
        // :: Setup
        ble = scanningBle();

        // :: Act
        ble.start();

        // :: Verify
        assertThat(recorder.locationNeeded).isEqualTo(1);
    }

    /** With location turned off Android finds nothing either, so the scan is not started and the error says why. */
    @Test
    public void start_locationOff_reportsLocationOff() {
        // :: Setup
        grant(Manifest.permission.ACCESS_FINE_LOCATION);
        setLocationEnabled(false);
        ble = scanningBle();

        // :: Act
        ble.start();

        // :: Verify
        assertThat(recorder.stages).containsExactly("Reconnecting in 5 s: location is off");
        assertThat(recorder.errors).containsExactly("Location is off; Android finds no Bluetooth sensor without it");
        assertThat(recorder.locationNeeded).isEqualTo(1);
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
        assertThat(recorder.locationNeeded).isEqualTo(0);
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

    // =================================================== restart =====================================================

    /** A failed attempt counts, so a restarted connection can go on from it. */
    @Test
    public void reconnectAttempt_afterFailedAttempt_one() {
        // :: Setup
        ble = scanningBle();
        ble.start();

        // :: Act
        int attempt = ble.reconnectAttempt();

        // :: Verify
        assertThat(attempt).isEqualTo(1);
    }

    /** Started from a later attempt, it waits as long as that attempt would, not as long as the first. */
    @Test
    public void start_fromFourthAttempt_waitsAsFifth() {
        // :: Setup
        ble = scanningBle();

        // :: Act
        ble.start(4);

        // :: Verify
        assertThat(recorder.stages).containsExactly("Reconnecting in 25 s: no location permission");
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

    private static void setLocationEnabled(boolean enabled) {
        LocationManager locationManager = (LocationManager) RuntimeEnvironment.getApplication()
                .getSystemService(Context.LOCATION_SERVICE);
        shadowOf(locationManager).setLocationEnabled(enabled);
    }

    /** Keeps the stages and errors the collector would show. */
    private static final class Recorder implements Libre3Ble.Listener {
        final List<String> stages = new ArrayList<>();
        final List<String> errors = new ArrayList<>();
        int locationNeeded;

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

        @Override
        public void onLocationNeeded() {
            locationNeeded++;
        }
    }
}
