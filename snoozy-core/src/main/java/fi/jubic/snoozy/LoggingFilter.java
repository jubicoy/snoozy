package fi.jubic.snoozy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final Logger logger = LoggerFactory.getLogger(
        LoggingFilter.class
    );
    private static final String START_TIME = "start-time";

    @SuppressWarnings("unused")
    @Context
    private HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        containerRequestContext.setProperty(START_TIME, System.currentTimeMillis());
    }

    @Override
    public void filter(
            ContainerRequestContext containerRequestContext,
            ContainerResponseContext containerResponseContext
    ) {
        long startTime = System.currentTimeMillis();

        try {
            startTime = (long) containerRequestContext.getProperty(START_TIME);
        } catch (NullPointerException ignored) {

        }

        long duration = System.currentTimeMillis() - startTime;

        logger.info(
                "{} {} {} {} {} {}",
                containerRequestContext.getMethod(),
                String.format(
                        "%s%s",
                        request.getContextPath(),
                        containerRequestContext.getUriInfo()
                                .getPath()
                ),
                containerResponseContext.getStatus(),
                request.getRemoteHost(),
                containerRequestContext.getHeaderString(HttpHeaders.USER_AGENT),
                String.format("%dms", duration)
        );
    }
}
