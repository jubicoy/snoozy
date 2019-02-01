package fi.jubic.resteasy.auth;

import java.util.Optional;

public interface IAuthenticator<P extends UserPrincipal> {
    Optional<P> authenticate(String token);
}
