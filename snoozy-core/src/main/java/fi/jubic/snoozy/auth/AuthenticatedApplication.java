package fi.jubic.snoozy.auth;

import fi.jubic.snoozy.Application;

public interface AuthenticatedApplication<P extends UserPrincipal> extends Application {
    Authentication<P> getAuthentication();
}
