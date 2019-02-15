package fi.jubic.resteasy.server;

import fi.jubic.resteasy.Application;
import fi.jubic.resteasy.LoggingFilter;
import fi.jubic.resteasy.auth.AuthenticatedApplication;
import fi.jubic.resteasy.auth.UserPrincipal;

import javax.ws.rs.ApplicationPath;
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
        ApplicationAdapter applicationAdapter = new ApplicationAdapter(application);
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

    private void setApplicationPath() {
        Class<?> clazz = application.getClass();

        if (!clazz.isAnnotationPresent(ApplicationPath.class)) {
            return;
        }

        try {
            Method method = Class.class.getDeclaredMethod("annotationData");
            method.setAccessible(true);

            Object annotationData = method.invoke(ApplicationAdapter.class);
            Field annotations = annotationData.getClass().getDeclaredField("annotations");
            annotations.setAccessible(true);

            Map<Class<? extends Annotation>, Annotation> map = new HashMap<>();
            map.put(ApplicationPath.class, clazz.getAnnotation(ApplicationPath.class));

            annotations.set(annotationData, map);
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
