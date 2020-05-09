package fi.jubic.snoozy.swagger;

public interface SwaggerConfig {
    /**
     * Return true to always enable automatic OpenAPI JSON generation for /swagger.json endpoint.
     * By default the OpenAPI document is available only in dev mode.
     */
    default boolean alwaysServeOpenApi() {
        return false;
    }

    /**
     * Return true to always serve Swagger UI at /swagger. By default Swagger UI is available only
     * in dev mode.
     */
    default boolean alwaysServeSwaggerUi() {
        return false;
    }
}
