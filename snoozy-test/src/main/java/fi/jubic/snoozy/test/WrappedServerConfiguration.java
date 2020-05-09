package fi.jubic.snoozy.test;

import fi.jubic.snoozy.MultipartConfig;
import fi.jubic.snoozy.ServerConfiguration;
import fi.jubic.snoozy.swagger.SwaggerConfig;

public class WrappedServerConfiguration implements ServerConfiguration {
    private final String hostname;
    private final int port;
    private final ServerConfiguration wrappedConfiguration;

    public WrappedServerConfiguration(
            String hostname,
            int port,
            ServerConfiguration wrappedConfiguration
    ) {
        this.hostname = hostname;
        this.port = port;
        this.wrappedConfiguration = wrappedConfiguration;
    }

    @Override
    public String getHostname() {
        return hostname;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public boolean isDevMode() {
        return wrappedConfiguration.isDevMode();
    }

    @Override
    public MultipartConfig getMultipartConfig() {
        return wrappedConfiguration.getMultipartConfig();
    }

    @Override
    public SwaggerConfig getSwaggerConfig() {
        return wrappedConfiguration.getSwaggerConfig();
    }
}
