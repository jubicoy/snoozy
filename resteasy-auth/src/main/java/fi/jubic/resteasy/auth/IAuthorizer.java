package fi.jubic.resteasy.auth;

public interface IAuthorizer<P extends UserPrincipal> {
    boolean authorize(P principal, String role);
}
