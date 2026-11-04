package com.eveningoutpost.dexdrip.cgm.libre3;

import static com.eveningoutpost.dexdrip.xdrip.gs;
import static com.google.common.truth.Truth.assertThat;
import static org.robolectric.Shadows.shadowOf;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.nfc.NfcAdapter;
import android.os.Looper;
import android.widget.TextView;

import com.eveningoutpost.dexdrip.R;
import com.eveningoutpost.dexdrip.RobolectricTestWithConfig;
import com.eveningoutpost.dexdrip.utilitymodels.Pref;
import com.eveningoutpost.dexdrip.utils.DexCollectionType;
import com.eveningoutpost.dexdrip.xdrip;

import org.junit.After;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowNfcAdapter;

/**
 * Tests for {@link Libre3Activity}: the screen asks for location, which the Bluetooth scan needs, without an app
 * credential it offers to download it, once per visit, and it reads NFC scans itself while it is open.
 *
 * @author Asbjørn Aarrestad - 2026.11
 */
public class Libre3ActivityTest extends RobolectricTestWithConfig {

    @Override
    public void setUp() {
        super.setUp();
        xdrip.setContextAlways(RuntimeEnvironment.application);
        ShadowDialog.reset();
    }

    @After
    public void tearDown() {
        Pref.removeItem("use_nfc_scan");
        Pref.removeItem(DexCollectionType.DEX_COLLECTION_METHOD);
    }

    // ================================================ Offer download =================================================

    /** Opening the screen without a credential asks whether to download it now. */
    @Test
    public void onResume_noCredential_offersDownload() {
        // :: Setup
        grantLocation();

        // :: Act
        Robolectric.buildActivity(Libre3Activity.class).setup();
        shadowOf(Looper.getMainLooper()).idle();

        // :: Verify
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        assertThat(dialog).isNotNull();
        assertThat(dialog.isShowing()).isTrue();
        assertThat(shadowOf(dialog).getMessage().toString()).isEqualTo(gs(R.string.libre3_offer_download));
    }

    /** Coming back to the screen after answering does not ask again. */
    @Test
    public void onResume_again_offersOnce() {
        // :: Setup
        grantLocation();
        ActivityController<Libre3Activity> screen = Robolectric.buildActivity(Libre3Activity.class).setup();
        shadowOf(Looper.getMainLooper()).idle();
        ShadowAlertDialog.getLatestAlertDialog().dismiss();

        // :: Act
        screen.pause().resume();
        shadowOf(Looper.getMainLooper()).idle();

        // :: Verify
        assertThat(ShadowDialog.getShownDialogs()).hasSize(1);
    }

    // =================================================== Location ====================================================

    /** Without location permission the screen asks for it first, and the download offer waits for the next visit. */
    @Test
    public void onResume_noLocationPermission_asksForIt() {
        // :: Act
        Robolectric.buildActivity(Libre3Activity.class).setup();
        shadowOf(Looper.getMainLooper()).idle();

        // :: Verify
        assertThat(ShadowDialog.getShownDialogs()).hasSize(1);
        assertThat(latestMessage()).isEqualTo(gs(R.string.without_location_scan_doesnt_work));
    }

    /** With location turned off the screen asks to turn it on. */
    @Test
    public void onResume_locationOff_asksToTurnItOn() {
        // :: Setup
        grantLocation();
        setLocationEnabled(false);

        // :: Act
        Robolectric.buildActivity(Libre3Activity.class).setup();
        shadowOf(Looper.getMainLooper()).idle();

        // :: Verify
        assertThat(ShadowDialog.getShownDialogs()).hasSize(1);
        assertThat(latestMessage()).isEqualTo(gs(R.string.location_not_found_message));
    }

    // ====================================================== NFC ======================================================

    /** With NFC scanning on, the open screen reads a scan itself, so the start or takeover question is asked on it. */
    @Test
    public void onResume_nfcScanOn_readsScans() {
        // :: Setup
        grantLocation();
        enableNfcScan();

        // :: Act
        ActivityController<Libre3Activity> screen = Robolectric.buildActivity(Libre3Activity.class).setup();

        // :: Verify
        assertThat(shadowOf(NfcAdapter.getDefaultAdapter(screen.get())).isInReaderMode()).isTrue();
    }

    /** Leaving the screen stops it reading scans, so the next screen gets them. */
    @Test
    public void onPause_nfcScanOn_stopsReadingScans() {
        // :: Setup
        grantLocation();
        enableNfcScan();
        ActivityController<Libre3Activity> screen = Robolectric.buildActivity(Libre3Activity.class).setup();

        // :: Act
        screen.pause();

        // :: Verify
        assertThat(shadowOf(NfcAdapter.getDefaultAdapter(screen.get())).isInReaderMode()).isFalse();
    }

    // ==================================================== helpers ====================================================

    /** The message of the newest dialog, whichever AlertDialog class built it. */
    private static String latestMessage() {
        TextView message = ShadowDialog.getLatestDialog().findViewById(android.R.id.message);
        return message.getText().toString();
    }

    /** NFC scanning turned on with the native Libre 3 collector, on a phone with NFC switched on. */
    private static void enableNfcScan() {
        Pref.setBoolean("use_nfc_scan", true);
        DexCollectionType.setDexCollectionType(DexCollectionType.Libre3Native);
        ShadowNfcAdapter.setNfcHardwareExists(true);
        shadowOf(RuntimeEnvironment.getApplication().getPackageManager())
                .setSystemFeature(PackageManager.FEATURE_NFC, true);
        shadowOf(NfcAdapter.getDefaultAdapter(RuntimeEnvironment.getApplication())).setEnabled(true);
    }

    private static void grantLocation() {
        shadowOf(RuntimeEnvironment.getApplication()).grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        setLocationEnabled(true);
    }

    private static void setLocationEnabled(boolean enabled) {
        LocationManager locationManager = (LocationManager) RuntimeEnvironment.getApplication()
                .getSystemService(Context.LOCATION_SERVICE);
        shadowOf(locationManager).setLocationEnabled(enabled);
    }
}
