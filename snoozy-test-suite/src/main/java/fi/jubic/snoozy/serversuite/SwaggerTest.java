package fi.jubic.snoozy.serversuite;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import fi.jubic.snoozy.DefaultServerConfiguration;
import fi.jubic.snoozy.Server;
import fi.jubic.snoozy.ServerConfiguration;
import fi.jubic.snoozy.swagger.SwaggerConfig;
import fi.jubic.snoozy.test.TestApplication;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.security.PermitAll;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fi.jubic.snoozy.test.TestUtil.withServer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressFBWarnings("THROWS_METHOD_THROWS_CLAUSE_BASIC_EXCEPTION")
public interface SwaggerTest<T extends Server> extends BaseTest<T> {
    static Stream<Arguments> provideStaticResourceConfigurations() {
        return Stream.of(
                Arguments.of(new DevConfiguration(), 200),
                Arguments.of(new AlwaysSwaggerConfig(), 200),
                Arguments.of(new DefaultServerConfiguration(), 404)
        );
    }

    @ParameterizedTest
    @MethodSource("provideStaticResourceConfigurations")
    default void swaggerStaticResourcesAreServed(
            ServerConfiguration configuration,
            int httpStatus
    ) throws Exception {
        withServer(
                instance(),
                new SwaggerApplication(),
                configuration,
                (uriBuilder) -> {
                    var client = HttpClient.newHttpClient();

                    List<String> resources = Arrays.asList(
                            "",
                            "index.html",
                            "favicon-16x16.png",
                            "favicon-32x32.png",
                            "swagger-ui-bundle.js",
                            "swagger-ui.css",
                            "swagger-ui-standalone-preset.js"
                    );
                    for (String resource : resources) {
                        var response = client.send(
                                HttpRequest
                                        .newBuilder(
                                                uriBuilder.build(
                                                        String.format(
                                                                "/swagger/%s",
                                                                resource
                                                        )
                                                )
                                        )
                                        .GET()
                                        .build(),
                                HttpResponse.BodyHandlers.ofString()
                        );
                        assertEquals(httpStatus, response.statusCode());
                    }
                }
        );
    }

    static Stream<Arguments> provideOpenApiConfigurations() {
        return Stream.of(
                Arguments.of(new DevConfiguration()),
                Arguments.of(new AlwaysSwaggerConfig())
        );
    }

    @ParameterizedTest
    @MethodSource("provideOpenApiConfigurations")
    default void swaggerOpenApiDescriptionIsGeneratedAndServed(
            ServerConfiguration configuration
    ) throws Exception {
        withServer(
                instance(),
                new SwaggerApplication(),
                configuration,
                (uriBuilder) -> {
                    var client = HttpClient.newHttpClient();

                    var response = client.send(
                            HttpRequest.newBuilder(uriBuilder.build("/swagger.json"))
                                    .GET()
                                    .build(),
                            HttpResponse.BodyHandlers.ofString()
                    );
                    assertEquals(200, response.statusCode());

                    OpenAPI oapi = Json.mapper()
                            .readValue(
                                    Objects.requireNonNull(response.body()),
                                    OpenAPI.class
                            );
                    assertEquals("Test application", oapi.getInfo().getTitle());
                    assertEquals(
                            "Application that can be used to test some OpenAPI features.",
                            oapi.getInfo().getDescription()
                    );
                    assertEquals("1.0", oapi.getInfo().getVersion());

                    assertNotNull(oapi.getPaths().get("/a/{id}").getGet());
                    assertNotNull(oapi.getPaths().get("/b").getPost());
                    assertNotNull(oapi.getComponents().getSchemas().get("Model"));
                }
        );
    }

    @Test
    default void swaggerOpenApiDescriptionNotServedInProdWhenNotConfigured() throws Exception {
        withServer(
                instance(),
                new SwaggerApplication(),
                (uriBuilder) -> {
                    var client = HttpClient.newHttpClient();

                    var response = client.send(
                            HttpRequest.newBuilder(uriBuilder.build("/swagger.json"))
                                    .GET()
                                    .build(),
                            HttpResponse.BodyHandlers.ofString()
                    );
                    assertEquals(404, response.statusCode());
                }
        );
    }

    @OpenAPIDefinition(
            info = @Info(
                    title = "Test application",
                    description = "Application that can be used to test some OpenAPI features.",
                    version = "1.0"
            )
    )
    class SwaggerApplication extends TestApplication {
        @Override
        public Set<Object> getSingletons() {
            return Stream
                    .of(
                            new ResourceA(),
                            new ResourceB()
                    )
                    .collect(Collectors.toSet());
        }
    }

    @Path("a")
    @Produces(MediaType.APPLICATION_JSON)
    class ResourceA {
        @GET
        @PermitAll
        @Path("{id}")
        public Model getModel(
                @PathParam("id") String id,
                @QueryParam("test") String queryParam
        ) {
            return new Model("test");
        }
    }

    @Path("b")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    class ResourceB {
        @POST
        @PermitAll
        public Model postModel(
                @HeaderParam("Authorization") String auth,
                Model model
        ) {
            return model;
        }
    }

    class Model {
        private final String field;

        Model(String field) {
            this.field = field;
        }

        public String getField() {
            return field;
        }
    }

    class DevConfiguration implements ServerConfiguration {
        @Override
        public String getHostname() {
            return null;
        }

        @Override
        public int getPort() {
            return 0;
        }

        @Override
        public boolean isDevMode() {
            return true;
        }
    }

    class AlwaysSwaggerConfig implements ServerConfiguration {
        @Override
        public String getHostname() {
            return null;
        }

        @Override
        public int getPort() {
            return 0;
        }

        @Override
        public SwaggerConfig getSwaggerConfig() {
            return new SwaggerConfig() {
                @Override
                public boolean alwaysServeOpenApi() {
                    return true;
                }

                @Override
                public boolean alwaysServeSwaggerUi() {
                    return true;
                }
            };
        }
    }
}
