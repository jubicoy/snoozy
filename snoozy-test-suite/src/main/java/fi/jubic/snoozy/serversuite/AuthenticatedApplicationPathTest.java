package fi.jubic.snoozy.serversuite;

import fi.jubic.snoozy.Server;
import fi.jubic.snoozy.auth.Authentication;
import fi.jubic.snoozy.auth.UserPrincipal;
import fi.jubic.snoozy.test.TestAuthenticatedApplication;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.junit.jupiter.api.Test;

import javax.annotation.security.PermitAll;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static fi.jubic.snoozy.test.TestUtil.withServer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface AuthenticatedApplicationPathTest<T extends Server> extends BaseTest<T> {
    @Test
    default void annotatedAuthApplicationServedWithPrefix() throws Exception {
        withServer(
                instance(),
                new AnnotatedApplication(),
                (hostname, port) -> {
                    OkHttpClient client = new OkHttpClient();

                    {
                        okhttp3.Response response = client.newCall(
                                new Request.Builder()
                                        .url(String.format("http://%s:%d/prefix", hostname, port))
                                        .get()
                                        .build()
                        ).execute();

                        assertEquals(200, response.code());
                        assertEquals("TEST", Objects.requireNonNull(response.body()).string());

                        response.close();
                    }

                    {
                        okhttp3.Response response = client.newCall(
                                new Request.Builder()
                                        .url(String.format("http://%s:%d/", hostname, port))
                                        .get()
                                        .build()
                        ).execute();

                        assertEquals(404, response.code());

                        response.close();
                    }
                }
        );
    }

    @Test
    default void annotatedAuthApplicationServedWithLeadingSlashPrefix() throws Exception {
        withServer(
                instance(),
                new LeadingSlashAnnotatedApplication(),
                (hostname, port) -> {
                    OkHttpClient client = new OkHttpClient();

                    {
                        okhttp3.Response response = client.newCall(
                                new Request.Builder()
                                        .url(String.format("http://%s:%d/prefix", hostname, port))
                                        .get()
                                        .build()
                        ).execute();

                        assertEquals(200, response.code());
                        assertEquals("TEST", Objects.requireNonNull(response.body()).string());

                        response.close();
                    }

                    {
                        okhttp3.Response response = client.newCall(
                                new Request.Builder()
                                        .url(String.format("http://%s:%d/", hostname, port))
                                        .get()
                                        .build()
                        ).execute();

                        assertEquals(404, response.code());

                        response.close();
                    }
                }
        );
    }

    @Test
    default void annotatedAuthApplicationServedWithLongPrefix() throws Exception {
        withServer(
                instance(),
                new LongPathAnnotatedApplication(),
                (hostname, port) -> {
                    OkHttpClient client = new OkHttpClient();

                    {
                        okhttp3.Response response = client.newCall(
                                new Request.Builder()
                                        .url(String.format("http://%s:%d/long/prefix", hostname, port))
                                        .get()
                                        .build()
                        ).execute();

                        assertEquals(200, response.code());
                        assertEquals("TEST", Objects.requireNonNull(response.body()).string());

                        response.close();
                    }

                    {
                        okhttp3.Response response = client.newCall(
                                new Request.Builder()
                                        .url(String.format("http://%s:%d/", hostname, port))
                                        .get()
                                        .build()
                        ).execute();

                        assertEquals(404, response.code());

                        response.close();
                    }
                }
        );
    }

    @Test
    default void plainAuthApplicationServedWithoutPrefix() throws Exception {
        withServer(
                instance(),
                new PlainApplication(),
                (hostname, port) -> {
                    OkHttpClient client = new OkHttpClient();

                    {
                        okhttp3.Response response = client.newCall(
                                new Request.Builder()
                                        .url(String.format("http://%s:%d/", hostname, port))
                                        .get()
                                        .build()
                        ).execute();

                        assertEquals(200, response.code());
                        assertEquals("TEST", Objects.requireNonNull(response.body()).string());

                        response.close();
                    }

                    {
                        okhttp3.Response response = client.newCall(
                                new Request.Builder()
                                        .url(String.format("http://%s:%d/prefix", hostname, port))
                                        .get()
                                        .build()
                        ).execute();

                        assertEquals(404, response.code());

                        response.close();
                    }
                }
        );
    }

    class User implements UserPrincipal {
        @Override
        public String getName() {
            return "Tester";
        }

        @Override
        public String getRole() {
            return "USER";
        }
    }

    static Authentication<User> getAuthentication() {
        return Authentication.<User>builder()
                .setAuthenticator(token -> Optional.of(new User()))
                .setAuthorizer((user, role) -> true)
                .setTokenParser(req -> Optional.of("TOKEN"))
                .setUserClass(User.class)
                .build();
    }

    @ApplicationPath("prefix")
    class AnnotatedApplication extends TestAuthenticatedApplication<User> {
        @Override
        public Set<Object> getSingletons() {
            return Collections.singleton(new ApplicationPathTest.ResourceClass());
        }

        @Override
        public Authentication<User> getAuthentication() {
            return AuthenticatedApplicationPathTest.getAuthentication();
        }
    }

    class PlainApplication extends TestAuthenticatedApplication<User> {
        @Override
        public Set<Object> getSingletons() {
            return Collections.singleton(new ApplicationPathTest.ResourceClass());
        }

        @Override
        public Authentication<User> getAuthentication() {
            return AuthenticatedApplicationPathTest.getAuthentication();
        }
    }

    @ApplicationPath("/prefix")
    class LeadingSlashAnnotatedApplication extends TestAuthenticatedApplication<User> {
        @Override
        public Set<Object> getSingletons() {
            return Collections.singleton(new ApplicationPathTest.ResourceClass());
        }

        @Override
        public Authentication<User> getAuthentication() {
            return AuthenticatedApplicationPathTest.getAuthentication();
        }
    }

    @ApplicationPath("/long/prefix")
    class LongPathAnnotatedApplication extends TestAuthenticatedApplication<User> {
        @Override
        public Set<Object> getSingletons() {
            return Collections.singleton(new ApplicationPathTest.ResourceClass());
        }

        @Override
        public Authentication<User> getAuthentication() {
            return AuthenticatedApplicationPathTest.getAuthentication();
        }
    }

    @Path("")
    @Produces(MediaType.TEXT_PLAIN)
    class ResourceClass {
        @GET
        @PermitAll
        public Response test() {
            return Response.ok("TEST")
                    .build();
        }
    }
}
