package fi.jubic.snoozy.auth;

import java.util.function.Supplier;
import javax.ws.rs.core.Response;

public class Authentication<P extends UserPrincipal> {
    private final Authenticator<P> authenticator;
    private final Authorizer<P> authorizer;
    private final TokenParser tokenParser;
    private final Class<P> userClass;
    private final Supplier<Response> unauthorized;

    private Authentication(
            Authenticator<P> authenticator,
            Authorizer<P> authorizer,
            TokenParser tokenParser,
            Class<P> userClass,
            Supplier<Response> unauthorized
    ) {
        this.authenticator = authenticator;
        this.authorizer = authorizer;
        this.tokenParser = tokenParser;
        this.userClass = userClass;
        this.unauthorized = unauthorized;
    }

    @Deprecated
    public Authenticator<P> authenticator() {
        return getAuthenticator();
    }

    public Authenticator<P> getAuthenticator() {
        return authenticator;
    }

    @Deprecated
    public Authorizer<P> authorizer() {
        return getAuthorizer();
    }

    public Authorizer<P> getAuthorizer() {
        return authorizer;
    }

    @Deprecated
    public TokenParser tokenParser() {
        return getTokenParser();
    }

    public TokenParser getTokenParser() {
        return tokenParser;
    }

    @Deprecated
    public Class<P> userClass() {
        return getUserClass();
    }

    public Class<P> getUserClass() {
        return userClass;
    }

    public Supplier<Response> getUnauthorized() {
        return unauthorized;
    }

    public static <P extends UserPrincipal> Builder<P> builder() {
        return new Builder<P>()
                .setUnauthorized(
                        () -> Response.status(Response.Status.UNAUTHORIZED)
                                .build()
                );
    }

    public static class Builder<P extends UserPrincipal> {
        private final Authenticator<P> authenticator;
        private final Authorizer<P> authorizer;
        private final TokenParser tokenParser;
        private final Class<P> userClass;
        private final Supplier<Response> unauthorized;

        private Builder() {
            this(null, null, null, null, null);
        }

        private Builder(
                Authenticator<P> authenticator,
                Authorizer<P> authorizer,
                TokenParser tokenParser,
                Class<P> userClass,
                Supplier<Response> unauthorized
        ) {
            this.authenticator = authenticator;
            this.authorizer = authorizer;
            this.tokenParser = tokenParser;
            this.userClass = userClass;
            this.unauthorized = unauthorized;
        }

        public Builder<P> setAuthenticator(Authenticator<P> authenticator) {
            return new Builder<>(
                    authenticator,
                    authorizer,
                    tokenParser,
                    userClass,
                    unauthorized
            );
        }

        public Builder<P> setAuthorizer(Authorizer<P> authorizer) {
            return new Builder<>(
                    authenticator,
                    authorizer,
                    tokenParser,
                    userClass,
                    unauthorized
            );
        }

        public Builder<P> setTokenParser(TokenParser tokenParser) {
            return new Builder<>(
                    authenticator,
                    authorizer,
                    tokenParser,
                    userClass,
                    unauthorized
            );
        }

        public Builder<P> setUserClass(Class<P> userClass) {
            return new Builder<>(
                    authenticator,
                    authorizer,
                    tokenParser,
                    userClass,
                    unauthorized
            );
        }

        public Builder<P> setUnauthorized(Supplier<Response> unauthorized) {
            return new Builder<>(
                    authenticator,
                    authorizer,
                    tokenParser,
                    userClass,
                    unauthorized
            );
        }

        public Authentication<P> build() {
            return new Authentication<>(
                    authenticator,
                    authorizer,
                    tokenParser,
                    userClass,
                    unauthorized
            );
        }
    }
}
