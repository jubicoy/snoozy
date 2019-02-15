package fi.jubic.resteasy;

import fi.jubic.resteasy.auth.AuthenticatedApplication;
import fi.jubic.resteasy.auth.UserPrincipal;

public interface Server {
    void start(
            Application application,
            ServerConfigurator serverConfigurator
    );

    <P extends UserPrincipal> void start(
        AuthenticatedApplication<P> application,
        ServerConfigurator serverConfigurator
    );
}
