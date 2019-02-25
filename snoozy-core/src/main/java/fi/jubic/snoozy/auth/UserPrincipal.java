package fi.jubic.snoozy.auth;

import java.security.Principal;

public interface UserPrincipal extends Principal {
    String getRole();
}
