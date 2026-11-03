package com.eveningoutpost.dexdrip.utils;

import static com.google.common.truth.Truth.assertThat;

import com.eveningoutpost.dexdrip.RobolectricTestWithConfig;
import com.eveningoutpost.dexdrip.cgm.libre3.Libre3Service;
import com.eveningoutpost.dexdrip.utilitymodels.Pref;

import org.junit.After;
import org.junit.Test;

/**
 * Tests for the Libre 3 collector's registration in {@link DexCollectionType}: the settings name it is stored under,
 * the Bluetooth and newer-collector sets, its NFC settings, its service, and its default sample period.
 *
 * @author Asbjørn Aarrestad - 2026.10
 */
public class DexCollectionTypeLibre3Test extends RobolectricTestWithConfig {

    @After
    public void tearDown() {
        Pref.removeItem(DexCollectionType.DEX_COLLECTION_METHOD);
    }

    // ================================================= Registration ==================================================

    /** The collector is stored under a name of its own, so selecting it survives a restart. */
    @Test
    public void getType_internalName_isLibre3Native() {
        // :: Act
        DexCollectionType type = DexCollectionType.getType("Libre3Native");

        // :: Verify
        assertThat(type).isEqualTo(DexCollectionType.Libre3Native);
    }

    /** Selected, it counts as a Bluetooth collector started the way the newer collectors are. */
    @Test
    public void selected_usesBluetoothAsNewerCollector() {
        // :: Setup
        DexCollectionType.setDexCollectionType(DexCollectionType.Libre3Native);

        // :: Act
        boolean bluetooth = DexCollectionType.hasBluetooth();
        boolean newer = DexCollectionType.isNewerCollector();

        // :: Verify
        assertThat(bluetooth).isTrue();
        assertThat(newer).isTrue();
        assertThat(DexCollectionType.hasLibre()).isFalse();
    }

    /** The collector runs in its own service. */
    @Test
    public void getCollectorServiceClass_libre3Native_isLibre3Service() {
        // :: Act
        Class<?> service = DexCollectionType.getCollectorServiceClass(DexCollectionType.Libre3Native);

        // :: Verify
        assertThat(service).isEqualTo(Libre3Service.class);
    }

    // ==================================================== NFC scan ===================================================

    /** A Libre 3 sensor is started by an NFC scan, so the NFC settings show for it as for the older Libre collectors. */
    @Test
    public void hasNfcScan_libre3Native_true() {
        // :: Act
        boolean nfc = DexCollectionType.hasNfcScan(DexCollectionType.Libre3Native);

        // :: Verify
        assertThat(nfc).isTrue();
    }

    /** The older Libre collectors keep their NFC settings. */
    @Test
    public void hasNfcScan_libreAlarm_true() {
        // :: Act
        boolean nfc = DexCollectionType.hasNfcScan(DexCollectionType.LibreAlarm);

        // :: Verify
        assertThat(nfc).isTrue();
    }

    /** A collector without an NFC sensor does not get the NFC settings. */
    @Test
    public void hasNfcScan_dexcomG5_false() {
        // :: Act
        boolean nfc = DexCollectionType.hasNfcScan(DexCollectionType.DexcomG5);

        // :: Verify
        assertThat(nfc).isFalse();
    }

    // ================================================= Sample period =================================================

    /** Without the one-minute setting the collector reports every five minutes, as the other Libre collectors do. */
    @Test
    public void getCollectorSamplePeriod_default_fiveMinutes() {
        // :: Act
        long period = DexCollectionType.getCollectorSamplePeriod(DexCollectionType.Libre3Native);

        // :: Verify
        assertThat(period).isEqualTo(300_000L);
    }
}
