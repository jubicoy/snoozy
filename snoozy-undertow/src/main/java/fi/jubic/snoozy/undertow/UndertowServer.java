package fi.jubic.snoozy.undertow;

import fi.jubic.snoozy.*;
import fi.jubic.snoozy.auth.AuthenticatedApplication;
import fi.jubic.snoozy.auth.UserPrincipal;
import fi.jubic.snoozy.filters.StaticFilesFilter;
import fi.jubic.snoozy.server.ApplicationAdapter;
import fi.jubic.snoozy.server.AuthFilterAdapter;
import io.undertow.Undertow;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import java.util.Set;

public class UndertowServer implements Server {
    @Override
    public void start(
            Application application,
            ServerConfigurator serverConfigurator
    ) {
        ApplicationAdapter applicationAdapter = ApplicationAdapter.of(application);
        UndertowJaxrsServer server = startServer(
                applicationAdapter,
                serverConfigurator
        );

        addStaticFiles(
                server,
                application.getStaticFiles(),
                (staticFiles, request) -> true // Allow all filter
        );
    }

    @Override
    public <P extends UserPrincipal> void start(
            AuthenticatedApplication<P> application,
            ServerConfigurator serverConfigurator
    ) {
        AuthFilterAdapter<P> authFilterAdapter = AuthFilterAdapter.of(
                application.getAuthentication(),
                crc -> ((ResourceMethodInvoker) crc.getProperty(
                        "org.jboss.resteasy.core.ResourceMethodInvoker"
                )).getMethod(),
                ResteasyProviderFactory::pushContext
        );

        ApplicationAdapter applicationAdapter = ApplicationAdapter.of(
                application,
                authFilterAdapter
        );

        UndertowJaxrsServer server = startServer(
                applicationAdapter,
                serverConfigurator
        );

        addStaticFiles(
                server,
                application.getStaticFiles(),
                authFilterAdapter
        );
    }

    private UndertowJaxrsServer startServer(
            javax.ws.rs.core.Application application,
            ServerConfigurator serverConfigurator
    ) {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder()
                .setStatusLevel(Level.INFO);

        AppenderComponentBuilder appenderBuilder = builder.newAppender("Stdout", "CONSOLE")
                .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);

        builder.add(appenderBuilder);

        builder.add(
                builder.newRootLogger(Level.INFO)
                        .add(builder.newAppenderRef("Stdout"))
        );

        Configurator.initialize(builder.build());

        ServerConfiguration configuration = serverConfigurator.getServerConfiguration();

        return new UndertowJaxrsServer()
                .deploy(application)
                .start(
                        Undertow.builder()
                                .addHttpListener(
                                        configuration.getPort(),
                                        configuration.getHostname()
                                )
                );
    }

    private void addStaticFiles(
            UndertowJaxrsServer server,
            Set<StaticFiles> staticFilesSet,
            StaticFilesFilter filter
    ) {
        staticFilesSet.forEach(
                staticFiles -> server.addResourcePrefixPath(
                        staticFiles.path(),
                        new FilteredResourceHandler(filter, staticFiles)
                                .addWelcomeFiles("index.html")
                )
        );
    }
}
