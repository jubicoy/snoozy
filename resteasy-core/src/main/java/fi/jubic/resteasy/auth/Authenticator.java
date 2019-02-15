package fi.jubic.resteasy.auth;

import java.util.Optional;

public interface Authenticator<P extends UserPrincipal> {
    Optional<P> authenticate(String token);
}
