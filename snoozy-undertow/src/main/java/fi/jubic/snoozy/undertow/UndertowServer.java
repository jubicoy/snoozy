package fi.jubic.snoozy.undertow;

import fi.jubic.snoozy.*;
import fi.jubic.snoozy.auth.AuthenticatedApplication;
import fi.jubic.snoozy.auth.UserPrincipal;
import fi.jubic.snoozy.filters.StaticFilesFilter;
import fi.jubic.snoozy.server.ApplicationAdapter;
import fi.jubic.snoozy.server.AuthFilterAdapter;
import fi.jubic.snoozy.server.RegisteredResource;
import io.undertow.Undertow;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ResteasyContext;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

public class UndertowServer implements Server {
    private static final Logger logger = LoggerFactory
            .getLogger(UndertowServer.class);

    @Override
    public void start(
            Application application,
            ServerConfigurator serverConfigurator
    ) {
        ApplicationAdapter applicationAdapter = ApplicationAdapter.of(
                application
        );
        UndertowJaxrsServer server = startServer(
                applicationAdapter,
                serverConfigurator
        );

        addStaticFiles(
                server,
                application.getStaticFiles(),
                (staticFiles, request) -> true // Allow all filter
        );

        logResources(applicationAdapter.getRegisteredResources());
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
                ResteasyContext::pushContext
        );

        logger.info(
                "\n\t::\n\t:: {}: v{} ::\n\t::\n",
                application.getClass().getPackage().getImplementationTitle(),
                application.getClass().getPackage().getImplementationVersion()
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

        logResources(applicationAdapter.getRegisteredResources());
    }

    private UndertowJaxrsServer startServer(
            javax.ws.rs.core.Application application,
            ServerConfigurator serverConfigurator
    ) {
        ServerConfiguration configuration = serverConfigurator
                .getServerConfiguration();

        logger.info(
                "Listening on {}:{}",
                configuration.getHostname(),
                configuration.getPort()
        );

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

        logStaticFiles(staticFilesSet);
    }

    private void logResources(List<RegisteredResource> resources) {
        String list = resources.stream()
                .map(RegisteredResource::toString)
                .reduce("", (a, b) -> a + "\n\t" + b);

        logger.info("The following paths were found: {}", list);
    }

    private void logStaticFiles(Set<StaticFiles> staticFiles) {
        int pathWidth = 0;
        int prefixWidth = 0;

        for (StaticFiles s : staticFiles) {
            String path = s.path().startsWith("/") ? s.path() : "/" + s.path();

            if (path.length() > pathWidth) {
                pathWidth = path.length();
            }

            if (s.prefix().length() > prefixWidth) {
                prefixWidth = s.prefix().length();
            }
        }

        int finalPathWidth = pathWidth;
        int finalPrefixWidth = prefixWidth;
        String list = staticFiles.stream()
                .map(s -> String.format(
                        "%-"
                                + finalPathWidth
                                + "s -> %-"
                                + finalPrefixWidth
                                + "s (%s)",
                        s.path().startsWith("/") ? s.path() : "/" + s.path(),
                        s.prefix(),
                        s.methodAccess().level()
                ))
                .reduce("", (a, b) -> a + "\n\t" + b);

        logger.info("The following static paths were found: {}", list);
    }
}
