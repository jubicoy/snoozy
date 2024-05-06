package fi.jubic.snoozy.swagger;

import com.fasterxml.jackson.databind.JsonNode;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import fi.jubic.snoozy.Application;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.jaxrs2.Reader;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressFBWarnings("CT_CONSTRUCTOR_THROW")
@Path("")
public class SwaggerResource {
    private final JsonNode openApiJson;

    public SwaggerResource(Application application) {
        this.openApiJson = Json.mapper().valueToTree(
                new Reader().read(
                        Stream.concat(
                                Stream.of(application.getClass()),
                                application.getSingletons()
                                        .stream()
                                        .map(Object::getClass)
                                        .filter(
                                                singletonClass -> !Objects.isNull(
                                                        singletonClass.getAnnotation(Path.class)
                                                )
                                        )
                        ).collect(Collectors.toSet())
                )
        );
    }

    @GET
    @Path("swagger.json")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonNode getSwaggerJson() {
        return openApiJson;
    }
}
