package fi.jubic.snoozy.server;

import fi.jubic.snoozy.Application;
import fi.jubic.snoozy.ServerConfiguration;
import fi.jubic.snoozy.staticfiles.StaticFiles;
import fi.jubic.snoozy.swagger.SwaggerResource;
import fi.jubic.snoozy.swagger.SwaggerStaticFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ApplicationPath;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Adapter class for adding built-in features to a {@link fi.jubic.snoozy.Application}.
 */
public class ApplicationAdapter extends javax.ws.rs.core.Application {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationAdapter.class);

    private final Application application;
    private final ServerConfiguration serverConfiguration;
    private Set<Object> filters;

    /**
     * Wrap a regular Application injecting the following built-ins.
     *
     * <ul>
     *     <li>Request logging</li>
     * </ul>
     */
    public ApplicationAdapter(
            Application application,
            ServerConfiguration serverConfiguration
    ) {
        this.application = application;
        this.serverConfiguration = serverConfiguration;
        this.filters = new HashSet<>();
        this.filters.add(application.getLoggingFilter());
    }

    public Class<?> getApplicationClass() {
        return application.getClass();
    }

    @Override
    public Set<Object> getSingletons() {
        boolean includeSwagger = serverConfiguration.isDevMode()
                || serverConfiguration.getSwaggerConfig().alwaysServeOpenApi();

        return Stream
                .of(
                        filters.stream(),
                        application.getSingletons().stream(),
                        includeSwagger
                                ? Stream.of(new SwaggerResource(application))
                                : Stream.empty()
                )
                .flatMap(Function.identity())
                .collect(Collectors.toSet());
    }

    public Set<StaticFiles> getStaticFiles() {
        boolean includeSwagger = serverConfiguration.isDevMode()
                || serverConfiguration.getSwaggerConfig().alwaysServeSwaggerUi();
        return Stream
                .of(
                        application.getStaticFiles().stream(),
                        includeSwagger
                                ? Stream.<StaticFiles>of(new SwaggerStaticFiles())
                                : Stream.<StaticFiles>empty()
                )
                .flatMap(Function.identity())
                .collect(Collectors.toSet());
    }

    /**
     * Performs startup logging.
     */
    public void logStartup() {
        application.getBanner().ifPresent(banner -> {
            logger.info(banner);

            logger.info(
                    "Listening on {}:{}",
                    serverConfiguration.getHostname(),
                    serverConfiguration.getPort()
            );

            logStaticFiles(application.getStaticFiles());
            logResources(getRegisteredResources());
        });
    }

    /**
     * List registered resource methods.
     */
    private List<RegisteredResource> getRegisteredResources() {
        String prefix;
        Class<? extends Application> applicationClass = application.getClass();

        if (applicationClass.isAnnotationPresent(ApplicationPath.class)) {
            prefix = applicationClass.getAnnotation(ApplicationPath.class)
                    .value();
        }
        else {
            prefix = "/";
        }

        return this.getSingletons()
                .stream()
                .map(Object::getClass)
                .map(c -> Stream.of(c.getMethods())
                        .map(m -> RegisteredResource.of(
                                prefix,
                                m
                        ))
                )
                .flatMap(s -> s)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator.comparing(RegisteredResource::getPath))
                .collect(Collectors.toList());
    }

    private void logResources(List<RegisteredResource> resources) {
        if (resources.isEmpty()) {
            logger.warn("No resources registered");
            return;
        }

        String list = Stream.concat(
                Stream.of(""),
                resources.stream()
                        .map(RegisteredResource::toString)
        ).collect(Collectors.joining(String.format("%n\t")));

        logger.info("The following paths were found: {}", list);
    }

    private void logStaticFiles(Set<StaticFiles> staticFiles) {
        if (staticFiles.isEmpty()) return;

        int pathWidth = 0;
        int prefixWidth = 0;

        for (StaticFiles s : staticFiles) {
            String path = s.getPath().startsWith("/") ? s.getPath() : "/" + s.getPath();

            if (path.length() > pathWidth) {
                pathWidth = path.length();
            }

            if (s.getPrefix().length() > prefixWidth) {
                prefixWidth = s.getPrefix().length();
            }
        }

        int finalPathWidth = pathWidth;
        int finalPrefixWidth = prefixWidth;
        String list = Stream.concat(
                Stream.of(""),
                staticFiles.stream()
                        .map(s -> String.format(
                                "%-"
                                        + finalPathWidth
                                        + "s -> %-"
                                        + finalPrefixWidth
                                        + "s (%s)",
                                s.getPath().startsWith("/") ? s.getPath() : "/" + s.getPath(),
                                s.getPrefix(),
                                s.getMethodAccess().getLevel()
                        ))
        ).collect(Collectors.joining(String.format("%n\t")));


        logger.info("The following static paths were found: {}", list);
    }
}
