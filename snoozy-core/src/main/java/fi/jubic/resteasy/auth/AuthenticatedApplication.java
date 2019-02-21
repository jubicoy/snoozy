package fi.jubic.resteasy.auth;

import fi.jubic.resteasy.Application;

public abstract class AuthenticatedApplication<P extends UserPrincipal> extends Application {
    public abstract Authentication<P> getAuthentication();
}
