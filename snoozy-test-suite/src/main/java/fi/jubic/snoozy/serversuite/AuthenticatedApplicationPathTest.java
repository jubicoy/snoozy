package fi.jubic.snoozy.serversuite;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import fi.jubic.snoozy.Server;
import fi.jubic.snoozy.auth.Authentication;
import fi.jubic.snoozy.auth.UserPrincipal;
import fi.jubic.snoozy.test.TestAuthenticatedApplication;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import javax.annotation.security.PermitAll;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static fi.jubic.snoozy.test.TestUtil.withServer;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressFBWarnings("THROWS_METHOD_THROWS_CLAUSE_BASIC_EXCEPTION")
public interface AuthenticatedApplicationPathTest<T extends Server> extends BaseTest<T> {
    @Test
    default void annotatedAuthApplicationServedWithPrefix() throws Exception {
        withServer(
                instance(),
                new AnnotatedApplication(),
                (uriBuilder) -> {
                    var client = HttpClient.newHttpClient();

                    {
                        var response = client.send(
                                HttpRequest.newBuilder(uriBuilder.build("/prefix"))
                                        .GET()
                                        .build(),
                                HttpResponse.BodyHandlers.ofString()
                        );

                        assertEquals(200, response.statusCode());
                        assertEquals("TEST", Objects.requireNonNull(response.body()));
                    }

                    {
                        var response = client.send(
                                HttpRequest.newBuilder(uriBuilder.build("/"))
                                        .GET()
                                        .build(),
                                HttpResponse.BodyHandlers.ofString()
                        );

                        assertEquals(404, response.statusCode());
                    }
                }
        );
    }

    @Test
    default void annotatedAuthApplicationServedWithLeadingSlashPrefix() throws Exception {
        withServer(
                instance(),
                new LeadingSlashAnnotatedApplication(),
                (uriBuilder) -> {
                    var client = HttpClient.newHttpClient();

                    {
                        var response = client.send(
                                HttpRequest.newBuilder(uriBuilder.build("/prefix"))
                                        .GET()
                                        .build(),
                                HttpResponse.BodyHandlers.ofString()
                        );

                        assertEquals(200, response.statusCode());
                        assertEquals("TEST", Objects.requireNonNull(response.body()));
                    }

                    {
                        var response = client.send(
                                HttpRequest.newBuilder(uriBuilder.build("/"))
                                        .GET()
                                        .build(),
                                HttpResponse.BodyHandlers.ofString()
                        );

                        assertEquals(404, response.statusCode());
                    }
                }
        );
    }

    @Test
    default void annotatedAuthApplicationServedWithLongPrefix() throws Exception {
        withServer(
                instance(),
                new LongPathAnnotatedApplication(),
                (uriBuilder) -> {
                    var client = HttpClient.newHttpClient();

                    {
                        var response = client.send(
                                HttpRequest.newBuilder(uriBuilder.build("/long/prefix"))
                                        .GET()
                                        .build(),
                                HttpResponse.BodyHandlers.ofString()
                        );

                        assertEquals(200, response.statusCode());
                        assertEquals("TEST", Objects.requireNonNull(response.body()));
                    }

                    {
                        var response = client.send(
                                HttpRequest.newBuilder(uriBuilder.build("/"))
                                        .GET()
                                        .build(),
                                HttpResponse.BodyHandlers.ofString()
                        );

                        assertEquals(404, response.statusCode());
                    }
                }
        );
    }

    @Test
    default void plainAuthApplicationServedWithoutPrefix() throws Exception {
        withServer(
                instance(),
                new PlainApplication(),
                (uriBuilder) -> {
                    var client = HttpClient.newHttpClient();

                    {
                        var response = client.send(
                                HttpRequest.newBuilder(uriBuilder.build("/"))
                                        .GET()
                                        .build(),
                                HttpResponse.BodyHandlers.ofString()
                        );

                        assertEquals(200, response.statusCode());
                        assertEquals("TEST", Objects.requireNonNull(response.body()));
                    }

                    {
                        var response = client.send(
                                HttpRequest.newBuilder(uriBuilder.build("/prefix"))
                                        .GET()
                                        .build(),
                                HttpResponse.BodyHandlers.ofString()
                        );

                        assertEquals(404, response.statusCode());
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
