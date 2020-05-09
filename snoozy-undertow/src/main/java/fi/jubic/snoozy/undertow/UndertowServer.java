package fi.jubic.snoozy.undertow;

import fi.jubic.snoozy.Application;
import fi.jubic.snoozy.AuthenticatedApplication;
import fi.jubic.snoozy.MultipartConfig;
import fi.jubic.snoozy.Server;
import fi.jubic.snoozy.ServerConfiguration;
import fi.jubic.snoozy.ServerConfigurator;
import fi.jubic.snoozy.auth.UserPrincipal;
import fi.jubic.snoozy.filters.StaticFilesFilter;
import fi.jubic.snoozy.server.ApplicationAdapter;
import fi.jubic.snoozy.server.AuthFilterAdapter;
import fi.jubic.snoozy.staticfiles.StaticFiles;
import io.undertow.Undertow;
import io.undertow.servlet.api.DeploymentInfo;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ResteasyContext;
import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class UndertowServer implements Server {
    private UndertowJaxrsServer server;

    @Override
    public void start(
            Application application,
            ServerConfigurator serverConfigurator
    ) {
        ApplicationAdapter applicationAdapter = new ApplicationAdapter(application);

        server = startServer(applicationAdapter, serverConfigurator);

        addStaticFiles(
                server,
                application.getStaticFiles(),
                // Allow all filter
                new StaticFilesFilter() {
                    @Override
                    public boolean filter(StaticFiles staticFiles, HttpServletRequest request) {
                        return true;
                    }

                    @Override
                    public Supplier<Response> getResponseSupplier() {
                        return () -> Response.status(Response.Status.UNAUTHORIZED)
                                .build();
                    }
                }
        );

        applicationAdapter.logStartup(serverConfigurator.getServerConfiguration());
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

        ApplicationAdapter applicationAdapter = new ApplicationAdapter(
                application,
                authFilterAdapter
        );

        server = startServer(
                applicationAdapter,
                serverConfigurator
        );

        addStaticFiles(
                server,
                application.getStaticFiles(),
                authFilterAdapter
        );

        applicationAdapter.logStartup(serverConfigurator.getServerConfiguration());
    }

    @Override
    public void stop() {
        server.stop();
    }


    private UndertowJaxrsServer startServer(
            ApplicationAdapter applicationAdapter,
            ServerConfigurator serverConfigurator
    ) {
        UndertowJaxrsServer undertowJaxrsServer = new UndertowJaxrsServer();

        ResteasyDeployment deployment = new ResteasyDeploymentImpl();
        deployment.setApplication(applicationAdapter);
        DeploymentInfo deploymentInfo = undertowJaxrsServer.undertowDeployment(deployment);
        deploymentInfo.setClassLoader(applicationAdapter.getClass().getClassLoader());

        String path = "/"
                + Optional.ofNullable(
                applicationAdapter.getApplicationClass()
                        .getAnnotation(ApplicationPath.class)
        )
                .map(ApplicationPath::value)
                .orElse("")
                .replaceAll("^/", "");

        deploymentInfo.setContextPath(path);
        deploymentInfo.setDeploymentName("Snoozy" + path);

        ServerConfiguration configuration = serverConfigurator
                .getServerConfiguration();

        MultipartConfig multipartConfig = configuration.getMultipartConfig();
        deploymentInfo.setDefaultMultipartConfig(
                new MultipartConfigElement(
                        multipartConfig.getCacheLocation(),
                        multipartConfig.getMaxFileSize(),
                        multipartConfig.getMaxRequestSize(),
                        multipartConfig.getSizeThreshold()
                )
        );

        return undertowJaxrsServer.deploy(deploymentInfo)
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
                        staticFiles.getPath(),
                        new FilteredResourceHandler(filter, staticFiles)
                                .addWelcomeFiles("index.html")
                )
        );
    }
}
