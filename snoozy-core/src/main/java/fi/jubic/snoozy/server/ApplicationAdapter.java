package fi.jubic.snoozy.server;

import fi.jubic.snoozy.Application;
import fi.jubic.snoozy.LoggingFilter;
import fi.jubic.snoozy.auth.AuthenticatedApplication;
import fi.jubic.snoozy.auth.UserPrincipal;

import javax.ws.rs.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApplicationAdapter extends javax.ws.rs.core.Application {
    private final Application application;
    private Set<Object> filters;

    private ApplicationAdapter(Application application) {
        this.application = application;
        this.filters = new HashSet<>();
        this.filters.add(new LoggingFilter());

        setApplicationPath();
    }

    public static ApplicationAdapter of(Application application) {
        return new ApplicationAdapter(application);
    }

    public static <P extends UserPrincipal> ApplicationAdapter of(
            AuthenticatedApplication<P> application,
            AuthFilterAdapter<P> authFilterAdapter
    ) {
        ApplicationAdapter applicationAdapter = new ApplicationAdapter(
                application
        );
        applicationAdapter.filters.add(authFilterAdapter);

        return applicationAdapter;
    }

    @Override
    public Set<Object> getSingletons() {
        return Stream.concat(
                filters.stream(),
                application.getSingletons().stream()
        ).collect(Collectors.toSet());
    }

    public List<RegisteredResource> getRegisteredResources() {
        String prefix = application.getClass()
                .getAnnotation(ApplicationPath.class)
                .value();

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

    private void setApplicationPath() {
        Class<?> clazz = application.getClass();

        if (!clazz.isAnnotationPresent(ApplicationPath.class)) {
            return;
        }

        try {
            Method method = Class.class.getDeclaredMethod("annotationData");
            method.setAccessible(true);

            Object annotationData = method.invoke(ApplicationAdapter.class);
            Field annotations = annotationData.getClass()
                    .getDeclaredField("annotations");
            annotations.setAccessible(true);

            Map<Class<? extends Annotation>, Annotation> map = new HashMap<>();
            map.put(
                    ApplicationPath.class,
                    clazz.getAnnotation(ApplicationPath.class)
            );

            annotations.set(annotationData, map);
        } catch (
                NoSuchFieldException
                | IllegalAccessException
                | NoSuchMethodException
                | InvocationTargetException e
        ) {
            e.printStackTrace();
        }
    }
}
