package fi.jubic.snoozy;

import fi.jubic.snoozy.swagger.SwaggerConfig;

public interface ServerConfiguration {
    /**
     * Server bind address.
     */
    String getHostname();

    /**
     * Server listen port.
     */
    int getPort();

    default boolean isDevMode() {
        return false;
    }

    default MultipartConfig getMultipartConfig() {
        return new DefaultMultipartConfig();
    }

    default SwaggerConfig getSwaggerConfig() {
        return new DefaultSwaggerConfig();
    }

    class DefaultMultipartConfig implements MultipartConfig {

    }

    class DefaultSwaggerConfig implements SwaggerConfig {

    }
}
