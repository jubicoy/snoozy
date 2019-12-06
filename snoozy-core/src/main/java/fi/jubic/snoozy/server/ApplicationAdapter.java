package fi.jubic.snoozy.server;

import fi.jubic.snoozy.Application;
import fi.jubic.snoozy.LoggingFilter;
import fi.jubic.snoozy.auth.AuthenticatedApplication;
import fi.jubic.snoozy.auth.UserPrincipal;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.ApplicationPath;

/**
 * Adapter class for adding built-in features to a {@link fi.jubic.snoozy.Application}.
 */
public class ApplicationAdapter extends javax.ws.rs.core.Application {
    private final Application application;
    private Set<Object> filters;

    /**
     * Wrap a regular Application injecting the following built-ins.
     *
     * <ul>
     *     <li>Request logging</li>
     * </ul>
     */
    public ApplicationAdapter(Application application) {
        this.application = application;
        this.filters = new HashSet<>();
        this.filters.add(new LoggingFilter());
    }

    /**
     * Wrap an authenticated Application injecting the following built-ins.
     *
     * <ul>
     *     <li>Request logging</li>
     *     <li>Auth filter</li>
     * </ul>
     *
     * @param <P> User principal type
     */
    public <P extends UserPrincipal> ApplicationAdapter(
            AuthenticatedApplication<P> application,
            AuthFilterAdapter<P> authFilterAdapter
    ) {
        this(application);

        filters.add(authFilterAdapter);
    }

    public Class<?> getApplicationClass() {
        return application.getClass();
    }

    @Override
    public Set<Object> getSingletons() {
        return Stream.concat(
                filters.stream(),
                application.getSingletons().stream()
        ).collect(Collectors.toSet());
    }

    /**
     * List registered resource methods.
     */
    public List<RegisteredResource> getRegisteredResources() {
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
}
