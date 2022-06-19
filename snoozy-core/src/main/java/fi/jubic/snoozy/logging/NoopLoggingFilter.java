package fi.jubic.snoozy.logging;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;

public class NoopLoggingFilter implements LoggingFilter {
    @Override
    public void filter(
            ContainerRequestContext containerRequestContext,
            ContainerResponseContext containerResponseContext
    ) {

    }
}
