package fi.jubic.snoozy.serversuite;

import fi.jubic.snoozy.Application;
import fi.jubic.snoozy.Server;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import javax.annotation.security.PermitAll;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.junit.jupiter.api.Test;

import static fi.jubic.snoozy.test.TestUtil.withServer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface ApplicationPathTest<T extends Server> extends BaseTest<T> {
    @Test
    default void annotatedApplicationServedWithPrefix() throws Exception {
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
    default void annotatedApplicationServedWithLeadingSlashPrefix() throws Exception {
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
    default void annotatedApplicationServedWithLongPrefix() throws Exception {
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
    default void plainApplicationServedWithoutPrefix() throws Exception {
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

    @ApplicationPath("prefix")
    class AnnotatedApplication extends Application {
        @Override
        public Set<Object> getSingletons() {
            return Collections.singleton(new ResourceClass());
        }
    }

    class PlainApplication extends Application {
        @Override
        public Set<Object> getSingletons() {
            return Collections.singleton(new ResourceClass());
        }
    }

    @ApplicationPath("/prefix")
    class LeadingSlashAnnotatedApplication extends Application {
        @Override
        public Set<Object> getSingletons() {
            return Collections.singleton(new ResourceClass());
        }
    }

    @ApplicationPath("/long/prefix")
    class LongPathAnnotatedApplication extends Application {
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
