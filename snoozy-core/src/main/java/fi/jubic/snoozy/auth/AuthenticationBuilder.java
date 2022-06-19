package fi.jubic.snoozy.auth;

import jakarta.ws.rs.core.Response;

import java.util.function.Supplier;

public class AuthenticationBuilder<P extends UserPrincipal> {
    private final Authenticator<P> authenticator;
    private final Authorizer<P> authorizer;
    private final TokenParser tokenParser;
    private final Class<P> userClass;
    private final Supplier<Response> unauthorized;
    private final Supplier<Response> forbidden;

    AuthenticationBuilder() {
        this(null, null, null, null, null, null);
    }

    AuthenticationBuilder(
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

    public AuthenticationBuilder<P> setAuthenticator(Authenticator<P> authenticator) {
        return new AuthenticationBuilder<>(
                authenticator,
                authorizer,
                tokenParser,
                userClass,
                unauthorized,
                forbidden
        );
    }

    public AuthenticationBuilder<P> setAuthorizer(Authorizer<P> authorizer) {
        return new AuthenticationBuilder<>(
                authenticator,
                authorizer,
                tokenParser,
                userClass,
                unauthorized,
                forbidden
        );
    }

    public AuthenticationBuilder<P> setTokenParser(TokenParser tokenParser) {
        return new AuthenticationBuilder<>(
                authenticator,
                authorizer,
                tokenParser,
                userClass,
                unauthorized,
                forbidden
        );
    }

    public AuthenticationBuilder<P> setUserClass(Class<P> userClass) {
        return new AuthenticationBuilder<>(
                authenticator,
                authorizer,
                tokenParser,
                userClass,
                unauthorized,
                forbidden
        );
    }

    public AuthenticationBuilder<P> setUnauthorized(Supplier<Response> unauthorized) {
        return new AuthenticationBuilder<>(
                authenticator,
                authorizer,
                tokenParser,
                userClass,
                unauthorized,
                forbidden
        );
    }

    public AuthenticationBuilder<P> setForbidden(Supplier<Response> forbidden) {
        return new AuthenticationBuilder<>(
                authenticator,
                authorizer,
                tokenParser,
                userClass,
                unauthorized,
                forbidden
        );
    }

    public Authentication<P> build() {
        return new DefaultAuthentication<>(
                authenticator,
                authorizer,
                tokenParser,
                userClass,
                unauthorized,
                forbidden
        );
    }
}
