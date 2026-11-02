package com.eveningoutpost.dexdrip.cgm.libre3;

import static com.eveningoutpost.dexdrip.xdrip.gs;
import static com.google.common.truth.Truth.assertThat;
import static org.robolectric.Shadows.shadowOf;

import android.app.AlertDialog;
import android.os.Looper;

import com.eveningoutpost.dexdrip.R;
import com.eveningoutpost.dexdrip.RobolectricTestWithConfig;
import com.eveningoutpost.dexdrip.xdrip;

import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowDialog;

/**
 * Tests for {@link Libre3Activity}: without an app credential the screen offers to download it, once per visit.
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

    // ================================================ Offer download =================================================

    /** Opening the screen without a credential asks whether to download it now. */
    @Test
    public void onResume_noCredential_offersDownload() {
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
        ActivityController<Libre3Activity> screen = Robolectric.buildActivity(Libre3Activity.class).setup();
        shadowOf(Looper.getMainLooper()).idle();
        ShadowAlertDialog.getLatestAlertDialog().dismiss();

        // :: Act
        screen.pause().resume();
        shadowOf(Looper.getMainLooper()).idle();

        // :: Verify
        assertThat(ShadowDialog.getShownDialogs()).hasSize(1);
    }
}
