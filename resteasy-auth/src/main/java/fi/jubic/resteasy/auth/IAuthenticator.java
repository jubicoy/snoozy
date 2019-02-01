package fi.jubic.resteasy.auth;

import java.util.Optional;

public interface IAuthenticator<U extends UserPrincipal> {
    Optional<U> authenticate(String token);
}
