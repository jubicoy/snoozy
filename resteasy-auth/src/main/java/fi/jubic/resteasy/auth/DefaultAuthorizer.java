package fi.jubic.resteasy.auth;

import java.util.Objects;

public class DefaultAuthorizer<U extends UserPrincipal> implements IAuthorizer<U>  {
    @Override
    public boolean authorize(U principal, String role) {
        return Objects.equals(principal.getRole(), role);
    }
}
