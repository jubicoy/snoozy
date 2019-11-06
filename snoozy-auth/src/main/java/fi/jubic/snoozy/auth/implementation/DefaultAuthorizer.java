package fi.jubic.snoozy.auth.implementation;

import fi.jubic.snoozy.auth.Authorizer;
import fi.jubic.snoozy.auth.UserPrincipal;

import java.util.Objects;

public class DefaultAuthorizer<P extends UserPrincipal> implements Authorizer<P> {
    @Override
    public boolean authorize(P principal, String role) {
        return Objects.equals(principal.getRole(), role);
    }
}
