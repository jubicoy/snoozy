package fi.jubic.snoozy.test;

import fi.jubic.snoozy.auth.AuthenticatedApplication;
import fi.jubic.snoozy.auth.UserPrincipal;
import fi.jubic.snoozy.logging.LoggingFilter;
import fi.jubic.snoozy.logging.NoopLoggingFilter;

import java.util.Optional;

/**
 * A less verbose AuthenticatedApplication variant for testing purposes.
 */
public abstract class TestAuthenticatedApplication<P extends UserPrincipal>
        implements AuthenticatedApplication<P> {
    @Override
    public Optional<String> getBanner() {
        return Optional.empty();
    }

    @Override
    public LoggingFilter getLoggingFilter() {
        return new NoopLoggingFilter();
    }
}
