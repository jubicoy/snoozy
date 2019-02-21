package fi.jubic.resteasy.example;

import fi.jubic.resteasy.ServerConfiguration;
import fi.jubic.resteasy.ServerConfigurator;

public class Configuration implements ServerConfigurator {
    @Override
    public ServerConfiguration getServerConfiguration() {
        ServerConfiguration serverConfiguration = new ServerConfiguration();
        serverConfiguration.setHostname("localhost");
        serverConfiguration.setPort(8080);
        return serverConfiguration;
    }
}
