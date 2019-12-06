package fi.jubic.snoozy.logging;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;

public class NoopLoggingFilter implements LoggingFilter {
    @Override
    public void filter(
            ContainerRequestContext containerRequestContext,
            ContainerResponseContext containerResponseContext
    ) {

    }
}
