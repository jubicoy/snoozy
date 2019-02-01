package fi.jubic.resteasy.auth;

import java.security.Principal;

public interface UserPrincipal extends Principal {
    String getRole();
}
