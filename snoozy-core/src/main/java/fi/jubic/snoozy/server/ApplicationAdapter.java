package fi.jubic.snoozy.server;

import fi.jubic.snoozy.Application;
import fi.jubic.snoozy.LoggingFilter;
import fi.jubic.snoozy.auth.AuthenticatedApplication;
import fi.jubic.snoozy.auth.UserPrincipal;

import javax.ws.rs.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApplicationAdapter extends javax.ws.rs.core.Application {
    private final Application application;
    private Set<Object> filters;

    public ApplicationAdapter(Application application) {
        this.application = application;
        this.filters = new HashSet<>();
        this.filters.add(new LoggingFilter());
    }

    public <P extends UserPrincipal> ApplicationAdapter(
            AuthenticatedApplication<P> application,
            AuthFilterAdapter<P> authFilterAdapter
    ) {
        this(application);

        filters.add(authFilterAdapter);
    }

    @Override
    public Set<Object> getSingletons() {
        return Stream.concat(
                filters.stream(),
                application.getSingletons().stream()
        ).collect(Collectors.toSet());
    }

    public List<RegisteredResource> getRegisteredResources() {
        String prefix;
        var applicationClass = application.getClass();

        if (applicationClass.isAnnotationPresent(ApplicationPath.class)) {
            prefix = applicationClass.getAnnotation(ApplicationPath.class)
                    .value();
        } else {
            prefix = "/";
        }

        return Stream.concat(
                this.getSingletons()
                        .stream()
                        .map(Object::getClass),
                application.getClasses()
                        .stream()
        )
                .map(c -> Stream.of(c.getMethods())
                        .map(m -> RegisteredResource.of(
                                prefix,
                                m
                        ))
                )
                .flatMap(s -> s)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator.comparing(RegisteredResource::path))
                .collect(Collectors.toList());
    }
}
