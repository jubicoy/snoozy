package fi.jubic.snoozy;

import fi.jubic.snoozy.auth.AuthenticatedApplication;
import fi.jubic.snoozy.auth.UserPrincipal;

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
