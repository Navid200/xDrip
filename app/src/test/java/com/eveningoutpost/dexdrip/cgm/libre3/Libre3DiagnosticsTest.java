package com.eveningoutpost.dexdrip.cgm.libre3;

import static com.eveningoutpost.dexdrip.models.JoH.tsl;
import static com.google.common.truth.Truth.assertThat;

import android.content.ClipboardManager;
import android.content.Context;
import android.util.Base64;

import com.eveningoutpost.dexdrip.RobolectricTestWithConfig;
import com.eveningoutpost.dexdrip.models.UserError;
import com.eveningoutpost.dexdrip.utilitymodels.Pref;
import com.eveningoutpost.dexdrip.xdrip;

import org.junit.After;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import java.util.Locale;

import lwld.libre3.Libre3SensorState;
import lwld.libre3.nfc.PatchInfo;
import lwld.libre3.util.Hex;

/**
 * Tests for {@link Libre3Diagnostics}: the report a tester pastes into a bug report says what the collector saw, and
 * never carries the sensor's secrets or the account ID.
 *
 * @author Asbjørn Aarrestad - 2026.11
 */
public class Libre3DiagnosticsTest extends RobolectricTestWithConfig {

    private static final String PATCH_INFO = "00a50001000200010060541e020401040c0130525243393839415151ff17";
    private static final byte[] PIN = {0x12, 0x34, 0x56, 0x78};
    private static final String ACCOUNT_ID = "acct-secret-1";

    private Context context;

    @Override
    public void setUp() {
        super.setUp();
        context = RuntimeEnvironment.getApplication();
        xdrip.setContextAlways(context);
        Libre3.forgetSensor();
    }

    @After
    public void tearDown() {
        Libre3.forgetSensor();
        Pref.removeItem(Libre3.PREF_ACCOUNT_ID);
    }

    // ===================================================== text ======================================================

    /** Without a stored sensor the report says so, next to the app and phone facts. */
    @Test
    public void text_noSensor_saysNone() {
        // :: Act
        String text = Libre3Diagnostics.text(context);

        // :: Verify
        assertThat(text).contains("Sensor: none");
        assertThat(text).contains("xDrip ");
        assertThat(text).contains("Location permission: no");
    }

    /** With a stored sensor the report names it and says whether it is paired. */
    @Test
    public void text_storedSensor_namesSensorFacts() {
        // :: Setup
        Libre3SensorState state = sensor();
        Libre3.saveState(state);

        // :: Act
        String text = Libre3Diagnostics.text(context);

        // :: Verify
        assertThat(text).contains(state.serial());
        assertThat(text).contains("paired key no");
    }

    /** A paired sensor's PIN and key, and the account ID, are not in the report in any common spelling. */
    @Test
    public void text_pairedSensor_holdsNoSecrets() {
        // :: Setup
        byte[] kAuth = new byte[16];
        for (int i = 0; i < kAuth.length; i++) {
            kAuth[i] = (byte) (0xA0 + i);
        }
        Libre3SensorState state = sensor();
        state.setKAuth(kAuth);
        Libre3.saveState(state);
        Pref.setString(Libre3.PREF_ACCOUNT_ID, ACCOUNT_ID);

        // :: Act
        String text = Libre3Diagnostics.text(context);

        // :: Verify
        assertThat(text).contains("paired key yes");
        for (byte[] secret : new byte[][]{PIN, kAuth}) {
            String hex = Hex.of(secret);
            assertThat(text).doesNotContain(hex.toUpperCase(Locale.ROOT));
            assertThat(text).doesNotContain(hex.toLowerCase(Locale.ROOT));
            assertThat(text).doesNotContain(Base64.encodeToString(secret, Base64.NO_WRAP));
        }
        assertThat(text).doesNotContain(ACCOUNT_ID);
    }

    /** The log part holds the collector's lines only. */
    @Test
    public void text_recentLog_onlyLibre3Lines() {
        // :: Setup
        UserError.Log.uel("Libre3Native", "paired line");
        UserError.Log.uel("OtherTag", "other line");

        // :: Act
        String text = Libre3Diagnostics.text(context);

        // :: Verify
        assertThat(text).contains("paired line");
        assertThat(text).doesNotContain("other line");
    }

    /** A log line that names the account ID, as the NFC error for a refused command does, is shown without it. */
    @Test
    public void text_logLineWithAccountId_isMasked() {
        // :: Setup
        Pref.setString(Libre3.PREF_ACCOUNT_ID, ACCOUNT_ID);
        UserError.Log.uel("Libre3Native", "Check the account ID (" + ACCOUNT_ID + ")");

        // :: Act
        String text = Libre3Diagnostics.text(context);

        // :: Verify
        assertThat(text).contains("Check the account ID (<account ID>)");
        assertThat(text).doesNotContain(ACCOUNT_ID);
    }

    // ================================================ copyToClipboard ================================================

    /** The copy button puts the report on the clipboard. */
    @Test
    public void copyToClipboard_putsTextOnClipboard() {
        // :: Act
        Libre3Diagnostics.copyToClipboard(context);

        // :: Verify
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        assertThat(clipboard.getPrimaryClip().getItemAt(0).getText().toString()).startsWith("Libre 3 diagnostics");
    }

    // ==================================================== helpers ====================================================

    private static Libre3SensorState sensor() {
        Libre3SensorState state = new Libre3SensorState("0RRC989AQ", "CC:22:DF:B8:F9:58", PIN);
        state.setActivationMs(tsl());
        state.applySensorCycle(PatchInfo.parse(Hex.toBytes(PATCH_INFO)));
        return state;
    }
}
