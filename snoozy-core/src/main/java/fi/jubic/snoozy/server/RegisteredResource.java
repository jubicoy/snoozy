package fi.jubic.snoozy.server;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.UriBuilder;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.stream.Stream;

public class RegisteredResource {
    private final String method;
    private final String path;
    private final String resource;

    private RegisteredResource(
            String method,
            String path,
            String resource
    ) {
        this.method = method;
        this.path = path;
        this.resource = resource;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getResource() {
        return resource;
    }

    @Override
    public String toString() {
        return String.format("%-6s %s (%s)", method, path, resource);
    }

    /**
     * Parse resource method definition from an annotated Method.
     */
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
        }
        else if (method.isAnnotationPresent(PUT.class)) {
            httpMethod = "PUT";
        }
        else if (method.isAnnotationPresent(PATCH.class)) {
            httpMethod = "PATCH";
        }
        else if (method.isAnnotationPresent(DELETE.class)) {
            httpMethod = "DELETE";
        }

        Object[] pathParams = Stream.of(method.getParameterAnnotations())
                .flatMap(Stream::of)
                .filter(a -> a.annotationType() == PathParam.class)
                .map(a -> String.format("{%s}", ((PathParam) a).value()))
                .toArray();

        return Optional.of(
                new RegisteredResource(
                        httpMethod,
                        path.build(pathParams).getPath(),
                        clazz.getName()
                )
        );
    }
}
