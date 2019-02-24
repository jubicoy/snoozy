package fi.jubic.resteasy.example;

import fi.jubic.easyconfig.annontations.EasyConfigProperty;
import fi.jubic.easyconfig.jooq.JooqConfiguration;
import fi.jubic.resteasy.ServerConfiguration;
import fi.jubic.resteasy.ServerConfigurator;

public class Configuration implements ServerConfigurator {
    private final ServerConfiguration serverConfiguration;
    private final JooqConfiguration jooqConfiguration;

    public Configuration(
            @EasyConfigProperty("SERVER_")
            ServerConfiguration serverConfiguration,
            @EasyConfigProperty("")
            JooqConfiguration jooqConfiguration
    ) {
        this.serverConfiguration = serverConfiguration;
        this.jooqConfiguration = jooqConfiguration;
    }

    @Override
    public ServerConfiguration getServerConfiguration() {
        return this.serverConfiguration;
    }

    public JooqConfiguration getJooqConfiguration() {
        return jooqConfiguration;
    }
}
