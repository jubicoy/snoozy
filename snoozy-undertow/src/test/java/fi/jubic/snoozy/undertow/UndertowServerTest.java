package fi.jubic.snoozy.undertow;

import static org.junit.Assert.assertEquals;

import fi.jubic.snoozy.Application;
import fi.jubic.snoozy.ServerConfiguration;
import fi.jubic.snoozy.ServerConfigurator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.Set;

public class UndertowServerTest {
    private static final String hostname = "127.0.0.1";

    private int port;
    private Configuration configuration;

    @Before
    public void setup() throws IOException {
        var socket = new ServerSocket(0);
        port = socket.getLocalPort();
        socket.close();
        configuration = new Configuration(hostname, port);
    }

    @Test
    public void testAnnotatedApplication() throws IOException, InterruptedException {
        testApplication(new AnnotatedApplication(), "test");
    }

    @Test
    public void testUnannotatedApplication() throws IOException, InterruptedException {
        testApplication(new UnannotatedApplication(), "");
    }

    private void testApplication(
            Application application,
            String suffix
    ) throws IOException, InterruptedException {
        var server = new UndertowServer();
        server.start(application, configuration);

        var request = HttpRequest.newBuilder()
                .uri(
                        URI.create(
                                String.format(
                                        "http://%s:%d/%s",
                                        hostname,
                                        port,
                                        suffix
                                )
                        )
                )
                .build();
        var client = HttpClient.newHttpClient();
        var response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(response.body(), "TEST");

        server.stop();
    }

    static class Configuration implements ServerConfigurator {
        private final ServerConfiguration serverConfiguration;

        public Configuration(String hostname, int port) {
            serverConfiguration = new ServerConfiguration();

            serverConfiguration.setHostname(hostname);
            serverConfiguration.setPort(port);
        }

        @Override
        public ServerConfiguration getServerConfiguration() {
            return serverConfiguration;
        }
    }

    static class UnannotatedApplication extends Application {
        @Override
        public Set<Object> getSingletons() {
            return Collections.singleton(new TestResource());
        }
    }
}
