package fi.jubic.snoozy.serversuite;

import fi.jubic.snoozy.Server;
import fi.jubic.snoozy.test.TestApplication;
import org.junit.jupiter.api.Test;

import javax.annotation.security.PermitAll;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static fi.jubic.snoozy.test.TestUtil.withServer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface ApplicationPathTest<T extends Server> extends BaseTest<T> {
    @Test
    default void annotatedApplicationServedWithPrefix() throws Exception {
        withServer(
                instance(),
                new AnnotatedApplication(),
                uriBuilder -> {
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
    default void annotatedApplicationServedWithLeadingSlashPrefix() throws Exception {
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
    default void annotatedApplicationServedWithLongPrefix() throws Exception {
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
    default void plainApplicationServedWithoutPrefix() throws Exception {
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

    @ApplicationPath("prefix")
    class AnnotatedApplication extends TestApplication {
        @Override
        public Set<Object> getSingletons() {
            return Collections.singleton(new ResourceClass());
        }
    }

    class PlainApplication extends TestApplication {
        @Override
        public Set<Object> getSingletons() {
            return Collections.singleton(new ResourceClass());
        }
    }

    @ApplicationPath("/prefix")
    class LeadingSlashAnnotatedApplication extends TestApplication {
        @Override
        public Set<Object> getSingletons() {
            return Collections.singleton(new ResourceClass());
        }
    }

    @ApplicationPath("/long/prefix")
    class LongPathAnnotatedApplication extends TestApplication {
        @Override
        public Set<Object> getSingletons() {
            return Collections.singleton(new ResourceClass());
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
