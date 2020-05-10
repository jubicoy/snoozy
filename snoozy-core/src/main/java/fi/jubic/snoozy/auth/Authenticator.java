package fi.jubic.snoozy.auth;

import java.util.Optional;

public interface Authenticator<P extends UserPrincipal> {
    /**
     * Authenticate token and map it to a {@link UserPrincipal}.
     *
     * @param token Authentication token.
     * @return An {@link Optional} of the authenticated {@link UserPrincipal}, or empty to serve a
     *         HTTP 401.
     */
    Optional<P> authenticate(String token);
}
