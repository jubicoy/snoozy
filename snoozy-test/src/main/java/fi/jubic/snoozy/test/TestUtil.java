package fi.jubic.snoozy.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import fi.jubic.snoozy.Application;
import fi.jubic.snoozy.AuthenticatedApplication;
import fi.jubic.snoozy.DefaultServerConfiguration;
import fi.jubic.snoozy.Server;
import fi.jubic.snoozy.ServerConfiguration;
import fi.jubic.snoozy.ServerConfigurator;
import fi.jubic.snoozy.auth.UserPrincipal;
import fi.jubic.snoozy.mappers.jackson.ObjectMapperResolver;

import javax.annotation.Nullable;
import java.net.ServerSocket;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;

@SuppressFBWarnings("UNENCRYPTED_SERVER_SOCKET")
public final class TestUtil {
    /**
     * Starts the application with the given server instance at an automatically selected port on
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
     * Starts the application with the given server instance at an automatically
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
     * Starts the application with the given server instance at an automatically selected port on
     * localhost, calls the consumer and finally stops the server. Default configuration will be
     * used.
     */
    public static void withServer(
            Server server,
            Application application,
            UriBuilderConsumer uriBuilderConsumer
    ) throws Exception {
        withServer(
                server,
                application,
                (hostname, port) -> uriBuilderConsumer.consume(new UriBuilder(hostname, port))
        );
    }

    /**
     * Starts the application with the given server instance at an automatically
     * selected port on localhost, calls the consumer and finally stops the server.
     */
    public static void withServer(
            Server server,
            Application application,
            ServerConfiguration serverConfiguration,
            UriBuilderConsumer uriBuilderConsumer
    ) throws Exception {
        withServer(
                server,
                application,
                serverConfiguration,
                (hostname, port) -> uriBuilderConsumer.consume(new UriBuilder(hostname, port))
        );
    }

    /**
     * Starts the authenticated application with the given server instance at an automatically
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
     * Starts the authenticated application with the given server instance at an automatically
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

    /**
     * Starts the authenticated application with the given server instance at an automatically
     * selected port on localhost, calls the consumer and finally stops the server. Default
     * configuration will be used.
     */
    public static <P extends UserPrincipal> void withServer(
            Server server,
            AuthenticatedApplication<P> application,
            UriBuilderConsumer uriBuilderConsumer
    ) throws Exception {
        withServer(
                server,
                application,
                (hostname, port) -> uriBuilderConsumer.consume(new UriBuilder(hostname, port))
        );
    }

    /**
     * Starts the authenticated application with the given server instance at an automatically
     * selected port on localhost, calls the consumer and finally stops the server.
     */
    public static <P extends UserPrincipal> void withServer(
            Server server,
            AuthenticatedApplication<P> application,
            ServerConfiguration serverConfiguration,
            UriBuilderConsumer uriBuilderConsumer
    ) throws Exception {
        withServer(
                server,
                application,
                serverConfiguration,
                (hostname, port) -> uriBuilderConsumer.consume(new UriBuilder(hostname, port))
        );
    }

    /**
     * Create a Jackson backed {@link java.net.http.HttpRequest.BodyPublisher} for a given object.
     * The built-in {@link ObjectMapper} is used.
     *
     * @param body The body object
     */
    public static HttpRequest.BodyPublisher requestJson(Object body) {
        return requestJson(body, new ObjectMapperResolver().getContext(null));
    }

    /**
     * Create a Jackson backed {@link java.net.http.HttpRequest.BodyPublisher} for a given object.
     * The provided {@link ObjectMapper} is used.
     *
     * @param body body object
     * @param objectMapper object mapper to use for serialization
     */
    public static HttpRequest.BodyPublisher requestJson(Object body, ObjectMapper objectMapper) {
        try {
            return HttpRequest.BodyPublishers.ofString(
                    objectMapper.writeValueAsString(body)
            );
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a {@link java.net.http.HttpRequest.BodyPublisher} from a {@link JsonNode}.
     */
    public static HttpRequest.BodyPublisher requestJsonNode(JsonNode jsonNode) {
        return requestJsonNode(jsonNode, new ObjectMapperResolver().getContext(null));
    }

    /**
     * Create a {@link java.net.http.HttpRequest.BodyPublisher} from a {@link JsonNode}.
     * The given {@link ObjectMapper} is used for serialization.
     */
    public static HttpRequest.BodyPublisher requestJsonNode(
            JsonNode jsonNode,
            ObjectMapper objectMapper
    ) {
        try {
            return HttpRequest.BodyPublishers.ofString(
                    objectMapper.writeValueAsString(jsonNode)
            );
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a {@link java.net.http.HttpResponse.BodyHandler} for a given type.
     * The built-on {@link ObjectMapper} is used.
     */
    public static <T> HttpResponse.BodyHandler<T> responseJson(Class<T> clazz) {
        return responseJson(clazz, new ObjectMapperResolver().getContext(null));
    }

    /**
     * Create a {@link java.net.http.HttpResponse.BodyHandler} for a given type.
     * The provided {@link ObjectMapper} is used for deserialization.
     */
    public static <T> HttpResponse.BodyHandler<T> responseJson(
            Class<T> clazz,
            ObjectMapper objectMapper
    ) {
        return responseInfo -> HttpResponse.BodySubscribers.mapping(
                HttpResponse.BodySubscribers.ofString(Charset.defaultCharset()),
                str -> {
                    try {
                        return objectMapper.readValue(str, clazz);
                    }
                    catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    /**
     * Create a {@link java.net.http.HttpResponse.BodyHandler} returning a
     * {@link JsonNode}.
     */
    public static HttpResponse.BodyHandler<JsonNode> responseJsonNode() {
        return responseJsonNode(new ObjectMapperResolver().getContext(null));
    }

    /**
     * Create a {@link java.net.http.HttpResponse.BodyHandler} returning a
     * {@link JsonNode}. The provided {@link ObjectMapper} is used for
     * deserialization.
     */
    public static HttpResponse.BodyHandler<JsonNode> responseJsonNode(
            ObjectMapper objectMapper
    ) {
        return responseInfo -> HttpResponse.BodySubscribers.mapping(
                HttpResponse.BodySubscribers.ofString(Charset.defaultCharset()),
                str -> {
                    try {
                        return objectMapper.readTree(str);
                    }
                    catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
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
