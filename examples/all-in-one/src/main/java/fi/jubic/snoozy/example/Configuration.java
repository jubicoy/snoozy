package fi.jubic.snoozy.example;

import fi.jubic.easyconfig.annotations.EasyConfigProperty;
import fi.jubic.easyconfig.jooq.JooqConfiguration;
import fi.jubic.easyconfig.snoozy.SnoozyServerConfiguration;
import fi.jubic.snoozy.ServerConfiguration;
import fi.jubic.snoozy.ServerConfigurator;

public class Configuration implements ServerConfigurator {
    private final ServerConfiguration serverConfiguration;
    private final JooqConfiguration jooqConfiguration;

    public Configuration(
            @EasyConfigProperty("SERVER_")
            SnoozyServerConfiguration serverConfiguration,
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
