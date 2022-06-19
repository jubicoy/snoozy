package fi.jubic.snoozy.auth;

import jakarta.ws.rs.core.Response;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class DefaultAuthentication<P extends UserPrincipal> implements Authentication<P> {
    private final Authenticator<P> authenticator;
    private final Authorizer<P> authorizer;
    private final TokenParser tokenParser;
    private final Class<P> userClass;
    private final Supplier<Response> unauthorized;
    private final Supplier<Response> forbidden;

    DefaultAuthentication(
            Authenticator<P> authenticator,
            Authorizer<P> authorizer,
            TokenParser tokenParser,
            Class<P> userClass,
            Supplier<Response> unauthorized,
            Supplier<Response> forbidden
    ) {
        this.authenticator = authenticator;
        this.authorizer = authorizer;
        this.tokenParser = tokenParser;
        this.userClass = userClass;
        this.unauthorized = unauthorized;
        this.forbidden = forbidden;
    }

    @Override
    public Authenticator<P> getAuthenticator() {
        return Objects.requireNonNull(authenticator);
    }

    @Override
    public Authorizer<P> getAuthorizer() {
        return Objects.requireNonNull(authorizer);
    }

    @Override
    public TokenParser getTokenParser() {
        return Objects.requireNonNull(tokenParser);
    }

    @Override
    public Class<P> getUserClass() {
        return Objects.requireNonNull(userClass);
    }

    @Override
    public Supplier<Response> getUnauthorized() {
        return Optional.ofNullable(unauthorized).orElseGet(Authentication.super::getUnauthorized);
    }

    @Override
    public Supplier<Response> getForbidden() {
        return Optional.ofNullable(forbidden).orElseGet(Authentication.super::getForbidden);
    }
}
