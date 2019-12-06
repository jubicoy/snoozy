package fi.jubic.snoozy.test;

import fi.jubic.snoozy.Application;
import fi.jubic.snoozy.logging.LoggingFilter;
import fi.jubic.snoozy.logging.NoopLoggingFilter;

import java.util.Optional;

/**
 * A less verbose Application variant for testing purposes.
 */
public abstract class TestApplication implements Application {
    @Override
    public Optional<String> getBanner() {
        return Optional.empty();
    }

    @Override
    public LoggingFilter getLoggingFilter() {
        return new NoopLoggingFilter();
    }
}
