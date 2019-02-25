package fi.jubic.snoozy.auth;

public interface Authorizer<P extends UserPrincipal> {
    boolean authorize(P principal, String role);
}
