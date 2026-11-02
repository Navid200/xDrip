package com.eveningoutpost.dexdrip.cgm.libre3;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.eveningoutpost.dexdrip.RobolectricTestWithConfig;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.robolectric.RuntimeEnvironment;
import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

import lwld.libre3.crypto.AppIdentity;
import lwld.libre3.util.Hex;

/**
 * Tests for {@link Libre3Credential}: a downloaded bundle is kept only once it unpacks against its digest, and a bad
 * download never replaces a good one. The bundle is the synthetic identity from the library's IdentityBundleTest,
 * so the digest is the fixture's own rather than the pinned release's.
 *
 * @author Asbjørn Aarrestad - 2026.10
 */
public class Libre3CredentialTest extends RobolectricTestWithConfig {

    private static final String SCALAR = "0000000000000000000000000000000000000000000000000000000000004444";
    private static final String CERT = "03000102030405060708090a0b0c0d0e0f1000005f92a79201000000000000000004a85b3aad"
            + "6f3a346d85523141cb434e1caf4c642b2b3cc952cb07a635cb6a1df1bcbdadb66bae8eb25eca92f8b08b67be02cc736150dd7ffcd3"
            + "f019c875f9ffde" + repeat('0', 128);

    // ===================================================== store =====================================================

    /** A bundle that unpacks is kept, and loads back as its identities. */
    @Test
    public void store_validBundle_loadsIdentities() throws Exception {
        // :: Setup
        byte[] json = bundle();
        Libre3Credential credential = new Libre3Credential(dir(), sha256(json));

        // :: Act
        credential.store(xz(json));

        // :: Verify
        List<AppIdentity> identities = credential.load();
        assertThat(identities).hasSize(1);
        assertThat(identities.get(0).label()).isEqualTo("a");
    }

    /** A download that does not unpack is refused and the earlier good bundle stays in place. */
    @Test
    public void store_invalidBundle_keepsEarlierValid() throws Exception {
        // :: Setup
        byte[] json = bundle();
        Libre3Credential credential = new Libre3Credential(dir(), sha256(json));
        credential.store(xz(json));
        byte[] truncated = Arrays.copyOf(xz(json), 20);

        // :: Act
        ThrowingRunnable store = () -> credential.store(truncated);

        // :: Verify
        assertThrows(GeneralSecurityException.class, store);
        assertThat(credential.load()).hasSize(1);
    }

    /** What one instance kept loads in the next, as after an app restart. */
    @Test
    public void load_newInstance_loadsStored() throws Exception {
        // :: Setup
        byte[] json = bundle();
        new Libre3Credential(dir(), sha256(json)).store(xz(json));

        // :: Act
        List<AppIdentity> identities = new Libre3Credential(dir(), sha256(json)).load();

        // :: Verify
        assertThat(identities).hasSize(1);
    }

    /** A kept credential is checked against the digest on every load, so a changed pin does not use it. */
    @Test
    public void load_otherDigest_empty() throws Exception {
        // :: Setup
        byte[] json = bundle();
        new Libre3Credential(dir(), sha256(json)).store(xz(json));

        // :: Act
        List<AppIdentity> identities = new Libre3Credential(dir(), sha256(new byte[1])).load();

        // :: Verify
        assertThat(identities).isEmpty();
    }

    /** Before anything was downloaded there are no identities, and no error. */
    @Test
    public void load_nothingStored_empty() throws Exception {
        // :: Setup
        Libre3Credential credential = new Libre3Credential(dir(), sha256(bundle()));

        // :: Act
        List<AppIdentity> identities = credential.load();

        // :: Verify
        assertThat(identities).isEmpty();
    }

    private static File dir() {
        return new File(RuntimeEnvironment.getApplication().getFilesDir(), "libre3");
    }

    private static byte[] bundle() throws Exception {
        JSONObject entry = new JSONObject()
                .put("label", "a")
                .put("privateKeyHex", SCALAR)
                .put("certificateHex", CERT);
        return new JSONObject()
                .put("format", 1)
                .put("curve", "P-256")
                .put("identities", new JSONArray().put(entry))
                .toString()
                .getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] xz(byte[] plain) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (XZOutputStream xz = new XZOutputStream(out, new LZMA2Options())) {
            xz.write(plain);
        }
        return out.toByteArray();
    }

    private static String sha256(byte[] data) throws Exception {
        return Hex.of(MessageDigest.getInstance("SHA-256").digest(data));
    }

    private static String repeat(char c, int count) {
        char[] chars = new char[count];
        Arrays.fill(chars, c);
        return new String(chars);
    }
}
