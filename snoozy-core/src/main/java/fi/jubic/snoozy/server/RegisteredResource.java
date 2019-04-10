package fi.jubic.snoozy.server;

import fi.jubic.easyvalue.EasyProperty;
import fi.jubic.easyvalue.EasyValue;

import javax.ws.rs.*;
import javax.ws.rs.core.UriBuilder;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.stream.Stream;

@EasyValue
public abstract class RegisteredResource {
    @EasyProperty
    public abstract String method();
    @EasyProperty
    public abstract String path();
    @EasyProperty
    public abstract String resource();

    @Override
    public String toString() {
        return String.format("%-6s %s (%s)", method(), path(), resource());
    }

    public static Optional<RegisteredResource> of(
            String prefix,
            Method method
    ) {
        UriBuilder path = UriBuilder.fromPath(prefix);
        Class<?> clazz = method.getDeclaringClass();

        if (
                !method.isAnnotationPresent(GET.class)
                && !method.isAnnotationPresent(POST.class)
                && !method.isAnnotationPresent(PUT.class)
                && !method.isAnnotationPresent(PATCH.class)
                && !method.isAnnotationPresent(DELETE.class)
        ) {
            return Optional.empty();
        }

        if (clazz.isAnnotationPresent(Path.class)) {
            path = path.path(clazz);
        }

        if (method.isAnnotationPresent(Path.class)) {
            path = path.path(method);
        }

        String httpMethod = "GET";

        if (method.isAnnotationPresent(POST.class)) {
            httpMethod = "POST";
        } else if (method.isAnnotationPresent(PUT.class)) {
            httpMethod = "PUT";
        } else if (method.isAnnotationPresent(PATCH.class)) {
            httpMethod = "PATCH";
        } else if (method.isAnnotationPresent(DELETE.class)) {
            httpMethod = "DELETE";
        }

        Object[] pathParams = Stream.of(method.getParameterAnnotations())
                .flatMap(Stream::of)
                .filter(a -> a.annotationType() == PathParam.class)
                .map(a -> String.format("{%s}", ((PathParam) a).value()))
                .toArray();

        return Optional.of(
                RegisteredResource.builder()
                    .setMethod(httpMethod)
                    .setPath(path.build(pathParams).getPath())
                    .setResource(clazz.getName())
                    .build()
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends EasyValue_RegisteredResource.Builder {

    }
}
