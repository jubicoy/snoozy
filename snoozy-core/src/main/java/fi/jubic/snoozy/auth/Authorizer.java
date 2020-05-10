package fi.jubic.snoozy.auth;

public interface Authorizer<P extends UserPrincipal> {
    /**
     * Authorize a {@link UserPrincipal}.
     *
     * @param principal The principal resolved by the {@link Authenticator}.
     * @param role Required role.
     * @return True if the principal is authorized, or false for a HTTP 403.
     */
    boolean authorize(P principal, String role);
}
