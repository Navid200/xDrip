package com.eveningoutpost.dexdrip.cgm.libre3;

import static com.eveningoutpost.dexdrip.xdrip.gs;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eveningoutpost.dexdrip.R;
import com.eveningoutpost.dexdrip.models.JoH;
import com.eveningoutpost.dexdrip.models.UserError;
import com.eveningoutpost.dexdrip.nfc.NFControl;
import com.eveningoutpost.dexdrip.utilitymodels.CollectionServiceStarter;
import com.eveningoutpost.dexdrip.utilitymodels.OkHttpWrapper;
import com.eveningoutpost.dexdrip.utilitymodels.StatusItem;
import com.eveningoutpost.dexdrip.utils.ActivityWithMenu;
import com.eveningoutpost.dexdrip.utils.LocationHelper;

import java.security.GeneralSecurityException;

/**
 * The Libre 3 screen: what the collector knows, asking for location, downloading the app credential, and forgetting
 * the sensor.
 * Starting or taking over a sensor is done by scanning it with NFC; the screen reads scans itself while it is open.
 *
 * @author Asbjørn Aarrestad - 2026.10
 */
public class Libre3Activity extends ActivityWithMenu {

    private static final String TAG = "Libre3Native";
    /** How often the open screen shows the collector's status again, so a scan or a reconnect on it shows. */
    private static final long REFRESH_MS = 5_000;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable refresher = new Runnable() {
        @Override
        public void run() {
            refresh();
            handler.postDelayed(this, REFRESH_MS);
        }
    };
    private TextView status;
    private boolean offeredDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int pad = (int) (16 * getResources().getDisplayMetrics().density);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(pad, pad, pad, pad);

        TextView help = new TextView(this);
        help.setText(R.string.libre3_help);
        layout.addView(help);

        status = new TextView(this);
        status.setPadding(0, pad, 0, pad);
        layout.addView(status);

        Button download = new Button(this);
        download.setText(R.string.libre3_download_credential);
        download.setOnClickListener(v -> downloadCredential());
        layout.addView(download);

        Button forget = new Button(this);
        forget.setText(R.string.libre3_forget_sensor);
        forget.setOnClickListener(v -> confirmForget());
        layout.addView(forget);

        Button diagnostics = new Button(this);
        diagnostics.setText(R.string.libre3_copy_diagnostics);
        diagnostics.setOnClickListener(v -> Libre3Diagnostics.copyToClipboard(this));
        layout.addView(diagnostics);

        ScrollView scroll = new ScrollView(this);
        scroll.addView(layout);
        setContentView(scroll);
        JoH.fixActionBar(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // a scan on this screen is read here, so the start or takeover question is asked on it
        NFControl.initNFC(this, false);
        handler.removeCallbacks(refresher);
        refresher.run();
        if (locationReady()) {
            offerDownload();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(refresher);
        NFControl.initNFC(this, true); // disables
    }

    /**
     * The Bluetooth scan needs location permission and location turned on; asks for whichever is missing, as the
     * other Bluetooth collectors do. The credential offer waits, so two dialogs never stack.
     *
     * @return true when nothing was asked
     */
    private boolean locationReady() {
        try {
            return LocationHelper.requestLocationForBluetooth(this) && LocationHelper.isLocationEnabled(this);
        } catch (Exception e) {
            UserError.Log.e(TAG, "Exception requesting location: " + e.getMessage());
            return true;
        }
    }

    /** Without a credential, asks once per visit whether to download it now. */
    private void offerDownload() {
        if (offeredDownload || !Libre3Credential.get(this).load().isEmpty()) {
            return;
        }
        offeredDownload = true;
        new AlertDialog.Builder(this)
                .setTitle(R.string.libre3_download_credential)
                .setMessage(R.string.libre3_offer_download)
                .setPositiveButton(R.string.yes, (dialog, which) -> downloadCredential())
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void refresh() {
        StringBuilder sb = new StringBuilder();
        for (StatusItem item : Libre3Service.megaStatus()) {
            if (item.runnable == null) {
                sb.append(item.name).append(": ").append(item.value).append('\n');
            }
        }
        status.setText(sb.toString().trim());
    }

    private void downloadCredential() {
        JoH.static_toast_short(gs(R.string.libre3_downloading));
        new Thread(() -> {
            String message;
            try {
                Libre3Credential.get(this).download(OkHttpWrapper.getClient());
                message = gs(R.string.libre3_credential_ready);
                CollectionServiceStarter.restartCollectionServiceBackground();
            } catch (GeneralSecurityException e) {
                UserError.Log.e(TAG, e.getMessage());
                message = gs(R.string.libre3_download_failed);
            }
            String result = message;
            runOnUiThread(() -> {
                JoH.static_toast_long(result);
                refresh();
            });
        }).start();
    }

    private void confirmForget() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.libre3_forget_sensor)
                .setMessage(R.string.libre3_forget_sensor_confirm)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    Libre3.forgetSensor();
                    UserError.Log.uel(TAG, "Libre 3 sensor forgotten by user");
                    CollectionServiceStarter.restartCollectionServiceBackground();
                    refresh();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public String getMenuName() {
        return "";
    }
}
