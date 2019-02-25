package fi.jubic.snoozy.auth;

import fi.jubic.snoozy.Application;

public abstract class AuthenticatedApplication<P extends UserPrincipal> extends Application {
    public abstract Authentication<P> getAuthentication();
}
