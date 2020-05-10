package fi.jubic.snoozy.auth;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.util.function.Supplier;

public interface Authentication<P extends UserPrincipal> {
    /**
     * The {@link Authenticator} is responsible for mapping a token to a {@link UserPrincipal}.
     */
    Authenticator<P> getAuthenticator();

    /**
     * The {@link Authorizer} is responsible for checking {@link UserPrincipal} authorization
     * against a provided role.
     */
    Authorizer<P> getAuthorizer();

    /**
     * The {@link TokenParser} is responsible for extracting the authentication token from a
     * {@link HttpServletRequest}.
     */
    TokenParser getTokenParser();

    /**
     * Most implementations require a {@link Class} reference for the {@link UserPrincipal}.
     */
    Class<P> getUserClass();

    /**
     * A custom unauthorized response can be set to provide consistent behavior for both static
     * files and JAX-RS responses.
     */
    default Supplier<Response> getUnauthorized() {
        return () -> Response.status(Response.Status.UNAUTHORIZED).build();
    }

    /**
     * A custom forbidden response can be set to provide consistent behavior for both static files
     * and JAX-RS responses.
     */
    default Supplier<Response> getForbidden() {
        return () -> Response.status(Response.Status.FORBIDDEN).build();
    }

    default AuthenticationBuilder<P> toBuilder() {
        return new AuthenticationBuilder<>(
                getAuthenticator(),
                getAuthorizer(),
                getTokenParser(),
                getUserClass(),
                getUnauthorized(),
                getForbidden()
        );
    }

    static <P extends UserPrincipal> AuthenticationBuilder<P> builder() {
        return new AuthenticationBuilder<P>()
                .setUnauthorized(
                        () -> Response.status(Response.Status.UNAUTHORIZED)
                                .build()
                );
    }
}
