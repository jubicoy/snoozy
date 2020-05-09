package fi.jubic.snoozy.swagger;

import com.fasterxml.jackson.databind.JsonNode;
import fi.jubic.snoozy.Application;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.jaxrs2.Reader;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
