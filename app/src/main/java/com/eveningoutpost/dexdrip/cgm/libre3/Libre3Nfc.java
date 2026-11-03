package com.eveningoutpost.dexdrip.cgm.libre3;

import static com.eveningoutpost.dexdrip.models.JoH.tsl;
import static com.eveningoutpost.dexdrip.xdrip.gs;

import android.app.Activity;
import android.app.AlertDialog;
import android.nfc.tech.NfcV;

import com.eveningoutpost.dexdrip.R;
import com.eveningoutpost.dexdrip.models.JoH;
import com.eveningoutpost.dexdrip.models.LibreOOPAlgorithm.SensorType;
import com.eveningoutpost.dexdrip.models.UserError;
import com.eveningoutpost.dexdrip.utilitymodels.CollectionServiceStarter;
import com.eveningoutpost.dexdrip.utils.DexCollectionType;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lwld.libre3.Libre3SensorState;
import lwld.libre3.nfc.ActivationResponse;
import lwld.libre3.nfc.NfcCommand;
import lwld.libre3.nfc.PatchInfo;
import lwld.libre3.pairing.ReceiverId;

/**
 * Starts a fresh Libre 3 or takes over a running one over NFC. The first scan only decides and warns; once the user
 * confirms, the next scan of the same sensor within five minutes sends the command. Never logs the PIN or the reply.
 *
 * @author Asbjørn Aarrestad - 2026.10
 */
public final class Libre3Nfc {

    private static final String TAG = "Libre3Native";
    private static final long PENDING_MS = 5 * 60_000;

    private static String pendingSerial;
    private static Decision pendingDecision;
    private static long pendingAt;

    /** What a scan would send: either a command with the warning to show first, or an error and nothing to send. */
    public static final class Decision {
        public final NfcCommand.Code code;
        public final ReceiverId receiverId;
        public final ReceiverId.Fold fold;
        /** The account behind the receiver ID, or blank for an accountless start. */
        public final String account;
        public final String warning;
        public final String error;

        private Decision(NfcCommand.Code code, ReceiverId receiverId, ReceiverId.Fold fold, String account,
                         String warning, String error) {
            this.code = code;
            this.receiverId = receiverId;
            this.fold = fold;
            this.account = account;
            this.warning = warning;
            this.error = error;
        }

        boolean sameCommand(Decision other) {
            return other != null && code == other.code && receiverId != null && receiverId.equals(other.receiverId);
        }
    }

    private Libre3Nfc() {
    }

    /** Only a Libre 3 scanned while the native Libre 3 collector is selected is taken here. */
    public static boolean handles(SensorType sensorType, DexCollectionType collector) {
        return sensorType == SensorType.Libre3 && collector == DexCollectionType.Libre3Native;
    }

    /**
     * Any other Libre scanned while the native Libre 3 collector is selected is ignored, so the existing NFC code
     * does not store its readings on the Libre 3 sensor.
     */
    public static boolean ignores(SensorType sensorType, DexCollectionType collector) {
        return sensorType != SensorType.Libre3 && collector == DexCollectionType.Libre3Native;
    }

    /**
     * @return the NFC settings that only the Libre 1/2 code reads, for the settings screen to hide under the Libre 3
     * collector; empty under any other collector
     */
    public static List<String> unusedNfcSettings(DexCollectionType collector) {
        if (collector != DexCollectionType.Libre3Native) {
            return Collections.emptyList();
        }
        return Arrays.asList("libre2_enable_bluetooth_streaming", "nfc_show_age", "nfc_expiry_days",
                "use_nfc_multiblock", "use_nfc_any_tag");
    }

    /**
     * A fresh sensor is started with the account's receiver ID, or the per-install ID when no account is given. A
     * running sensor is taken over the same way: with the account it was started with, or without one with the
     * per-install ID, which only a sensor this install started accepts.
     */
    public static Decision decide(PatchInfo info, String account, ReceiverId.Fold fold, String uniqueId) {
        String trimmed = account == null ? "" : account.trim();
        boolean hasAccount = !trimmed.isEmpty();
        if (info.recommended() == NfcCommand.Code.ACTIVATE) {
            if (hasAccount) {
                return new Decision(NfcCommand.Code.ACTIVATE, ReceiverId.fromAccount(trimmed, fold), fold, trimmed,
                        gs(R.string.libre3_warn_activate_account), null);
            }
            return new Decision(NfcCommand.Code.ACTIVATE, ReceiverId.accountless(uniqueId), fold, "",
                    gs(R.string.libre3_warn_activate_accountless), null);
        }
        if (!hasAccount) {
            return new Decision(NfcCommand.Code.SWITCH_RECEIVER, ReceiverId.accountless(uniqueId), fold, "",
                    gs(R.string.libre3_warn_takeover_accountless), null);
        }
        return new Decision(NfcCommand.Code.SWITCH_RECEIVER, ReceiverId.fromAccount(trimmed, fold), fold, trimmed,
                gs(R.string.libre3_warn_takeover), null);
    }

    /** Remembers the choice the user confirmed, for the next scan of the same sensor. */
    public static synchronized void confirm(String serial, Decision decision, long nowMs) {
        pendingSerial = serial;
        pendingDecision = decision;
        pendingAt = nowMs;
    }

    /** The choice confirmed for this sensor in the last five minutes, or null. */
    public static synchronized Decision pending(String serial, long nowMs) {
        if (pendingDecision == null || !serial.equals(pendingSerial) || nowMs - pendingAt > PENDING_MS) {
            return null;
        }
        return pendingDecision;
    }

    private static synchronized void clearPending() {
        pendingSerial = null;
        pendingDecision = null;
        pendingAt = 0;
    }

    /**
     * Stores the sensor from the reply to a sent command. A switch reply carries the original start time; a fresh
     * start carries none, so the time the command was sent is used. The same sensor seen again keeps its first start.
     *
     * @return null when stored, else an error naming the app and account that were tried; the log line for it leaves
     * the account ID out, since the log is shared in bug reports
     */
    public static String onResponse(PatchInfo info, Decision decision, byte[] reply, long commandMs) {
        ActivationResponse response;
        try {
            response = ActivationResponse.parse(reply);
        } catch (IllegalArgumentException e) {
            UserError.Log.uel(TAG, "Libre 3 command not accepted: app " + foldName(decision.fold) + ", "
                    + (decision.account.isEmpty() ? "no account" : "with account") + ", " + e.getMessage());
            return gs(R.string.libre3_error_bad_reply, foldName(decision.fold),
                    decision.account.isEmpty() ? gs(R.string.libre3_no_account) : decision.account);
        }
        long reported = littleEndianSeconds(response.activationTimeRaw()) * 1000;
        long activationMs = reported > 0 ? reported : commandMs;
        Libre3SensorState previous = Libre3.loadState();
        boolean sameSensor = previous != null && previous.serial().equals(info.serial());
        if (reported <= 0 && sameSensor && previous.activationMs() > 0) {
            activationMs = previous.activationMs();
        }
        Libre3SensorState state = new Libre3SensorState(info.serial(), response.bleAddress(), response.blePin());
        state.setReceiverId(decision.receiverId);
        state.setFold(decision.fold);
        state.setActivationMs(activationMs);
        state.applySensorCycle(info);
        Libre3.saveState(state);
        return null;
    }

    /**
     * Takes a Libre 3 scan from NFCReaderX, on its background thread with the tag connected. Without a confirmed
     * choice for this sensor it shows the warning and sends nothing.
     */
    public static void onScan(Activity activity, NfcV tag, byte[] rawPatchInfo) {
        PatchInfo info;
        try {
            info = PatchInfo.parse(rawPatchInfo);
        } catch (IllegalArgumentException e) {
            UserError.Log.e(TAG, "Libre 3 patch info not understood: " + e.getMessage());
            JoH.static_toast_long(gs(R.string.libre3_error_patch_info));
            return;
        }
        UserError.Log.uel(TAG, "Libre 3 scanned: " + info.serial() + " region " + info.region()
                + " security " + info.securityVersion());
        Decision decision = decide(info, Libre3.accountId(), Libre3.fold(), Libre3.uniqueId());
        if (decision.error != null) {
            UserError.Log.uel(TAG, decision.error);
            showMessage(activity, decision.error);
            return;
        }
        long now = tsl();
        if (!decision.sameCommand(pending(info.serial(), now))) {
            askFirst(activity, info.serial(), decision);
            return;
        }
        clearPending();
        long commandMs = (now / 1000 - 1) * 1000;
        byte[] reply;
        try {
            reply = tag.transceive(NfcCommand.command(decision.code, commandMs / 1000, decision.receiverId));
        } catch (IOException | SecurityException e) {
            UserError.Log.e(TAG, "Libre 3 NFC command failed: " + e.getMessage());
            JoH.static_toast_long(gs(R.string.libre3_error_nfc_failed));
            return;
        }
        UserError.Log.uel(TAG, "Libre 3 " + decision.code + " sent, fold " + decision.fold + ", reply "
                + (reply == null ? 0 : reply.length) + " bytes");
        String error = onResponse(info, decision, reply == null ? new byte[0] : reply, commandMs);
        if (error != null) {
            showMessage(activity, error);
            return;
        }
        Libre3.ensureSensor(Libre3.loadState());
        JoH.static_toast_long(gs(R.string.libre3_nfc_done));
        CollectionServiceStarter.restartCollectionServiceBackground();
    }

    private static void askFirst(Activity activity, String serial, Decision decision) {
        activity.runOnUiThread(() -> new AlertDialog.Builder(activity)
                .setTitle(decision.code == NfcCommand.Code.ACTIVATE
                        ? R.string.libre3_title_activate : R.string.libre3_title_takeover)
                .setMessage(decision.warning + "\n\n" + gs(R.string.libre3_scan_again))
                .setPositiveButton(R.string.ok, (dialog, which) -> confirm(serial, decision, tsl()))
                .setNegativeButton(R.string.cancel, null)
                .show());
    }

    private static void showMessage(Activity activity, String message) {
        activity.runOnUiThread(() -> new AlertDialog.Builder(activity)
                .setTitle(R.string.libre3_title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null)
                .show());
    }

    private static String foldName(ReceiverId.Fold fold) {
        return gs(fold == ReceiverId.Fold.LIBRE_BY_ABBOTT ? R.string.libre3_fold_lba : R.string.libre3_fold_fsl3);
    }

    private static long littleEndianSeconds(byte[] le4) {
        long v = 0;
        for (int i = 3; i >= 0; i--) {
            v = (v << 8) | (le4[i] & 0xff);
        }
        return v;
    }
}
