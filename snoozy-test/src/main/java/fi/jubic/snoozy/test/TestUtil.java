package fi.jubic.snoozy.test;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import fi.jubic.snoozy.Application;
import fi.jubic.snoozy.AuthenticatedApplication;
import fi.jubic.snoozy.DefaultServerConfiguration;
import fi.jubic.snoozy.Server;
import fi.jubic.snoozy.ServerConfiguration;
import fi.jubic.snoozy.ServerConfigurator;
import fi.jubic.snoozy.auth.UserPrincipal;

import javax.annotation.Nullable;
import java.net.ServerSocket;

@SuppressFBWarnings("UNENCRYPTED_SERVER_SOCKET")
public final class TestUtil {
    /**
     * Starts the application with the given server instance at a automatically selected port on
     * localhost, calls the consumer and finally stops the server. Default configuration will be
     * used.
     */
    public static void withServer(
            Server server,
            Application application,
            ServerConsumer serverConsumer
    ) throws Exception {
        withServer(server, application, null, serverConsumer);
    }

    /**
     * Starts the application with the given server instance at a automatically
     * selected port on localhost, calls the consumer and finally stops the server.
     */
    public static void withServer(
            Server server,
            Application application,
            ServerConfiguration serverConfiguration,
            ServerConsumer serverConsumer
    ) throws Exception {
        // Select available port
        ServerSocket socket = new ServerSocket(0);

        String hostname = "localhost";
        int port = socket.getLocalPort();

        socket.close();

        // Create configuration
        ServerConfigurator configurator = buildConfigurator(hostname, port, serverConfiguration);

        server.start(application, configurator);

        serverConsumer.consume(hostname, port);

        server.stop();
    }

    /**
     * Starts the authenticated application with the given server instance at a automatically
     * selected port on localhost, calls the consumer and finally stops the server. Default
     * configuration will be used.
     */
    public static <P extends UserPrincipal> void withServer(
            Server server,
            AuthenticatedApplication<P> application,
            ServerConsumer serverConsumer
    ) throws Exception {
        withServer(server, application, null, serverConsumer);
    }

    /**
     * Starts the authenticated application with the given server instance at a automatically
     * selected port on localhost, calls the consumer and finally stops the server.
     */
    public static <P extends UserPrincipal> void withServer(
            Server server,
            AuthenticatedApplication<P> application,
            ServerConfiguration serverConfiguration,
            ServerConsumer serverConsumer
    ) throws Exception {
        // Select available port
        ServerSocket socket = new ServerSocket(0);

        String hostname = "localhost";
        int port = socket.getLocalPort();

        socket.close();

        // Create configuration
        ServerConfigurator configurator = buildConfigurator(hostname, port, serverConfiguration);

        server.start(application, configurator);

        serverConsumer.consume(hostname, port);

        server.stop();
    }

    private static ServerConfigurator buildConfigurator(
            String hostname,
            int port,
            @Nullable ServerConfiguration serverConfiguration
    ) {
        if (serverConfiguration == null) {
            return () -> new DefaultServerConfiguration(hostname, port);
        }
        else {
            return () -> new WrappedServerConfiguration(
                    hostname,
                    port,
                    serverConfiguration
            );
        }
    }
}
