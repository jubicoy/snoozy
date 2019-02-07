package fi.jubic.resteasy.server;

import fi.jubic.resteasy.auth.Auth;
import fi.jubic.resteasy.auth.AuthFilter;
import fi.jubic.resteasy.auth.UserPrincipal;

import javax.annotation.Nullable;
import javax.ws.rs.container.ContainerRequestFilter;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Application<U extends UserPrincipal> extends javax.ws.rs.core.Application {
    public abstract Set<Object> getResources();

    @Nullable
    public abstract Auth<U> getAuth();
    @Nullable
    public abstract StaticFiles getStaticFiles();

    @Override
    public Set<Object> getSingletons() {
        Optional<ContainerRequestFilter> filter = getAuthFilter();

        return filter.map(
                containerRequestFilter -> Stream.concat(getResources().stream(), Stream.of(containerRequestFilter)
        )
                .collect(Collectors.toSet()))
                .orElseGet(this::getResources);

    }

    private Optional<ContainerRequestFilter> getAuthFilter() {
        Auth<U> auth = getAuth();

        if (auth == null) {
            return Optional.empty();
        }

        AuthFilter.Builder<U> builder = AuthFilter.<U>builder()
                .setAuthenticator(auth.authenticator())
                .setPrincipalClass(auth.userClass());

        if (auth.authorizer() != null) {
            builder = builder.setAuthorizer(auth.authorizer());
        }

        if (auth.tokenParser() != null) {
            builder = builder.setTokenParser(auth.tokenParser());
        }

        return Optional.of(builder.build());
    }
}
