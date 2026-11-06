package com.eveningoutpost.dexdrip.cgm.libre3;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.eveningoutpost.dexdrip.R;
import com.eveningoutpost.dexdrip.RobolectricTestWithConfig;
import com.eveningoutpost.dexdrip.cgm.libre3.Libre3AccountLookup.Failure;
import com.eveningoutpost.dexdrip.cgm.libre3.Libre3AccountLookup.LookupException;
import com.eveningoutpost.dexdrip.models.UserError;
import com.eveningoutpost.dexdrip.utilitymodels.Pref;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import xrwd.libre3.util.Reversed;

/**
 * Tests for {@link Libre3AccountLookup}: one login gives the account ID, a region answer is asked once more at that
 * region only, and anything else is a failure that names what went wrong without the password ever leaving for an
 * unexpected host. A found ID fills the account ID setting; a failure leaves it as it was, and the log shows only the
 * outcome. No test touches the network: {@link FakeLibreView} answers every request.
 *
 * @author Asbjørn Aarrestad - 2026.11
 */
public class Libre3AccountLookupTest extends RobolectricTestWithConfig {

    private static final String EMAIL = "someone@example.com";
    private static final String PASSWORD = "pass-word";
    private static final String ID = "ABCDEF01-2345-6789-ABCD-EF0123456789";
    private static final String SUCCESS = "{\"status\":0,\"data\":{\"user\":{\"id\":\"" + ID + "\"}}}";
    private static final String TOKEN = "tok-secret";

    @After
    public void tearDown() {
        Pref.removeItem(Libre3.PREF_ACCOUNT_ID);
    }

    // ==================================================== success ====================================================

    /** A login is one POST to the global host with the app headers and the credentials, and gives the ID. */
    @Test
    public void accountId_success_returnsLowerCasedId() throws Exception {
        // :: Setup
        FakeLibreView server = new FakeLibreView().answer(200, SUCCESS);

        // :: Act
        String id = lookup(server).accountId(EMAIL, PASSWORD);

        // :: Verify
        assertThat(id).isEqualTo("abcdef01-2345-6789-abcd-ef0123456789");
        assertThat(server.requests).hasSize(1);
        Request request = server.requests.get(0);
        assertThat(request.method()).isEqualTo("POST");
        assertThat(request.url().toString()).isEqualTo(Reversed.of("nigol/htua/ull/oi.weiverbil.ipa//:sptth"));
        assertThat(request.header("product")).isEqualTo("llu.android");
        assertThat(request.header("version")).isEqualTo("4.17.0");
        JSONObject body = new JSONObject(bodyOf(request));
        assertThat(body.getString("email")).isEqualTo(EMAIL);
        assertThat(body.getString("password")).isEqualTo(PASSWORD);
    }

    /** An account in another region is asked once more at that region's host. */
    @Test
    public void accountId_regionRedirect_asksThatRegion() throws Exception {
        // :: Setup
        FakeLibreView server = new FakeLibreView().answer(200, redirect("eu")).answer(200, SUCCESS);

        // :: Act
        String id = lookup(server).accountId(EMAIL, PASSWORD);

        // :: Verify
        assertThat(id).isEqualTo("abcdef01-2345-6789-abcd-ef0123456789");
        assertThat(server.requests).hasSize(2);
        assertThat(server.requests.get(1).url().host()).isEqualTo(Reversed.of("oi.weiverbil.ue-ipa"));
    }

    // ================================================ region failures ================================================

    /** A region that is not a LibreView region is never asked; the password goes nowhere else. */
    @Test
    public void accountId_unknownRegion_unavailable() {
        // :: Setup
        FakeLibreView server = new FakeLibreView().answer(200, redirect("evil.example"));

        // :: Act
        LookupException e = assertThrows(LookupException.class, () -> lookup(server).accountId(EMAIL, PASSWORD));

        // :: Verify
        assertThat(e.failure).isEqualTo(Failure.UNAVAILABLE);
        assertThat(e.getMessage()).contains("evil.example");
        assertThat(server.requests).hasSize(1);
    }

    /** A region that sends the login back to itself ends the lookup instead of asking again. */
    @Test
    public void accountId_redirectLoop_unavailable() {
        // :: Setup
        FakeLibreView server = new FakeLibreView().answer(200, redirect("eu")).answer(200, redirect("eu"));

        // :: Act
        LookupException e = assertThrows(LookupException.class, () -> lookup(server).accountId(EMAIL, PASSWORD));

        // :: Verify
        assertThat(e.failure).isEqualTo(Failure.UNAVAILABLE);
        assertThat(server.requests).hasSize(2);
    }

    // ================================================= login answers =================================================

    /** Status 2 is a login LibreView did not accept. */
    @Test
    public void accountId_status2_login() {
        // :: Act
        LookupException e = failure(new FakeLibreView().answer(200, "{\"status\":2}"));

        // :: Verify
        assertThat(e.failure).isEqualTo(Failure.LOGIN);
    }

    /** Status 4 is new terms to accept in the official app. */
    @Test
    public void accountId_status4_terms() {
        // :: Act
        LookupException e = failure(new FakeLibreView().answer(200, "{\"status\":4,\"data\":{}}"));

        // :: Verify
        assertThat(e.failure).isEqualTo(Failure.TERMS);
    }

    /** Any other status, such as an app version LibreView no longer accepts, is named so a log shows it. */
    @Test
    public void accountId_status920_unavailableWithStatus() {
        // :: Act
        LookupException e = failure(new FakeLibreView().answer(200, "{\"status\":920}"));

        // :: Verify
        assertThat(e.failure).isEqualTo(Failure.UNAVAILABLE);
        assertThat(e.getMessage()).contains("920");
    }

    /** A status that is not a number is not read as one. */
    @Test
    public void accountId_statusAsString_unavailable() {
        // :: Act
        LookupException e = failure(new FakeLibreView().answer(200, "{\"status\":\"2\"}"));

        // :: Verify
        assertThat(e.failure).isEqualTo(Failure.UNAVAILABLE);
    }

    // ================================================ broken answers =================================================

    /** A server error is a failure that names the HTTP code. */
    @Test
    public void accountId_http500_unavailable() {
        // :: Act
        LookupException e = failure(new FakeLibreView().answer(500, SUCCESS));

        // :: Verify
        assertThat(e.failure).isEqualTo(Failure.UNAVAILABLE);
        assertThat(e.getMessage()).contains("500");
    }

    /** An HTTP redirect is a failure, never a login. */
    @Test
    public void accountId_http302_unavailable() {
        // :: Setup
        FakeLibreView server = new FakeLibreView().answer(302, SUCCESS);

        // :: Act
        LookupException e = failure(server);

        // :: Verify
        assertThat(e.failure).isEqualTo(Failure.UNAVAILABLE);
        assertThat(server.requests).hasSize(1);
    }

    /** A page that is not JSON is a failure that carries none of the page. */
    @Test
    public void accountId_notJson_unavailable() {
        // :: Act
        LookupException e = failure(new FakeLibreView().answer(200, "<html>secret page</html>"));

        // :: Verify
        assertThat(e.failure).isEqualTo(Failure.UNAVAILABLE);
        assertThat(e.getCause()).isNull();
        assertThat(e.getMessage()).doesNotContain("secret");
    }

    /** An ID that is not a UUID is not an account ID. */
    @Test
    public void accountId_idNotUuid_unavailable() {
        // :: Act
        LookupException e = failure(new FakeLibreView()
                .answer(200, "{\"status\":0,\"data\":{\"user\":{\"id\":\"42\"}}}"));

        // :: Verify
        assertThat(e.failure).isEqualTo(Failure.UNAVAILABLE);
    }

    /** No connection is a failure, not a crash. */
    @Test
    public void accountId_ioException_unavailable() {
        // :: Act
        LookupException e = failure(new FakeLibreView().fail(new IOException("offline")));

        // :: Verify
        assertThat(e.failure).isEqualTo(Failure.UNAVAILABLE);
        assertThat(e.getCause()).isNull();
    }

    // ===================================================== client ====================================================

    /** The lookup's client never follows an HTTP redirect, so the password is sent only to the hosts it chose. */
    @Test
    public void client_neverFollowsRedirects() {
        // :: Act
        OkHttpClient client = new Libre3AccountLookup(new OkHttpClient()).client();

        // :: Verify
        assertThat(client.followRedirects()).isFalse();
        assertThat(client.followSslRedirects()).isFalse();
    }

    // ================================================= fillAccountId =================================================

    /** A found ID is saved as the account ID. */
    @Test
    public void fillAccountId_success_savesIdAndSaysDone() {
        // :: Act
        int message = Libre3AccountLookup.fillAccountId(lookup(new FakeLibreView().answer(200, SUCCESS)), EMAIL,
                PASSWORD);

        // :: Verify
        assertThat(message).isEqualTo(R.string.libre3_lookup_done);
        assertThat(Pref.getString(Libre3.PREF_ACCOUNT_ID, "")).isEqualTo("abcdef01-2345-6789-abcd-ef0123456789");
    }

    /** A login that is not accepted leaves the account ID that was there. */
    @Test
    public void fillAccountId_failure_keepsExistingId() {
        // :: Setup
        Pref.setString(Libre3.PREF_ACCOUNT_ID, "existing");

        // :: Act
        int message = Libre3AccountLookup.fillAccountId(lookup(new FakeLibreView().answer(200, "{\"status\":2}")),
                EMAIL, PASSWORD);

        // :: Verify
        assertThat(message).isEqualTo(R.string.libre3_lookup_bad_login);
        assertThat(Pref.getString(Libre3.PREF_ACCOUNT_ID, "")).isEqualTo("existing");
    }

    /** New terms send the user to the official app. */
    @Test
    public void fillAccountId_terms_saysTerms() {
        // :: Act
        int message = Libre3AccountLookup.fillAccountId(lookup(new FakeLibreView().answer(200, "{\"status\":4}")),
                EMAIL, PASSWORD);

        // :: Verify
        assertThat(message).isEqualTo(R.string.libre3_lookup_terms);
    }

    /** Anything else sends the user to the settings to enter the ID by hand. */
    @Test
    public void fillAccountId_unavailable_saysFailed() {
        // :: Act
        int message = Libre3AccountLookup.fillAccountId(lookup(new FakeLibreView().fail(new IOException("offline"))),
                EMAIL, PASSWORD);

        // :: Verify
        assertThat(message).isEqualTo(R.string.libre3_lookup_failed);
        assertThat(Pref.getString(Libre3.PREF_ACCOUNT_ID, "")).isEmpty();
    }

    /** Neither a found ID nor a refused login puts the email, password, token or ID in xDrip's log. */
    @Test
    public void fillAccountId_neverLogsSecrets() {
        // :: Setup
        String success = "{\"status\":0,\"data\":{\"user\":{\"id\":\"" + ID + "\"},"
                + "\"authTicket\":{\"token\":\"" + TOKEN + "\"}}}";

        // :: Act
        Libre3AccountLookup.fillAccountId(lookup(new FakeLibreView().answer(200, success)), EMAIL, PASSWORD);
        Libre3AccountLookup.fillAccountId(lookup(new FakeLibreView().answer(200, "{\"status\":2}")), EMAIL,
                PASSWORD);

        // :: Verify
        boolean logged = false;
        for (UserError row : UserError.all()) {
            String text = (row.shortError + " " + row.message).toLowerCase();
            logged |= text.contains("account id lookup");
            assertThat(text).doesNotContain(EMAIL);
            assertThat(text).doesNotContain(PASSWORD);
            assertThat(text).doesNotContain(TOKEN);
            assertThat(text).doesNotContain(ID.toLowerCase());
        }
        assertThat(logged).isTrue();
    }

    // ==================================================== helpers ====================================================

    static Libre3AccountLookup lookup(FakeLibreView server) {
        return new Libre3AccountLookup(new OkHttpClient.Builder().addInterceptor(server).build());
    }

    private static LookupException failure(FakeLibreView server) {
        return assertThrows(LookupException.class, () -> lookup(server).accountId(EMAIL, PASSWORD));
    }

    private static String redirect(String region) {
        return "{\"status\":0,\"data\":{\"redirect\":true,\"region\":\"" + region + "\"}}";
    }

    private static String bodyOf(Request request) throws IOException {
        Buffer buffer = new Buffer();
        request.body().writeTo(buffer);
        return buffer.readUtf8();
    }

    /** LibreView as a queue of canned answers; it records every request and never touches the network. */
    static final class FakeLibreView implements Interceptor {

        final List<Request> requests = new ArrayList<>();
        private final Deque<Object> answers = new ArrayDeque<>();

        FakeLibreView answer(int code, String body) {
            answers.add(new Object[]{code, body});
            return this;
        }

        FakeLibreView fail(IOException e) {
            answers.add(e);
            return this;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            requests.add(chain.request());
            Object next = answers.remove();
            if (next instanceof IOException) {
                throw (IOException) next;
            }
            Object[] answer = (Object[]) next;
            return new Response.Builder()
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .code((Integer) answer[0])
                    .message("")
                    .body(ResponseBody.create((String) answer[1], null))
                    .build();
        }
    }
}
