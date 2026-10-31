package lwld.libre3;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import lwld.libre3.data.Lifecycle;
import lwld.libre3.nfc.PatchInfo;
import lwld.libre3.pairing.ReceiverId;
import lwld.libre3.util.Hex;

/**
 * What is kept about one sensor between connections: its address and PIN from activation, the cycle read over NFC,
 * the receiver ID it was started or taken over with, the cached kAuth and the last reading. The PIN and kAuth are
 * secrets: {@link #toString()} leaves them out, and the JSON is for private app storage only. Ported from
 * RoundWhiteDiscKit {@code Libre3SensorState.swift} and {@code Libre3SensorStateLoader.swift} (MIT); the JSON keys
 * it shares with them are kept.
 *
 * @author Asbjørn Aarrestad - 2026.10
 */
public final class Libre3SensorState {

    private static final int PIN_SIZE = 4;
    private static final int KAUTH_SIZE = 16;
    /** Refused life counts kept behind the newest one; history is not asked for further back. */
    private static final int KEEP_REJECTED_MINUTES = 24 * 60;

    private final String serial;
    private final String bleAddress;
    private final byte[] blePin;
    private ReceiverId receiverId;
    private ReceiverId.Fold fold;
    /** 0 when unknown. */
    private long activationMs;
    private Integer warmupMinutes;
    private Integer wearMinutes;
    private Integer productType;
    private Integer generation;
    private PatchInfo.Region region;
    private String identityLabel;
    private byte[] kAuth;
    private Integer lastLifeCount;
    private Integer lastMgdl;
    private Set<Integer> rejectedLifeCounts = new HashSet<>();

    /** @throws IllegalArgumentException unless the PIN is 4 bytes */
    public Libre3SensorState(String serial, String bleAddress, byte[] blePin) {
        if (blePin == null || blePin.length != PIN_SIZE) {
            throw new IllegalArgumentException("BLE PIN must be " + PIN_SIZE + " bytes, got "
                    + (blePin == null ? "none" : blePin.length));
        }
        this.serial = serial;
        this.bleAddress = bleAddress;
        this.blePin = blePin.clone();
    }

    /** Keeps the warm-up, wear, product, generation and region the sensor reported over NFC. */
    public void applySensorCycle(PatchInfo info) {
        warmupMinutes = Math.max(0, info.warmupMinutes());
        wearMinutes = Math.max(0, info.wearMinutes());
        productType = info.productType();
        generation = info.generation();
        region = info.region();
    }

    public void updateLastGlucose(int lifeCount, int mgdl) {
        lastLifeCount = lifeCount;
        lastMgdl = mgdl;
    }

    /** The stored cycle; without one, the default warm-up and no wear end, as a reconnect without NFC has. */
    public Lifecycle lifecycle() {
        return new Lifecycle(warmupMinutes != null ? warmupMinutes : Lifecycle.DEFAULT_WARMUP_MINUTES,
                wearMinutes != null ? wearMinutes : Integer.MAX_VALUE);
    }

    public String toJson() {
        try {
            JSONObject json = new JSONObject()
                    .put("serialNumber", serial)
                    .put("bleAddress", bleAddress)
                    .put("blePIN", Hex.of(blePin))
                    .put("activationMs", activationMs);
            if (receiverId != null) {
                json.put("receiverID", receiverId.littleEndianHex());
            }
            if (fold != null) {
                json.put("fold", fold.name());
            }
            json.putOpt("warmupDurationMinutes", warmupMinutes);
            json.putOpt("wearDurationMinutes", wearMinutes);
            json.putOpt("productType", productType);
            json.putOpt("generation", generation);
            if (region != null) {
                json.put("region", region.name());
            }
            json.putOpt("identityLabel", identityLabel);
            if (kAuth != null) {
                json.put("kAuth", Hex.of(kAuth));
            }
            json.putOpt("lastGlucoseLifeCount", lastLifeCount);
            json.putOpt("lastGlucoseMgDL", lastMgdl);
            JSONArray rejected = new JSONArray();
            for (Integer lifeCount : rejectedLifeCounts) {
                rejected.put(lifeCount);
            }
            json.put("rejectedLifeCounts", rejected);
            return json.toString();
        } catch (JSONException e) {
            throw new IllegalStateException("Cannot write sensor state", e);
        }
    }

    /** @throws IllegalArgumentException when the text is not a stored state, or the PIN or kAuth has the wrong size */
    public static Libre3SensorState fromJson(String text) {
        try {
            JSONObject json = new JSONObject(text);
            Libre3SensorState state = new Libre3SensorState(json.getString("serialNumber"),
                    json.getString("bleAddress"), Hex.toBytes(json.getString("blePIN")));
            if (json.has("receiverID")) {
                state.receiverId = ReceiverId.fromLittleEndianHex(json.getString("receiverID"));
            }
            if (json.has("fold")) {
                state.fold = ReceiverId.Fold.valueOf(json.getString("fold"));
            }
            state.activationMs = json.optLong("activationMs", 0);
            state.warmupMinutes = optNonNegative(json, "warmupDurationMinutes");
            state.wearMinutes = optNonNegative(json, "wearDurationMinutes");
            state.productType = optInt(json, "productType");
            state.generation = optInt(json, "generation");
            if (json.has("region")) {
                state.region = PatchInfo.Region.valueOf(json.getString("region"));
            }
            state.identityLabel = json.has("identityLabel") ? json.getString("identityLabel") : null;
            if (json.has("kAuth")) {
                state.setKAuth(Hex.toBytes(json.getString("kAuth")));
            }
            state.lastLifeCount = optInt(json, "lastGlucoseLifeCount");
            state.lastMgdl = optInt(json, "lastGlucoseMgDL");
            JSONArray rejected = json.optJSONArray("rejectedLifeCounts");
            if (rejected != null) {
                for (int i = 0; i < rejected.length(); i++) {
                    state.rejectedLifeCounts.add(rejected.getInt(i));
                }
            }
            return state;
        } catch (JSONException e) {
            throw new IllegalArgumentException("Not a stored Libre 3 sensor state", e);
        }
    }

    private static Integer optInt(JSONObject json, String key) throws JSONException {
        return json.has(key) ? json.getInt(key) : null;
    }

    private static Integer optNonNegative(JSONObject json, String key) throws JSONException {
        Integer value = optInt(json, key);
        return value == null ? null : Math.max(0, value);
    }

    public String serial() {
        return serial;
    }

    public String bleAddress() {
        return bleAddress;
    }

    public byte[] blePin() {
        return blePin.clone();
    }

    public ReceiverId receiverId() {
        return receiverId;
    }

    public void setReceiverId(ReceiverId receiverId) {
        this.receiverId = receiverId;
    }

    public ReceiverId.Fold fold() {
        return fold;
    }

    public void setFold(ReceiverId.Fold fold) {
        this.fold = fold;
    }

    public long activationMs() {
        return activationMs;
    }

    public void setActivationMs(long activationMs) {
        this.activationMs = activationMs;
    }

    public Integer warmupMinutes() {
        return warmupMinutes;
    }

    public Integer wearMinutes() {
        return wearMinutes;
    }

    public Integer productType() {
        return productType;
    }

    public Integer generation() {
        return generation;
    }

    public PatchInfo.Region region() {
        return region;
    }

    public String identityLabel() {
        return identityLabel;
    }

    public void setIdentityLabel(String identityLabel) {
        this.identityLabel = identityLabel;
    }

    public byte[] kAuth() {
        return kAuth == null ? null : kAuth.clone();
    }

    /** @param kAuth 16 bytes, or null to forget it and force a first pair */
    public void setKAuth(byte[] kAuth) {
        if (kAuth != null && kAuth.length != KAUTH_SIZE) {
            throw new IllegalArgumentException("kAuth must be " + KAUTH_SIZE + " bytes");
        }
        this.kAuth = kAuth == null ? null : kAuth.clone();
    }

    public Integer lastLifeCount() {
        return lastLifeCount;
    }

    public Integer lastMgdl() {
        return lastMgdl;
    }

    /** Life counts whose reading was refused, so a backfill does not store them again. */
    public Set<Integer> rejectedLifeCounts() {
        return Collections.unmodifiableSet(rejectedLifeCounts);
    }

    public void setRejectedLifeCounts(Set<Integer> rejectedLifeCounts) {
        this.rejectedLifeCounts = new HashSet<>(rejectedLifeCounts);
    }

    /** Adds a refused life count and drops those more than a day older than the newest. */
    public void addRejectedLifeCount(int lifeCount) {
        rejectedLifeCounts.add(lifeCount);
        int oldest = Collections.max(rejectedLifeCounts) - KEEP_REJECTED_MINUTES;
        Iterator<Integer> it = rejectedLifeCounts.iterator();
        while (it.hasNext()) {
            if (it.next() < oldest) {
                it.remove();
            }
        }
    }

    @Override
    public String toString() {
        return "Libre3SensorState(serial=" + serial + ", address=" + bleAddress
                + ", receiver=" + (receiverId == null ? "none" : receiverId.littleEndianHex())
                + ", fold=" + fold + ", region=" + region + ", warmup=" + warmupMinutes + ", wear=" + wearMinutes
                + ", identity=" + identityLabel + ", paired=" + (kAuth != null) + ", last=" + lastLifeCount + ")";
    }
}
