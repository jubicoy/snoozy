package fi.jubic.resteasy.auth;

import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

@Provider
public class AuthFilter<U extends UserPrincipal> implements ContainerRequestFilter {
    private IAuthenticator<U> authenticator;
    private IAuthorizer<U> authorizer;
    private Class<U> clazz;

    private AuthFilter(IAuthenticator<U> authenticator, IAuthorizer<U> authorizer, Class<U> clazz) {
        this.authenticator = authenticator;
        this.authorizer = authorizer;
        this.clazz = clazz;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        Method method = ((ResourceMethodInvoker) containerRequestContext.getProperty(
                "org.jboss.resteasy.core.ResourceMethodInvoker"
        )).getMethod();

        if (method.isAnnotationPresent(DenyAll.class)) {
            unauthorized(containerRequestContext);
            return;
        }

        String authHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        Optional<U> user = authenticator.authenticate(authHeader);

        user.ifPresent(u -> ResteasyProviderFactory.pushContext(clazz, u));

        if (method.isAnnotationPresent(PermitAll.class)) {
            if (!user.isPresent()) {
                unauthorized(containerRequestContext);
                return;
            }

            return;
        }

        if (method.isAnnotationPresent(RolesAllowed.class)) {
            if (!user.isPresent()) {
                unauthorized(containerRequestContext);
                return;
            }

            if (isUnauthorized(method.getAnnotation(RolesAllowed.class).value(), user.get())) {
                unauthorized(containerRequestContext);
                return;
            }

            return;
        }

        Class<?> resource = method.getDeclaringClass();

        if (resource.isAnnotationPresent(DenyAll.class)) {
            unauthorized(containerRequestContext);
            return;
        }

        if (resource.isAnnotationPresent(PermitAll.class)) {
            if (!user.isPresent()) {
                unauthorized(containerRequestContext);
                return;
            }

            return;
        }

        if (resource.isAnnotationPresent(RolesAllowed.class)) {
            if (!user.isPresent()) {
                unauthorized(containerRequestContext);
                return;
            }

            if (isUnauthorized(resource.getAnnotation(RolesAllowed.class).value(), user.get())) {
                unauthorized(containerRequestContext);
            }
        }
    }

    private boolean isUnauthorized(String[] roles, U user) {
        return Arrays.stream(roles)
                .map(role -> !authorizer.authorize(user, role))
                .reduce(true, Boolean::logicalAnd);
    }

    private void unauthorized(ContainerRequestContext containerRequestContext) {
        containerRequestContext.abortWith(
                Response.status(Response.Status.FORBIDDEN)
                    .build()
        );
    }

    public static class Builder<U extends UserPrincipal> {
        private IAuthenticator<U> authenticator;
        private IAuthorizer<U> authorizer;
        private Class<U> clazz;

        public Builder() {
            authorizer = new DefaultAuthorizer<>();
        }

        public Builder<U> setAuthenticator(IAuthenticator<U> authenticator) {
            this.authenticator = authenticator;
            return this;
        }

        public Builder<U> setAuthorizer(IAuthorizer<U> authorizer) {
            this.authorizer = authorizer;
            return this;
        }

        public Builder<U> setPrincipalClass(Class<U> clazz) {
            this.clazz = clazz;
            return this;
        }

        public AuthFilter<U> build() {
            return new AuthFilter<>(authenticator, authorizer, clazz);
        }
    }
}
