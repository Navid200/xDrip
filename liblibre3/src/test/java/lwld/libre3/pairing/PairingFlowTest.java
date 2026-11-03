package lwld.libre3.pairing;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lwld.libre3.ble.Gatt;
import lwld.libre3.crypto.AesCcm;
import lwld.libre3.crypto.AppIdentity;
import lwld.libre3.crypto.SensorCert;
import lwld.libre3.util.Hex;

/**
 * Tests for the pairing handshake against a fake sensor that plays the sensor side with its own synthetic keys and
 * derives kAuth from its end of the two ECDH exchanges. Ported from RoundWhiteDiscKit 7dd6396
 * {@code PublicSmokeTests.swift} and the command order in {@code PairingFlow.swift}.
 *
 * @author Asbjørn Aarrestad - 2026.10
 */
public class PairingFlowTest {

    /** The synthetic app identity from AppIdentityTest. */
    private static final String IDENTITY_SCALAR = "0000000000000000000000000000000000000000000000000000000000004444";
    private static final String IDENTITY_CERT = "03000102030405060708090a0b0c0d0e0f1000005f92a79201000000000000000004a85b"
            + "3aad6f3a346d85523141cb434e1caf4c642b2b3cc952cb07a635cb6a1df1bcbdadb66bae8eb25eca92f8b08b67be02cc736150dd"
            + "7ffcd3f019c875f9ffde" + zeros(128);

    private static final byte[] BLE_PIN = Hex.toBytes("3225ec72");
    private static final byte[] K_ENC = Hex.toBytes("505152535455565758595a5b5c5d5e5f");
    private static final byte[] IV_ENC = Hex.toBytes("6061626364656667");

    /** The full first-pair command order RoundWhiteDiscKit's PairingFlow writes. */
    private static final List<Integer> FULL_ORDER = Arrays.asList(
            0x01, 0x02, 0x03, 0x09, 0x0d, 0x0e, 0x11, 0x08);

    // ================================================== First pair ===================================================

    /** A first pair writes the full command order and ends with the session keys the sensor issued. */
    @Test
    public void firstPair_runsFullSequence_andReturnsSensorKeys() throws Exception {
        // :: Setup
        FakeSensor sensor = new FakeSensor();
        PairingFlow flow = flow(sensor, "0000000000000000000000000000000000000000000000000000000000002222");

        // :: Act
        PairingResult result = flow.firstPair(BLE_PIN);

        // :: Verify
        assertThat(sensor.commands).isEqualTo(FULL_ORDER);
        assertThat(result.kAuth()).isEqualTo(sensor.kAuth);
        assertThat(result.kEnc()).isEqualTo(K_ENC);
        assertThat(result.ivEnc()).isEqualTo(IV_ENC);
        assertThat(result.usedCache()).isFalse();
        assertThat(sensor.tailSeen).isEqualTo(BLE_PIN);
    }

    /** The phone certificate and padded ephemeral key go out as offset-prefixed 18-byte fragments. */
    @Test
    public void firstPair_writesCertificateAndEphemeralFragmented() throws Exception {
        // :: Setup
        FakeSensor sensor = new FakeSensor();
        PairingFlow flow = flow(sensor, "0000000000000000000000000000000000000000000000000000000000002222");

        // :: Act
        flow.firstPair(BLE_PIN);

        // :: Verify
        assertThat(sensor.writeCount(Gatt.SEC_CERT_DATA)).isEqualTo(9 + 4);
        assertThat(sensor.writeCount(Gatt.SEC_CHALLENGE_DATA)).isEqualTo(3);
        assertThat(sensor.phoneCert).isEqualTo(Hex.toBytes(IDENTITY_CERT));
    }

    /** The result's text names the stage reached and none of the keys. */
    @Test
    public void firstPair_toString_hidesKeys() throws Exception {
        // :: Setup
        FakeSensor sensor = new FakeSensor();
        PairingFlow flow = flow(sensor, "0000000000000000000000000000000000000000000000000000000000002222");

        // :: Act
        PairingResult result = flow.firstPair(BLE_PIN);

        // :: Verify
        String text = result.toString();
        assertThat(text).contains(result.lastStage());
        assertThat(text).doesNotContain(Hex.of(result.kAuth()));
        assertThat(text).doesNotContain(Hex.of(K_ENC));
        assertThat(text).doesNotContain(Hex.of(IV_ENC));
    }

    /** A first pair hands back the sensor's static key, so the collector can hold later pairings to it. */
    @Test
    public void firstPair_returnsSensorStaticKey() throws Exception {
        // :: Setup
        FakeSensor sensor = new FakeSensor();
        PairingFlow flow = flow(sensor, "0000000000000000000000000000000000000000000000000000000000002222");

        // :: Act
        PairingResult result = flow.firstPair(BLE_PIN);

        // :: Verify
        assertThat(result.sensorKey65()).isEqualTo(sensor.staticKey65());
    }

    /** A sensor showing the key it showed before pairs as usual. */
    @Test
    public void firstPair_sameKeyAsBefore_pairs() throws Exception {
        // :: Setup
        FakeSensor sensor = new FakeSensor();
        PairingFlow flow = flow(sensor, "0000000000000000000000000000000000000000000000000000000000002222");

        // :: Act
        PairingResult result = flow.firstPair(BLE_PIN, sensor.staticKey65());

        // :: Verify
        assertThat(result.kEnc()).isEqualTo(K_ENC);
        assertThat(sensor.tailSeen).isEqualTo(BLE_PIN);
    }

    /**
     * A device showing another key than the sensor paired before is refused before the challenge, so it never gets
     * the PIN.
     */
    @Test
    public void firstPair_otherKeyThanBefore_throwsBeforePinIsSent() {
        // :: Setup
        FakeSensor sensor = new FakeSensor();
        PairingFlow flow = flow(sensor, "0000000000000000000000000000000000000000000000000000000000002222");
        byte[] before = sensor.staticKey65();
        before[64] ^= 1;

        // :: Act
        ThrowingRunnable pair = () -> flow.firstPair(BLE_PIN, before);

        // :: Verify
        GeneralSecurityException e = assertThrows(GeneralSecurityException.class, pair);
        assertThat(e).hasMessageThat().contains("key");
        assertThat(sensor.commands).doesNotContain(0x11);
        assertThat(sensor.tailSeen).isNull();
    }

    /** A reconnect exchanges no certificate, so it has no sensor key to hand back. */
    @Test
    public void reconnect_returnsNoSensorKey() throws Exception {
        // :: Setup
        FakeSensor sensor = new FakeSensor();
        byte[] cached = Hex.toBytes("404142434445464748494a4b4c4d4e4f");
        sensor.acceptedKAuth = cached;
        PairingFlow flow = flow(sensor, "0000000000000000000000000000000000000000000000000000000000002222");

        // :: Act
        PairingResult result = flow.reconnect(cached, BLE_PIN);

        // :: Verify
        assertThat(result.sensorKey65()).isNull();
    }

    /** A PIN that is not four bytes is rejected before anything reaches the sensor. */
    @Test
    public void firstPair_pinNotFourBytes_throwsBeforeAnyWrite() {
        // :: Setup
        FakeSensor sensor = new FakeSensor();
        PairingFlow flow = flow(sensor, "0000000000000000000000000000000000000000000000000000000000002222");

        // :: Act
        ThrowingRunnable pair = () -> flow.firstPair(new byte[]{0x01, 0x02, 0x03});

        // :: Verify
        assertThrows(IllegalArgumentException.class, pair);
        assertThat(sensor.commands).isEmpty();
        assertThat(sensor.writes).isEmpty();
    }

    /** A sensor that goes quiet surfaces as an IOException that names the stage, with no partial result. */
    @Test
    public void firstPair_notifyTimeout_throwsNamingStage() {
        // :: Setup
        FakeSensor sensor = new FakeSensor();
        sensor.silentAfter = 0x0e;
        PairingFlow flow = flow(sensor, "0000000000000000000000000000000000000000000000000000000000002222");

        // :: Act
        ThrowingRunnable pair = () -> flow.firstPair(BLE_PIN);

        // :: Verify
        IOException e = assertThrows(IOException.class, pair);
        assertThat(e).hasMessageThat().contains("EphemeralReady");
        assertThat(sensor.commands).doesNotContain(0x11);
    }

    /** A command answered with the wrong opcode stops the handshake at that stage. */
    @Test
    public void firstPair_certificateRejected_throwsNamingStage() {
        // :: Setup
        FakeSensor sensor = new FakeSensor();
        sensor.certificateAnswer = 0x05;
        PairingFlow flow = flow(sensor, "0000000000000000000000000000000000000000000000000000000000002222");

        // :: Act
        ThrowingRunnable pair = () -> flow.firstPair(BLE_PIN);

        // :: Verify
        IOException e = assertThrows(IOException.class, pair);
        assertThat(e).hasMessageThat().contains("CertificateAccepted");
        assertThat(sensor.commands).containsExactly(0x01, 0x02, 0x03).inOrder();
    }

    /** A Phase 6 reply the phone's kAuth cannot open is a security failure, not a result. */
    @Test
    public void firstPair_phase6UnderOtherKey_throwsSecurityException() {
        // :: Setup
        FakeSensor sensor = new FakeSensor();
        sensor.phase6KeyOverride = Hex.toBytes("000102030405060708090a0b0c0d0e0f");
        PairingFlow flow = flow(sensor, "0000000000000000000000000000000000000000000000000000000000002222");

        // :: Act
        ThrowingRunnable pair = () -> flow.firstPair(BLE_PIN);

        // :: Verify
        assertThrows(GeneralSecurityException.class, pair);
    }

    // =============================================== Cached reconnect ================================================

    /** A cached reconnect starts at StartAuthorization and never sends a certificate or ephemeral key. */
    @Test
    public void reconnect_cachedAccepted_skipsCertificateExchange() throws Exception {
        // :: Setup
        FakeSensor sensor = new FakeSensor();
        byte[] kAuth = Hex.toBytes("404142434445464748494a4b4c4d4e4f");
        sensor.acceptedKAuth = kAuth;
        PairingFlow flow = flow(sensor, "0000000000000000000000000000000000000000000000000000000000002222");

        // :: Act
        PairingResult result = flow.reconnect(kAuth, BLE_PIN);

        // :: Verify
        assertThat(sensor.commands).containsExactly(0x11, 0x08).inOrder();
        assertThat(sensor.writeCount(Gatt.SEC_CERT_DATA)).isEqualTo(0);
        assertThat(sensor.writeCount(Gatt.SEC_CHALLENGE_DATA)).isEqualTo(3);
        assertThat(result.usedCache()).isTrue();
        assertThat(result.kAuth()).isEqualTo(kAuth);
        assertThat(result.kEnc()).isEqualTo(K_ENC);
        assertThat(result.ivEnc()).isEqualTo(IV_ENC);
        assertThat(sensor.tailSeen).isEqualTo(BLE_PIN);
    }

    /**
     * A sensor that refuses StartAuthorization rejects the cached key: no full handshake on the same connection, so
     * the caller can drop the key and pair afresh next time.
     */
    @Test
    public void reconnect_startAuthorizationRefused_throwsWithoutFullSequence() {
        // :: Setup
        FakeSensor sensor = new FakeSensor();
        PairingFlow flow = flow(sensor, "0000000000000000000000000000000000000000000000000000000000002222");

        // :: Act
        ThrowingRunnable reconnect = () -> flow.reconnect(Hex.toBytes("404142434445464748494a4b4c4d4e4f"), BLE_PIN);

        // :: Verify
        GeneralSecurityException e = assertThrows(GeneralSecurityException.class, reconnect);
        assertThat(e).hasMessageThat().contains("ChallengeLoadDone");
        assertThat(sensor.commands).containsExactly(0x11);
    }

    /** A cached key the sensor no longer holds is rejected without a full handshake on the same connection. */
    @Test
    public void reconnect_staleKey_throwsWithoutFullSequence() {
        // :: Setup
        FakeSensor sensor = new FakeSensor();
        sensor.acceptedKAuth = Hex.toBytes("404142434445464748494a4b4c4d4e4f");
        PairingFlow flow = flow(sensor, "0000000000000000000000000000000000000000000000000000000000002222");

        // :: Act
        ThrowingRunnable reconnect = () -> flow.reconnect(Hex.toBytes("ffeeddccbbaa99887766554433221100"), BLE_PIN);

        // :: Verify
        assertThrows(GeneralSecurityException.class, reconnect);
        assertThat(sensor.commands).containsExactly(0x11, 0x08).inOrder();
    }

    /** A link failure on the cached path is not a rejection: it surfaces and the cache is not abandoned. */
    @Test
    public void reconnect_linkFailure_doesNotFallBack() {
        // :: Setup
        FakeSensor sensor = new FakeSensor();
        sensor.acceptedKAuth = Hex.toBytes("404142434445464748494a4b4c4d4e4f");
        sensor.silentAfter = 0x11;
        PairingFlow flow = flow(sensor, "0000000000000000000000000000000000000000000000000000000000002222");

        // :: Act
        ThrowingRunnable reconnect = () -> flow.reconnect(sensor.acceptedKAuth, BLE_PIN);

        // :: Verify
        IOException e = assertThrows(IOException.class, reconnect);
        assertThat(e).hasMessageThat().contains("ChallengeLoadDone");
        assertThat(sensor.commands).containsExactly(0x11);
    }

    /** A PIN that is not four bytes is rejected before anything reaches the sensor. */
    @Test
    public void reconnect_pinNotFourBytes_throwsBeforeAnyWrite() {
        // :: Setup
        FakeSensor sensor = new FakeSensor();
        PairingFlow flow = flow(sensor, "0000000000000000000000000000000000000000000000000000000000002222");

        // :: Act
        ThrowingRunnable reconnect = () -> flow.reconnect(Hex.toBytes("404142434445464748494a4b4c4d4e4f"),
                new byte[5]);

        // :: Verify
        assertThrows(IllegalArgumentException.class, reconnect);
        assertThat(sensor.writes).isEmpty();
    }

    private static PairingFlow flow(FakeSensor sensor, String phoneEphemeralScalar) {
        try {
            AppIdentity identity = AppIdentity.of("test", Hex.toBytes(IDENTITY_SCALAR), Hex.toBytes(IDENTITY_CERT));
            sensor.phoneStatic65 = Arrays.copyOfRange(Hex.toBytes(IDENTITY_CERT), 33, 33 + 65);
            AsymmetricCipherKeyPair ephemeral = EphemeralExchange.fromScalar(Hex.toBytes(phoneEphemeralScalar));
            return new PairingFlow(sensor, identity, () -> ephemeral);
        } catch (GeneralSecurityException e) {
            throw new AssertionError(e);
        }
    }

    private static String zeros(int count) {
        char[] chars = new char[count];
        Arrays.fill(chars, '0');
        return new String(chars);
    }

    /**
     * Plays the sensor side of the handshake. Commands are answered on the command characteristic; certificate,
     * ephemeral key, R1 and Phase 6 go out as sequence-numbered notifies of up to 19 bytes.
     */
    private static final class FakeSensor implements Libre3Transport {

        private static final byte[] R1 = Hex.toBytes("101112131415161718191a1b1c1d1e1f");
        private static final byte[] R1_NONCE = Hex.toBytes("2104000080c4b0");
        private static final byte[] PHASE6_NONCE = Hex.toBytes("220400007f438e");

        final List<Integer> commands = new ArrayList<>();
        final List<UUID> writes = new ArrayList<>();
        final Map<UUID, ByteArrayOutputStream> received = new HashMap<>();
        final Map<UUID, Deque<byte[]>> notifies = new HashMap<>();

        byte[] phoneStatic65;
        byte[] acceptedKAuth;
        byte[] phase6KeyOverride;
        int certificateAnswer = 0x04;
        int silentAfter = -1;

        byte[] phoneCert;
        byte[] kAuth;
        byte[] tailSeen;

        private final AsymmetricCipherKeyPair staticKey;
        private final AsymmetricCipherKeyPair ephemeralKey;
        private boolean fullHandshake;
        private boolean challengeSent;

        FakeSensor() {
            try {
                staticKey = EphemeralExchange.fromScalar(Hex.toBytes(
                        "00000000000000000000000000000000000000000000000000000000000a1b2c"));
                ephemeralKey = EphemeralExchange.fromScalar(Hex.toBytes(
                        "00000000000000000000000000000000000000000000000000000000000d3e4f"));
            } catch (GeneralSecurityException e) {
                throw new AssertionError(e);
            }
        }

        byte[] staticKey65() {
            return EphemeralExchange.publicKey65(staticKey);
        }

        int writeCount(UUID characteristic) {
            int count = 0;
            for (UUID uuid : writes) {
                if (uuid.equals(characteristic)) {
                    count++;
                }
            }
            return count;
        }

        @Override
        public void write(UUID characteristic, byte[] value) throws IOException {
            writes.add(characteristic);
            if (!characteristic.equals(Gatt.SEC_COMMAND_RESPONSE)) {
                int offset = (value[0] & 0xff) | (value[1] & 0xff) << 8;
                ByteArrayOutputStream buffer = received.computeIfAbsent(characteristic,
                        k -> new ByteArrayOutputStream());
                assertThat(offset).isEqualTo(buffer.size());
                buffer.write(value, 2, value.length - 2);
                return;
            }
            assertThat(value).hasLength(1);
            int command = value[0] & 0xff;
            commands.add(command);
            if (command == silentAfter) {
                return;
            }
            try {
                answer(command);
            } catch (GeneralSecurityException e) {
                throw new AssertionError(e);
            }
        }

        @Override
        public byte[] awaitNotify(UUID characteristic, long timeoutMs) throws IOException {
            Deque<byte[]> queue = notifies.get(characteristic);
            if (queue == null || queue.isEmpty()) {
                throw new IOException("Timed out after " + timeoutMs + " ms");
            }
            return queue.removeFirst();
        }

        private void answer(int command) throws GeneralSecurityException {
            switch (command) {
                case 0x01:
                    fullHandshake = true;
                    challengeSent = false;
                    received.clear();
                    break;
                case 0x03:
                    phoneCert = take(Gatt.SEC_CERT_DATA);
                    respond(certificateAnswer);
                    break;
                case 0x09:
                    respond(0x0a);
                    byte[] cert = new byte[SensorCert.SIZE];
                    System.arraycopy(EphemeralExchange.publicKey65(staticKey), 0, cert, 11, 65);
                    notify(Gatt.SEC_CERT_DATA, cert);
                    break;
                case 0x0e:
                    byte[] phoneEphemeral = Arrays.copyOf(take(Gatt.SEC_CERT_DATA), 65);
                    kAuth = deriveKAuth(phoneEphemeral);
                    respond(0x0f);
                    notify(Gatt.SEC_CERT_DATA, EphemeralExchange.publicKey65(ephemeralKey));
                    break;
                case 0x11:
                    if (!fullHandshake && acceptedKAuth == null) {
                        respond(0x0b);
                        break;
                    }
                    if (!fullHandshake) {
                        kAuth = acceptedKAuth;
                    }
                    respond(0x08);
                    notify(Gatt.SEC_CHALLENGE_DATA, concat(R1, R1_NONCE));
                    challengeSent = true;
                    break;
                case 0x08:
                    if (!challengeSent) {
                        respond(0x0b);
                        break;
                    }
                    byte[] phase5 = take(Gatt.SEC_CHALLENGE_DATA);
                    byte[] phoneR2 = new byte[16];
                    try {
                        byte[] plain = AesCcm.decrypt(kAuth, R1_NONCE, Arrays.copyOf(phase5, 40), 4);
                        assertThat(Arrays.copyOf(plain, 16)).isEqualTo(R1);
                        phoneR2 = Arrays.copyOfRange(plain, 16, 32);
                        tailSeen = Arrays.copyOfRange(plain, 32, 36);
                    } catch (GeneralSecurityException wrongKey) {
                        // the sensor answers anyway; the phone cannot open what follows
                    }
                    respond(0x08);
                    byte[] key = phase6KeyOverride != null ? phase6KeyOverride : kAuth;
                    byte[] sealed = AesCcm.encrypt(key, PHASE6_NONCE, concat(phoneR2, R1, K_ENC, IV_ENC), 4);
                    notify(Gatt.SEC_CHALLENGE_DATA, concat(sealed, PHASE6_NONCE));
                    break;
                default:
                    break;
            }
        }

        private byte[] deriveKAuth(byte[] phoneEphemeral65) throws GeneralSecurityException {
            byte[] ze = EphemeralExchange.sharedSecret((ECPrivateKeyParameters) ephemeralKey.getPrivate(),
                    EphemeralExchange.parsePeer(phoneEphemeral65));
            byte[] zs = EphemeralExchange.sharedSecret((ECPrivateKeyParameters) staticKey.getPrivate(),
                    EphemeralExchange.parsePeer(phoneStatic65));
            SHA256Digest sha = new SHA256Digest();
            sha.update(new byte[]{0, 0, 0, 1}, 0, 4);
            sha.update(ze, 0, ze.length);
            sha.update(zs, 0, zs.length);
            byte[] digest = new byte[32];
            sha.doFinal(digest, 0);
            return Arrays.copyOf(digest, 16);
        }

        private byte[] take(UUID characteristic) {
            ByteArrayOutputStream buffer = received.remove(characteristic);
            return buffer == null ? new byte[0] : buffer.toByteArray();
        }

        private void respond(int opcode) {
            queue(Gatt.SEC_COMMAND_RESPONSE).add(new byte[]{(byte) opcode, 0x17});
        }

        private void notify(UUID characteristic, byte[] message) {
            int sequence = 0;
            for (int offset = 0; offset < message.length; offset += 19, sequence++) {
                int length = Math.min(19, message.length - offset);
                byte[] chunk = new byte[1 + length];
                chunk[0] = (byte) sequence;
                System.arraycopy(message, offset, chunk, 1, length);
                queue(characteristic).add(chunk);
            }
        }

        private Deque<byte[]> queue(UUID characteristic) {
            return notifies.computeIfAbsent(characteristic, k -> new ArrayDeque<>());
        }

        private static byte[] concat(byte[]... parts) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            for (byte[] part : parts) {
                out.write(part, 0, part.length);
            }
            return out.toByteArray();
        }
    }
}
