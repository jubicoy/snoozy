package fi.jubic.resteasy.auth.implementation;

import fi.jubic.resteasy.auth.UserPrincipal;
import fi.jubic.resteasy.auth.Authorizer;

import java.util.Objects;

public class DefaultAuthorizer<P extends UserPrincipal> implements Authorizer<P> {
    @Override
    public boolean authorize(P principal, String role) {
        return Objects.equals(principal.getRole(), role);
    }
}
