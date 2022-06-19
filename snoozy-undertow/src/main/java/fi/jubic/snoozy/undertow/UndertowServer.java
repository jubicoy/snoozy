package fi.jubic.snoozy.undertow;

import fi.jubic.snoozy.MultipartConfig;
import fi.jubic.snoozy.Server;
import fi.jubic.snoozy.ServerConfiguration;
import fi.jubic.snoozy.auth.UserPrincipal;
import fi.jubic.snoozy.filters.StaticFilesFilter;
import fi.jubic.snoozy.server.ApplicationAdapter;
import fi.jubic.snoozy.server.AuthFilterAdapter;
import fi.jubic.snoozy.server.AuthenticatedApplicationAdapter;
import fi.jubic.snoozy.staticfiles.StaticFiles;
import io.undertow.Undertow;
import io.undertow.servlet.api.DeploymentInfo;
import jakarta.servlet.MultipartConfigElement;
import jakarta.ws.rs.ApplicationPath;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ResteasyContext;
import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;

import java.util.Optional;
import java.util.Set;

public class UndertowServer implements Server {
    private UndertowJaxrsServer server;

    @Override
    public void start(
            ApplicationAdapter applicationAdapter,
            ServerConfiguration serverConfiguration
    ) {
        server = startServer(applicationAdapter, serverConfiguration);

        addStaticFiles(
                server,
                applicationAdapter.getStaticFiles(),
                // Allow all filter
                (staticFiles, request) -> Optional.empty()
        );
    }

    @Override
    public <P extends UserPrincipal> void start(
            AuthenticatedApplicationAdapter<P> applicationAdapter,
            ServerConfiguration serverConfiguration
    ) {
        AuthFilterAdapter<P> authFilterAdapter = AuthFilterAdapter.of(
                applicationAdapter.getAuthentication(),
                crc -> ((ResourceMethodInvoker) crc.getProperty(
                        "org.jboss.resteasy.core.ResourceMethodInvoker"
                )).getMethod(),
                ResteasyContext::pushContext
        );

        applicationAdapter.setAuthFilterAdapter(authFilterAdapter);

        server = startServer(
                applicationAdapter,
                serverConfiguration
        );

        addStaticFiles(
                server,
                applicationAdapter.getStaticFiles(),
                authFilterAdapter
        );
    }

    @Override
    public void stop() {
        server.stop();
    }


    private UndertowJaxrsServer startServer(
            ApplicationAdapter applicationAdapter,
            ServerConfiguration serverConfiguration
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

        MultipartConfig multipartConfig = serverConfiguration.getMultipartConfig();
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
                                        serverConfiguration.getPort(),
                                        serverConfiguration.getHostname()
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
