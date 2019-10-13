package fi.jubic.snoozy.undertow;

import fi.jubic.snoozy.Application;
import fi.jubic.snoozy.Server;
import fi.jubic.snoozy.ServerConfiguration;
import fi.jubic.snoozy.ServerConfigurator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UndertowServerTest {
    private static final String hostname = "127.0.0.1";

    private int port;
    private Configuration configuration;

    @BeforeEach
    public void setup() throws IOException {
        ServerSocket socket = new ServerSocket(0);
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
        Server server = new UndertowServer();
        server.start(application, configuration);

        URL url = new URL(String.format("http://%s:%d/%s", hostname, port, suffix));
        HttpURLConnection client = (HttpURLConnection) url.openConnection();

        String body;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            body = stringBuilder.toString();
        }

        server.stop();

        assertEquals(body, "TEST");
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