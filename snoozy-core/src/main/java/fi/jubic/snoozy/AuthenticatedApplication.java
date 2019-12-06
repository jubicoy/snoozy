package fi.jubic.snoozy;

import fi.jubic.snoozy.Application;
import fi.jubic.snoozy.auth.Authentication;
import fi.jubic.snoozy.auth.UserPrincipal;

public interface AuthenticatedApplication<P extends UserPrincipal> extends Application {
    Authentication<P> getAuthentication();
}
