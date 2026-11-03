package com.eveningoutpost.dexdrip.cgm.libre3;

import static com.eveningoutpost.dexdrip.models.JoH.tsl;
import static com.eveningoutpost.dexdrip.xdrip.gs;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.XmlResourceParser;
import android.nfc.tech.NfcV;
import android.os.Looper;

import com.activeandroid.query.Select;
import com.eveningoutpost.dexdrip.NFCReaderX;
import com.eveningoutpost.dexdrip.R;
import com.eveningoutpost.dexdrip.RobolectricTestWithConfig;
import com.eveningoutpost.dexdrip.models.LibreOOPAlgorithm.SensorType;
import com.eveningoutpost.dexdrip.models.UserError;
import com.eveningoutpost.dexdrip.utilitymodels.Pref;
import com.eveningoutpost.dexdrip.utils.DexCollectionType;
import com.eveningoutpost.dexdrip.xdrip;

import org.junit.After;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowToast;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lwld.libre3.Libre3SensorState;
import lwld.libre3.nfc.NfcCommand;
import lwld.libre3.nfc.PatchInfo;
import lwld.libre3.pairing.ReceiverId;
import lwld.libre3.util.Hex;

/**
 * Tests for {@link Libre3Nfc}: which scans it takes from NFCReaderX, what it decides to send for a fresh or a running
 * sensor, the confirmed choice the second scan picks up, and what it keeps from the sensor's reply. Patch info and
 * replies are the RoundWhiteDiscKit 7dd6396 test vectors for serial 0RRC989AQ.
 *
 * @author Asbjørn Aarrestad - 2026.10
 */
public class Libre3NfcTest extends RobolectricTestWithConfig {

    private static final String FRESH = "00a50001000200010060541e020401040c0130525243393839415151ff17";
    private static final String ACTIVE = "00a50001000200010060541e020401040c04305252433938394151c6ca";
    private static final String ACTIVATED_REPLY = "00a50058f9b8df22cc3225ec7200000000ad06";
    private static final String SWITCH_REPLY = "00a50058f9b8df22cc02bafbbbfc2bee698e6e";
    private static final String SERIAL = "0RRC989AQ";
    private static final String ACCOUNT = "2977dec2-492a-11ea-9702-0242ac110002";
    private static final String UNIQUE_ID = "0123456789abcdef0123456789abcdef";
    private static final byte[] KAUTH = Hex.toBytes("00112233445566778899aabbccddeeff");
    private static final long NOW = 1_800_000_000_000L;

    @Override
    public void setUp() {
        super.setUp();
        xdrip.setContextAlways(RuntimeEnvironment.application);
        Libre3.forgetSensor();
        Libre3Nfc.clearPending(); // static, so a choice confirmed in one test would carry into the next
    }

    @After
    public void tearDown() {
        Pref.removeItem("use_nfc_scan");
        Pref.removeItem(DexCollectionType.DEX_COLLECTION_METHOD);
        Libre3.forgetSensor();
        Libre3Nfc.clearPending();
    }

    // ==================================================== useNFC =====================================================

    /** With NFC scanning on, the Libre 3 collector listens for scans although it is not one of the Libre bridges. */
    @Test
    public void useNFC_libre3NativeWithNfcScan_true() {
        // :: Setup
        Pref.setBoolean("use_nfc_scan", true);
        DexCollectionType.setDexCollectionType(DexCollectionType.Libre3Native);

        // :: Act
        boolean used = NFCReaderX.useNFC();

        // :: Verify
        assertThat(used).isTrue();
    }

    /** With NFC scanning off, the Libre 3 collector does not listen for scans. */
    @Test
    public void useNFC_libre3NativeWithoutNfcScan_false() {
        // :: Setup
        Pref.setBoolean("use_nfc_scan", false);
        DexCollectionType.setDexCollectionType(DexCollectionType.Libre3Native);

        // :: Act
        boolean used = NFCReaderX.useNFC();

        // :: Verify
        assertThat(used).isFalse();
    }

    // ==================================================== handles ====================================================

    /** A Libre 3 scanned while another Libre collector is selected stays with the existing NFC code. */
    @Test
    public void handles_libre3UnderLibreReceiver_false() {
        // :: Act
        boolean handled = Libre3Nfc.handles(SensorType.Libre3, DexCollectionType.LibreReceiver);

        // :: Verify
        assertThat(handled).isFalse();
    }

    /** A Libre 2 scanned while the Libre 3 collector is selected is not taken here. */
    @Test
    public void handles_libre2UnderLibre3Native_false() {
        // :: Act
        boolean handled = Libre3Nfc.handles(SensorType.Libre2, DexCollectionType.Libre3Native);

        // :: Verify
        assertThat(handled).isFalse();
    }

    /** A Libre 3 scanned while the Libre 3 collector is selected is taken here. */
    @Test
    public void handles_libre3UnderLibre3Native_true() {
        // :: Act
        boolean handled = Libre3Nfc.handles(SensorType.Libre3, DexCollectionType.Libre3Native);

        // :: Verify
        assertThat(handled).isTrue();
    }

    // ==================================================== ignores ====================================================

    /** A Libre 2 scanned while the Libre 3 collector is selected is ignored, so its readings never reach that sensor. */
    @Test
    public void ignores_libre2UnderLibre3Native_true() {
        // :: Act
        boolean ignored = Libre3Nfc.ignores(SensorType.Libre2, DexCollectionType.Libre3Native);

        // :: Verify
        assertThat(ignored).isTrue();
    }

    /** A Libre 3 scanned while the Libre 3 collector is selected is not ignored. */
    @Test
    public void ignores_libre3UnderLibre3Native_false() {
        // :: Act
        boolean ignored = Libre3Nfc.ignores(SensorType.Libre3, DexCollectionType.Libre3Native);

        // :: Verify
        assertThat(ignored).isFalse();
    }

    /** Under another Libre collector the existing NFC code handles every sensor as before. */
    @Test
    public void ignores_libre2UnderLibreReceiver_false() {
        // :: Act
        boolean ignored = Libre3Nfc.ignores(SensorType.Libre2, DexCollectionType.LibreReceiver);

        // :: Verify
        assertThat(ignored).isFalse();
    }

    // =============================================== unusedNfcSettings ===============================================

    /**
     * Under the Libre 3 collector the NFC settings screen hides what only the Libre 1/2 code reads. The filter length
     * stays: the Libre 3 readings are smoothed with it.
     */
    @Test
    public void unusedNfcSettings_libre3Native_libre12Only() {
        // :: Act
        List<String> unused = Libre3Nfc.unusedNfcSettings(DexCollectionType.Libre3Native);

        // :: Verify
        assertThat(unused).containsExactly("libre2_enable_bluetooth_streaming", "nfc_show_age", "nfc_expiry_days",
                "use_nfc_multiblock", "use_nfc_any_tag");
    }

    /** Under another Libre collector the screen shows everything, as before. */
    @Test
    public void unusedNfcSettings_libreReceiver_none() {
        // :: Act
        List<String> unused = Libre3Nfc.unusedNfcSettings(DexCollectionType.LibreReceiver);

        // :: Verify
        assertThat(unused).isEmpty();
    }

    /** Every hidden key is on the NFC settings screen, so a renamed setting cannot slip back into view. */
    @Test
    public void unusedNfcSettings_libre3Native_allOnScreen() throws Exception {
        // :: Act
        List<String> keys = preferenceKeys(R.xml.pref_data_source);

        // :: Verify
        assertThat(keys).containsAtLeastElementsIn(Libre3Nfc.unusedNfcSettings(DexCollectionType.Libre3Native));
    }

    // ==================================================== decide =====================================================

    /** A fresh sensor with an account is started with the account's receiver ID, and the user is warned first. */
    @Test
    public void decide_freshWithAccount_activatesWithAccountId() {
        // :: Act
        Libre3Nfc.Decision decision = Libre3Nfc.decide(patchInfo(FRESH), null, ACCOUNT,
                ReceiverId.Fold.FREESTYLE_LIBRE3, UNIQUE_ID);

        // :: Verify
        assertThat(decision.error).isNull();
        assertThat(decision.code).isEqualTo(NfcCommand.Code.ACTIVATE);
        assertThat(decision.receiverId)
                .isEqualTo(ReceiverId.fromAccount(ACCOUNT, ReceiverId.Fold.FREESTYLE_LIBRE3));
        assertThat(decision.warning).contains("Abbott app");
    }

    /** A fresh sensor without an account gets the per-install ID, with the warning that it can never move back. */
    @Test
    public void decide_freshWithoutAccount_activatesAccountless() {
        // :: Act
        Libre3Nfc.Decision decision = Libre3Nfc.decide(patchInfo(FRESH), null, " ",
                ReceiverId.Fold.FREESTYLE_LIBRE3, UNIQUE_ID);

        // :: Verify
        assertThat(decision.error).isNull();
        assertThat(decision.code).isEqualTo(NfcCommand.Code.ACTIVATE);
        assertThat(decision.receiverId).isEqualTo(ReceiverId.accountless(UNIQUE_ID));
        assertThat(decision.warning).contains("can never move to the Abbott app");
        assertThat(decision.warning).contains("lost if xDrip is reinstalled");
    }

    /** A running sensor is taken over with the receiver ID the chosen app's fold gives for the account. */
    @Test
    public void decide_activeWithAccount_switchesWithChosenFold() {
        // :: Act
        Libre3Nfc.Decision decision = Libre3Nfc.decide(patchInfo(ACTIVE), null, ACCOUNT,
                ReceiverId.Fold.LIBRE_BY_ABBOTT, UNIQUE_ID);

        // :: Verify
        assertThat(decision.error).isNull();
        assertThat(decision.code).isEqualTo(NfcCommand.Code.SWITCH_RECEIVER);
        assertThat(decision.receiverId)
                .isEqualTo(ReceiverId.fromAccount(ACCOUNT, ReceiverId.Fold.LIBRE_BY_ABBOTT));
        assertThat(decision.warning).contains("Abbott app");
    }

    /**
     * Without an account a running sensor is taken over with this install's ID, so a sensor this install started can
     * be picked up again after a lost reply or "Forget sensor". The user is warned first.
     */
    @Test
    public void decide_activeWithoutAccount_takesOverWithInstallId() {
        // :: Act
        Libre3Nfc.Decision decision = Libre3Nfc.decide(patchInfo(ACTIVE), null, "",
                ReceiverId.Fold.FREESTYLE_LIBRE3, UNIQUE_ID);

        // :: Verify
        assertThat(decision.error).isNull();
        assertThat(decision.code).isEqualTo(NfcCommand.Code.SWITCH_RECEIVER);
        assertThat(decision.receiverId).isEqualTo(ReceiverId.accountless(UNIQUE_ID));
        assertThat(decision.account).isEmpty();
        assertThat(decision.warning).contains("started by this xDrip install");
    }

    /** The running sensor xDrip already uses is not taken over again: that would only reset its pairing. */
    @Test
    public void decide_runningSensorInUse_refusedAsAlreadyInUse() {
        // :: Act
        Libre3Nfc.Decision decision = Libre3Nfc.decide(patchInfo(ACTIVE), stored(SERIAL), ACCOUNT,
                ReceiverId.Fold.FREESTYLE_LIBRE3, UNIQUE_ID);

        // :: Verify
        assertThat(decision.code).isNull();
        assertThat(decision.error).isEqualTo(gs(R.string.libre3_already_in_use));
    }

    /** A running sensor other than the one xDrip uses is still taken over. */
    @Test
    public void decide_runningSensorOtherInUse_takesOver() {
        // :: Act
        Libre3Nfc.Decision decision = Libre3Nfc.decide(patchInfo(ACTIVE), stored("0AAA111BB"), ACCOUNT,
                ReceiverId.Fold.FREESTYLE_LIBRE3, UNIQUE_ID);

        // :: Verify
        assertThat(decision.error).isNull();
        assertThat(decision.code).isEqualTo(NfcCommand.Code.SWITCH_RECEIVER);
    }

    /** A sensor that has never run is started, even when xDrip has a sensor stored under its serial. */
    @Test
    public void decide_freshSensorWithStoredSerial_activates() {
        // :: Act
        Libre3Nfc.Decision decision = Libre3Nfc.decide(patchInfo(FRESH), stored(SERIAL), ACCOUNT,
                ReceiverId.Fold.FREESTYLE_LIBRE3, UNIQUE_ID);

        // :: Verify
        assertThat(decision.error).isNull();
        assertThat(decision.code).isEqualTo(NfcCommand.Code.ACTIVATE);
    }

    // ==================================================== onScan =====================================================

    /** Scanning the sensor in use shows that it is in use, asks nothing, and leaves its pairing as it was. */
    @Test
    public void onScan_sensorInUse_showsAlreadyInUseAndKeepsPairing() {
        // :: Setup
        Libre3SensorState state = stored(SERIAL);
        state.setKAuth(KAUTH);
        Libre3.saveState(state);
        Activity activity = Robolectric.buildActivity(Activity.class).setup().get();

        // :: Act
        Libre3Nfc.onScan(activity, null, Hex.toBytes(ACTIVE)); // nothing is sent, so no tag is needed
        shadowOf(Looper.getMainLooper()).idle();

        // :: Verify
        assertThat(ShadowDialog.getShownDialogs()).hasSize(1);
        AlertDialog dialog = (AlertDialog) ShadowDialog.getLatestDialog();
        assertThat(String.valueOf(shadowOf(dialog).getMessage())).isEqualTo(gs(R.string.libre3_already_in_use));
        assertThat(Libre3.loadState().kAuth()).isEqualTo(KAUTH);
    }

    /** A message for a screen that has closed comes as a toast, since a dialog there would crash xDrip. */
    @Test
    public void onScan_screenClosed_messageAsToast() {
        // :: Setup
        Libre3.saveState(stored(SERIAL));
        Activity activity = Robolectric.buildActivity(Activity.class).setup().get();
        activity.finish();

        // :: Act
        Libre3Nfc.onScan(activity, null, Hex.toBytes(ACTIVE));
        shadowOf(Looper.getMainLooper()).idle();

        // :: Verify
        assertThat(ShadowDialog.getShownDialogs()).isEmpty();
        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(gs(R.string.libre3_already_in_use));
    }

    /** The takeover question is not shown on a screen that has closed, and nothing is confirmed. */
    @Test
    public void onScan_screenClosedBeforeQuestion_nothingConfirmed() {
        // :: Setup
        Activity activity = Robolectric.buildActivity(Activity.class).setup().get();
        activity.finish();

        // :: Act
        Libre3Nfc.onScan(activity, null, Hex.toBytes(ACTIVE));
        shadowOf(Looper.getMainLooper()).idle();

        // :: Verify
        assertThat(ShadowDialog.getShownDialogs()).isEmpty();
        assertThat(Libre3Nfc.pending(SERIAL, tsl())).isNull();
    }

    /** Lost contact while sending keeps the confirmed choice, so the next scan sends it without asking again. */
    @Test
    public void onScan_contactLost_keepsConfirmedChoice() throws Exception {
        // :: Setup
        Libre3Nfc.confirm(SERIAL, Libre3Nfc.decide(patchInfo(ACTIVE), null, Libre3.accountId(), Libre3.fold(),
                Libre3.uniqueId()), tsl());
        NfcV tag = mock(NfcV.class);
        when(tag.transceive(any(byte[].class))).thenThrow(new IOException("Tag was lost."));
        Activity activity = Robolectric.buildActivity(Activity.class).setup().get();

        // :: Act
        Libre3Nfc.onScan(activity, tag, Hex.toBytes(ACTIVE));

        // :: Verify
        assertThat(Libre3Nfc.pending(SERIAL, tsl())).isNotNull();
        assertThat(Libre3.loadState()).isNull();
    }

    // ==================================================== pending ====================================================

    /** The second scan of the same sensor within five minutes picks up the choice confirmed after the first. */
    @Test
    public void pending_sameSerialWithinFiveMinutes_returnsDecision() {
        // :: Setup
        Libre3Nfc.Decision decision = Libre3Nfc.decide(patchInfo(ACTIVE), null, ACCOUNT,
                ReceiverId.Fold.FREESTYLE_LIBRE3, UNIQUE_ID);
        Libre3Nfc.confirm(SERIAL, decision, NOW);

        // :: Act
        Libre3Nfc.Decision pending = Libre3Nfc.pending(SERIAL, NOW + 4 * 60_000);

        // :: Verify
        assertThat(pending).isSameInstanceAs(decision);
    }

    /** A different sensor never picks up a choice confirmed for another one. */
    @Test
    public void pending_otherSerial_returnsNull() {
        // :: Setup
        Libre3Nfc.confirm(SERIAL, Libre3Nfc.decide(patchInfo(ACTIVE), null, ACCOUNT,
                ReceiverId.Fold.FREESTYLE_LIBRE3, UNIQUE_ID), NOW);

        // :: Act
        Libre3Nfc.Decision pending = Libre3Nfc.pending("0RRC989AX", NOW + 60_000);

        // :: Verify
        assertThat(pending).isNull();
    }

    /** A choice older than five minutes has lapsed, so the scan starts over with a warning. */
    @Test
    public void pending_afterFiveMinutes_returnsNull() {
        // :: Setup
        Libre3Nfc.confirm(SERIAL, Libre3Nfc.decide(patchInfo(ACTIVE), null, ACCOUNT,
                ReceiverId.Fold.FREESTYLE_LIBRE3, UNIQUE_ID), NOW);

        // :: Act
        Libre3Nfc.Decision pending = Libre3Nfc.pending(SERIAL, NOW + 5 * 60_000 + 1);

        // :: Verify
        assertThat(pending).isNull();
    }

    // ================================================== onResponse ===================================================

    /** A reply that is not an activation reply names the app and account tried, and stores no sensor. */
    @Test
    public void onResponse_unparseableReply_errorNamesFoldAndAccount() {
        // :: Setup
        PatchInfo info = patchInfo(ACTIVE);
        Libre3Nfc.Decision decision = Libre3Nfc.decide(info, null, ACCOUNT, ReceiverId.Fold.LIBRE_BY_ABBOTT, UNIQUE_ID);

        // :: Act
        String error = Libre3Nfc.onResponse(info, decision, new byte[]{0x00, 0x01}, NOW);

        // :: Verify
        assertThat(error).contains("Libre by Abbott");
        assertThat(error).contains(ACCOUNT);
        assertThat(Libre3.loadState()).isNull();
    }

    /** The refused command is logged without the account ID, since the log is shared in bug reports. */
    @Test
    public void onResponse_unparseableReply_logsWithoutAccount() {
        // :: Setup
        PatchInfo info = patchInfo(ACTIVE);
        Libre3Nfc.Decision decision = Libre3Nfc.decide(info, null, ACCOUNT, ReceiverId.Fold.LIBRE_BY_ABBOTT, UNIQUE_ID);

        // :: Act
        Libre3Nfc.onResponse(info, decision, new byte[]{0x00, 0x01}, NOW);

        // :: Verify
        List<UserError> rows = new Select().from(UserError.class).where("shortError = ?", "Libre3Native").execute();
        assertThat(rows).isNotEmpty();
        for (UserError row : rows) {
            assertThat(row.message).doesNotContain(ACCOUNT);
        }
        assertThat(rows.get(rows.size() - 1).message).contains("Libre by Abbott");
    }

    /** A takeover keeps the address, PIN, receiver ID, fold, cycle and the start time the sensor reports. */
    @Test
    public void onResponse_switchReply_storesSensorWithReportedStart() {
        // :: Setup
        PatchInfo info = patchInfo(ACTIVE);
        Libre3Nfc.Decision decision = Libre3Nfc.decide(info, null, ACCOUNT, ReceiverId.Fold.LIBRE_BY_ABBOTT, UNIQUE_ID);

        // :: Act
        String error = Libre3Nfc.onResponse(info, decision, Hex.toBytes(SWITCH_REPLY), NOW);

        // :: Verify
        assertThat(error).isNull();
        Libre3SensorState state = Libre3.loadState();
        assertThat(state.serial()).isEqualTo(SERIAL);
        assertThat(state.bleAddress()).isEqualTo("CC:22:DF:B8:F9:58");
        assertThat(state.receiverId()).isEqualTo(decision.receiverId);
        assertThat(state.fold()).isEqualTo(ReceiverId.Fold.LIBRE_BY_ABBOTT);
        assertThat(state.activationMs()).isEqualTo(0x69ee2bfcL * 1000);
        assertThat(state.wearMinutes()).isEqualTo(21600);
        assertThat(state.region()).isEqualTo(PatchInfo.Region.US);
    }

    /** A fresh start reports no start time, so the time the command was sent is kept as the start. */
    @Test
    public void onResponse_activateReply_storesCommandTimeAsStart() {
        // :: Setup
        PatchInfo info = patchInfo(FRESH);
        Libre3Nfc.Decision decision = Libre3Nfc.decide(info, null, ACCOUNT,
                ReceiverId.Fold.FREESTYLE_LIBRE3, UNIQUE_ID);

        // :: Act
        String error = Libre3Nfc.onResponse(info, decision, Hex.toBytes(ACTIVATED_REPLY), NOW);

        // :: Verify
        assertThat(error).isNull();
        assertThat(Libre3.loadState().activationMs()).isEqualTo(NOW);
    }

    /** Scanning the same started sensor again with a reply that reports no start time keeps the stored start. */
    @Test
    public void onResponse_sameSerialAgain_keepsFirstStart() {
        // :: Setup
        PatchInfo info = patchInfo(FRESH);
        Libre3Nfc.Decision decision = Libre3Nfc.decide(info, null, ACCOUNT,
                ReceiverId.Fold.FREESTYLE_LIBRE3, UNIQUE_ID);
        Libre3Nfc.onResponse(info, decision, Hex.toBytes(ACTIVATED_REPLY), NOW);

        // :: Act
        Libre3Nfc.onResponse(info, decision, Hex.toBytes(ACTIVATED_REPLY), NOW + 3_600_000);

        // :: Verify
        assertThat(Libre3.loadState().activationMs()).isEqualTo(NOW);
    }

    /**
     * Scanning the same sensor again when the reply reports a start time takes that time, so a wrong start time
     * stored before can be put right by a new scan.
     */
    @Test
    public void onResponse_sameSerialReportedStart_takesReportedStart() {
        // :: Setup
        PatchInfo info = patchInfo(FRESH);
        Libre3Nfc.Decision decision = Libre3Nfc.decide(info, null, ACCOUNT,
                ReceiverId.Fold.FREESTYLE_LIBRE3, UNIQUE_ID);
        Libre3Nfc.onResponse(info, decision, Hex.toBytes(ACTIVATED_REPLY), NOW);

        // :: Act
        Libre3Nfc.onResponse(info, decision, Hex.toBytes(SWITCH_REPLY), NOW + 3_600_000);

        // :: Verify
        assertThat(Libre3.loadState().activationMs()).isEqualTo(0x69ee2bfcL * 1000);
    }

    private static PatchInfo patchInfo(String hex) {
        return PatchInfo.parse(Hex.toBytes(hex));
    }

    private static Libre3SensorState stored(String serial) {
        return new Libre3SensorState(serial, "CC:22:DF:B8:F9:58", new byte[]{0x32, 0x25, (byte) 0xec, 0x72});
    }

    /** The android:key of every preference in the XML resource. */
    private static List<String> preferenceKeys(int xml) throws Exception {
        List<String> keys = new ArrayList<>();
        XmlResourceParser parser = RuntimeEnvironment.getApplication().getResources().getXml(xml);
        for (int event = parser.getEventType(); event != XmlPullParser.END_DOCUMENT; event = parser.next()) {
            if (event == XmlPullParser.START_TAG) {
                String key = parser.getAttributeValue("http://schemas.android.com/apk/res/android", "key");
                if (key != null) {
                    keys.add(key);
                }
            }
        }
        return keys;
    }
}
