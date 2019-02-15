package fi.jubic.resteasy.auth;

public interface Authorizer<P extends UserPrincipal> {
    boolean authorize(P principal, String role);
}
