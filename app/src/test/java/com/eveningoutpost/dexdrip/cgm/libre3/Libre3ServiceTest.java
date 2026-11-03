package com.eveningoutpost.dexdrip.cgm.libre3;

import static com.eveningoutpost.dexdrip.models.JoH.tsl;
import static com.eveningoutpost.dexdrip.utilitymodels.Constants.DAY_IN_MS;
import static com.eveningoutpost.dexdrip.utilitymodels.Constants.MINUTE_IN_MS;
import static com.eveningoutpost.dexdrip.xdrip.gs;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import static org.robolectric.Shadows.shadowOf;

import android.content.Intent;
import android.util.Log;

import com.eveningoutpost.dexdrip.R;
import com.eveningoutpost.dexdrip.RobolectricTestWithConfig;
import com.eveningoutpost.dexdrip.g5model.SensorDays;
import com.eveningoutpost.dexdrip.models.BgReading;
import com.eveningoutpost.dexdrip.models.UserError;
import com.eveningoutpost.dexdrip.utilitymodels.Pref;
import com.eveningoutpost.dexdrip.utilitymodels.StatusItem;
import com.eveningoutpost.dexdrip.utils.DexCollectionType;
import com.eveningoutpost.dexdrip.xdrip;

import org.junit.After;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ServiceController;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import lwld.libre3.Libre3SensorState;
import lwld.libre3.data.PatchStatus;
import lwld.libre3.data.RealtimeReading;
import lwld.libre3.nfc.PatchInfo;
import lwld.libre3.util.Hex;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Tests for {@link Libre3Service.Session}: each Bluetooth session hands its values to the readings of its own sensor.
 *
 * @author Asbjørn Aarrestad - 2026.10
 */
public class Libre3ServiceTest extends RobolectricTestWithConfig {

    /** A US Libre 3 Plus: 60 minutes warm-up, 21600 minutes wear. */
    private static final String PATCH_INFO = "00a50001000200010060541e020401040c0130525243393839415151ff17";

    private long now;

    @Override
    public void setUp() {
        super.setUp();
        xdrip.setContextAlways(RuntimeEnvironment.application);
        // DexCollectionType reads the one-minute setting once, when the class loads, and keeps it for every later test
        // in the run: load it here with the setting off, before any test turns it on
        Pref.removeItem("libre_one_minute");
        DexCollectionType.getDexCollectionType();
        Libre3.forgetSensor();
        Pref.setBoolean("libre_one_minute", true);
        now = tsl();
    }

    @After
    public void tearDown() {
        Pref.removeItem("dex_collection_method");
        Pref.removeItem("libre_one_minute");
        Libre3.forgetSensor();
        SensorDays.clearCache();
        UserError.ExtraLogTags.readPreference("");
    }

    // ==================================================== Session ====================================================

    /**
     * A reading still arriving from the old sensor's session after a new sensor was scanned is not stored, not even
     * at the new sensor's times.
     */
    @Test
    public void onReading_oldSessionAfterNewSensor_notStored() {
        // :: Setup
        Libre3SensorState old = sensor("0RRC989AQ", now - 1010 * MINUTE_IN_MS);
        Libre3Service.Session oldSession = new Libre3Service.Session(new Libre3Readings(old));
        Libre3SensorState replacement = sensor("0RRCNEW00", now - 2000 * MINUTE_IN_MS);
        new Libre3Service.Session(new Libre3Readings(replacement));

        // :: Act
        oldSession.onReading(reading(1000, 120));

        // :: Verify
        assertThat(BgReading.getForPreciseTimestamp(old.activationMs() + 1000 * MINUTE_IN_MS, 1000)).isNull();
        assertThat(BgReading.getForPreciseTimestamp(replacement.activationMs() + 1000 * MINUTE_IN_MS, 1000)).isNull();
    }

    /** The current sensor's session stores its readings. */
    @Test
    public void onReading_currentSession_stored() {
        // :: Setup
        Libre3SensorState current = sensor("0RRC989AQ", now - 1010 * MINUTE_IN_MS);
        Libre3Service.Session session = new Libre3Service.Session(new Libre3Readings(current));

        // :: Act
        session.onReading(reading(1000, 120));

        // :: Verify
        BgReading stored = BgReading.getForPreciseTimestamp(current.activationMs() + 1000 * MINUTE_IN_MS, 1000);
        assertThat(stored.calculated_value).isEqualTo(120.0);
    }

    /** With extra logging on for the collector, a Bluetooth debug line is kept in xDrip's log, where a tester can send it. */
    @Test
    public void onLog_debugWithExtraTag_isStored() {
        // :: Setup
        UserError.ExtraLogTags.readPreference("Libre3Native:d");
        Libre3Service.Session session = new Libre3Service.Session(new Libre3Readings(sensor("0RRC989AQ", now)));

        // :: Act
        session.onLog(Log.DEBUG, "gatt.connect()");

        // :: Verify
        assertThat(logRows("BLE: gatt.connect()")).hasSize(1);
    }

    /** Without extra logging a Bluetooth debug line is not kept. */
    @Test
    public void onLog_debugWithoutExtraTag_isNotStored() {
        // :: Setup
        UserError.ExtraLogTags.readPreference("");
        Libre3Service.Session session = new Libre3Service.Session(new Libre3Readings(sensor("0RRC989AQ", now)));

        // :: Act
        session.onLog(Log.DEBUG, "gatt.connect()");

        // :: Verify
        assertThat(logRows("BLE: gatt.connect()")).isEmpty();
    }

    /**
     * Without extra logging a Bluetooth error line is not kept either: an out-of-range sensor fails a connect every few
     * minutes, and those must not fill a normal user's error list.
     */
    @Test
    public void onLog_errorWithoutExtraTag_isNotStored() {
        // :: Setup
        UserError.ExtraLogTags.readPreference("");
        Libre3Service.Session session = new Libre3Service.Session(new Libre3Readings(sensor("0RRC989AQ", now)));

        // :: Act
        session.onLog(Log.ERROR, "Error (0x85): GATT ERROR");

        // :: Verify
        assertThat(logRows("BLE: Error (0x85): GATT ERROR")).isEmpty();
    }

    /** Each retry is counted by its reason, so a tester's report says at a glance why the session kept retrying. */
    @Test
    public void onStage_retries_countedByReason() {
        // :: Setup
        Libre3Service.Session session = new Libre3Service.Session(new Libre3Readings(sensor("0RRC989AQ", now)));

        // :: Act
        session.onStage("Connecting");
        session.onStage("Reconnecting in 5 s: connect failed, reason 10");
        session.onStage("Reconnecting in 10 s: sensor not seen");
        session.onStage("Reconnecting in 15 s: connect failed, reason 10");

        // :: Verify
        assertThat(session.retryCounts())
                .containsExactly("connect failed, reason 10", 2, "sensor not seen", 1)
                .inOrder();
    }

    /** Only the newest 20 stages are kept, oldest first, so a long session does not grow without bound. */
    @Test
    public void onStage_manyStages_keepsNewest20() {
        // :: Setup
        Libre3Service.Session session = new Libre3Service.Session(new Libre3Readings(sensor("0RRC989AQ", now)));

        // :: Act
        for (int i = 0; i < 25; i++) {
            session.onStage("s" + i);
        }

        // :: Verify
        List<String> stages = session.recentStages();
        assertThat(stages).hasSize(20);
        assertThat(stages.get(0)).endsWith(" s5");
        assertThat(stages.get(19)).endsWith(" s24");
    }

    // ===================================================== Stall =====================================================

    /** A session that never reported a stage or a value is stalled, so the next wake-up restarts it. */
    @Test
    public void stalled_noProgress_true() {
        // :: Setup
        Libre3Service.Session session = new Libre3Service.Session(new Libre3Readings(sensor("0RRC989AQ", now)));

        // :: Act
        boolean stalled = session.stalled(tsl());

        // :: Verify
        assertThat(stalled).isTrue();
    }

    /** A stage reported eleven minutes ago is recent enough: the session is still working. */
    @Test
    public void stalled_stageElevenMinutesAgo_false() {
        // :: Setup
        Libre3Service.Session session = new Libre3Service.Session(new Libre3Readings(sensor("0RRC989AQ", now)));
        session.onStage("Connecting");

        // :: Act
        boolean stalled = session.stalled(tsl() + 11 * MINUTE_IN_MS);

        // :: Verify
        assertThat(stalled).isFalse();
    }

    /** No stage or value for thirteen minutes means the session is stuck, e.g. a retry timer that Doze held back. */
    @Test
    public void stalled_stageThirteenMinutesAgo_true() {
        // :: Setup
        Libre3Service.Session session = new Libre3Service.Session(new Libre3Readings(sensor("0RRC989AQ", now)));
        session.onStage("Reconnecting in 300 s: disconnected, reason 8");

        // :: Act
        boolean stalled = session.stalled(tsl() + 13 * MINUTE_IN_MS);

        // :: Verify
        assertThat(stalled).isTrue();
    }

    /** A reading counts as progress even when it is not stored. */
    @Test
    public void stalled_readingJustNow_false() {
        // :: Setup
        Libre3Service.Session session = new Libre3Service.Session(new Libre3Readings(sensor("0RRC989AQ", now)));
        session.onReading(reading(1, 120));

        // :: Act
        boolean stalled = session.stalled(tsl());

        // :: Verify
        assertThat(stalled).isFalse();
    }

    /** A sensor status counts as progress, as during warm-up when no glucose value is stored. */
    @Test
    public void stalled_statusJustNow_false() {
        // :: Setup
        Libre3Service.Session session = new Libre3Service.Session(new Libre3Readings(sensor("0RRC989AQ", now)));
        session.onStatus(PatchStatus.parse(new byte[PatchStatus.SIZE]));

        // :: Act
        boolean stalled = session.stalled(tsl());

        // :: Verify
        assertThat(stalled).isFalse();
    }

    /** An error alone is not progress: a session that only failed, like one without a Bluetooth adapter, is stalled. */
    @Test
    public void stalled_onlyError_true() {
        // :: Setup
        Libre3Service.Session session = new Libre3Service.Session(new Libre3Readings(sensor("0RRC989AQ", now)));
        session.onError("No Bluetooth adapter");

        // :: Act
        boolean stalled = session.stalled(tsl());

        // :: Verify
        assertThat(stalled).isTrue();
    }

    // =================================================== Start ======================================================

    /** Without a credential, starting the collector opens the Libre 3 screen, which offers the download. */
    @Test
    public void onStartCommand_noCredential_opensLibre3Screen() {
        // :: Setup
        selectSource(DexCollectionType.Libre3Native);

        // :: Act
        Robolectric.buildService(Libre3Service.class).create().startCommand(0, 1);

        // :: Verify
        Intent started = shadowOf(RuntimeEnvironment.getApplication()).getNextStartedActivity();
        assertThat(started.getComponent().getClassName()).isEqualTo(Libre3Activity.class.getName());
    }

    /** The five-minute wake-ups do not open the screen again; only a new start of the collector does. */
    @Test
    public void onStartCommand_wakeUp_opensOnce() {
        // :: Setup
        selectSource(DexCollectionType.Libre3Native);
        ServiceController<Libre3Service> service = Robolectric.buildService(Libre3Service.class).create();
        service.startCommand(0, 1);
        shadowOf(RuntimeEnvironment.getApplication()).getNextStartedActivity();

        // :: Act
        service.startCommand(0, 2);

        // :: Verify
        assertThat(shadowOf(RuntimeEnvironment.getApplication()).getNextStartedActivity()).isNull();
    }

    /** With extra logging on, a restart of the collector shows in the log as the service ending and starting. */
    @Test
    public void lifecycle_withExtraTag_logsCreateAndDestroy() {
        // :: Setup
        UserError.ExtraLogTags.readPreference("Libre3Native:d");
        selectSource(DexCollectionType.Libre3Native);

        // :: Act
        Robolectric.buildService(Libre3Service.class).create().startCommand(0, 1).destroy();

        // :: Verify
        assertThat(logRows("Service created")).hasSize(1);
        assertThat(logRows("Start command, no session running")).hasSize(1);
        assertThat(logRows("Service destroyed")).hasSize(1);
    }

    /** A collector that is not the chosen source stops without opening anything. */
    @Test
    public void onStartCommand_otherSource_opensNothing() {
        // :: Setup
        selectSource(DexCollectionType.Disabled);

        // :: Act
        Robolectric.buildService(Libre3Service.class).create().startCommand(0, 1);

        // :: Verify
        assertThat(shadowOf(RuntimeEnvironment.getApplication()).getNextStartedActivity()).isNull();
    }

    // ================================================== megaStatus ===================================================

    /** Without a credential, the status page says why the last download failed. */
    @Test
    public void megaStatus_downloadFailed_showsReason() {
        // :: Setup
        Libre3Credential credential = Libre3Credential.get(xdrip.getAppContext());
        assertThrows(GeneralSecurityException.class, () -> credential.download(answering404()));

        // :: Act
        List<StatusItem> status = Libre3Service.megaStatus();

        // :: Verify
        assertThat(status.stream().anyMatch(item -> item.value.contains("HTTP 404"))).isTrue();
    }

    /** Right after "Forget sensor" the old session's stage is gone: the page says there is no sensor. */
    @Test
    public void megaStatus_afterForget_showsNoSensor() {
        // :: Setup
        Libre3Service.Session session = new Libre3Service.Session(new Libre3Readings(sensor("0RRC989AQ", now)));
        session.onStage("Reconnecting in 55 s: connect failed, reason 10");

        // :: Act
        Libre3.forgetSensor();
        List<StatusItem> status = Libre3Service.megaStatus();

        // :: Verify
        assertThat(value(status, R.string.libre3_sensor)).isEqualTo(gs(R.string.libre3_none));
        assertThat(value(status, R.string.libre3_stage)).isEqualTo(gs(R.string.libre3_stage_no_sensor));
    }

    /** The sensor end on the status page is the same text as on the home screen. */
    @Test
    public void megaStatus_runningSensor_endsAsOnHomeScreen() {
        // :: Setup
        selectSource(DexCollectionType.Libre3Native);
        SensorDays.clearCache();
        sensor("0RRC989AQ", now - 2 * DAY_IN_MS - 10 * MINUTE_IN_MS);

        // :: Act
        List<StatusItem> status = Libre3Service.megaStatus();

        // :: Verify
        assertThat(value(status, R.string.libre3_remaining)).isEqualTo("Ends in 13 days");
        assertThat(value(status, R.string.libre3_remaining)).isEqualTo(SensorDays.get().getSpannable().toString());
    }

    // ==================================================== helpers ====================================================

    /** The collector's rows in xDrip's log with this message. */
    private static List<UserError> logRows(String message) {
        List<UserError> rows = new ArrayList<>();
        for (UserError row : UserError.all()) {
            if ("Libre3Native".equals(row.shortError) && message.equals(row.message)) {
                rows.add(row);
            }
        }
        return rows;
    }

    /** The value of the status row with this name, or null when the page has no such row. */
    private static String value(List<StatusItem> status, int name) {
        return status.stream().filter(item -> gs(name).equals(item.name)).map(item -> item.value).findFirst()
                .orElse(null);
    }


    /** Stores a running sensor as the current one, as an NFC scan does. */
    private static Libre3SensorState sensor(String serial, long activationMs) {
        Libre3SensorState state = new Libre3SensorState(serial, "CC:22:DF:B8:F9:58", new byte[]{1, 2, 3, 4});
        state.setActivationMs(activationMs);
        state.applySensorCycle(PatchInfo.parse(Hex.toBytes(PATCH_INFO)));
        Libre3.saveState(state);
        Libre3.ensureSensor(state);
        return state;
    }

    /**
     * Selects the source with the one-minute setting off: DexCollectionType reads that setting once, when the class
     * loads, and keeps it for every later test in the run.
     */
    private static void selectSource(DexCollectionType type) {
        Pref.removeItem("libre_one_minute");
        DexCollectionType.setDexCollectionType(type);
    }

    /** A client that never touches the network: every request is answered 404. */
    private static OkHttpClient answering404() {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> new Response.Builder()
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_1)
                        .code(404)
                        .message("test")
                        .body(ResponseBody.create(new byte[0], null))
                        .build())
                .build();
    }

    private static RealtimeReading reading(int lifeCount, int mgdl) {
        byte[] b = new byte[RealtimeReading.SIZE];
        b[0] = (byte) lifeCount;
        b[1] = (byte) (lifeCount >> 8);
        b[14] = 3;
        b[15] = (byte) mgdl;
        b[16] = (byte) (mgdl >> 8);
        return RealtimeReading.parse(b);
    }
}
