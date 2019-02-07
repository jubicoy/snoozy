package fi.jubic.resteasy.server;

import fi.jubic.resteasy.auth.Auth;
import fi.jubic.resteasy.auth.RestrictedResourceHandler;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import org.apache.logging.log4j.core.config.Configurator;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;

import java.util.Optional;

public class UndertowServer implements IServer {
    private Optional<ResourceHandler> getStaticResourceHandler(Application application) {
        StaticFiles staticFiles = application.getStaticFiles();

        if (staticFiles == null) {
            return Optional.empty();
        }

        Auth<?> auth = application.getAuth();
        ResourceHandler handler;

        if (auth == null) {
            handler = Handlers.resource(
                    new ClassPathResourceManager(
                            staticFiles.classLoader(),
                            staticFiles.prefix()
                    )
            );
        } else {
            RestrictedResourceHandler.Builder builder = RestrictedResourceHandler.builder()
                    .setClassLoader(staticFiles.classLoader())
                    .setPrefix(staticFiles.prefix())
                    .setAuthenticator(auth.authenticator())
                    .setExcludes(auth.excludes())
                    .setIncludes(auth.includes());

            if (auth.tokenParser() != null) {
                builder = builder.setTokenParser(auth.tokenParser());
            }

            handler = builder.build();
        }

        return Optional.of(handler);
    }

    public void start(Application application) {
        Configurator.initialize(application.getLoggerConfiguration());

        UndertowJaxrsServer server = new UndertowJaxrsServer()
                .deploy(application)
                .start(
                        Undertow.builder()
                            .addHttpListener(8080, "localhost")
                );

        getStaticResourceHandler(application).ifPresent(h -> server.addResourcePrefixPath("/", h));
    }
}
