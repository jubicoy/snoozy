package fi.jubic.resteasy.server;

import org.apache.logging.log4j.LogManager;
import org.joda.time.DateTime;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final String START_TIME = "start-time";

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
        long startTime = (long) containerRequestContext.getProperty(START_TIME);
        long duration = System.currentTimeMillis() - startTime;

        LogManager.getLogger(LoggingFilter.class).info(
                "[{}] {} {} {} {} {} {}",
                DateTime.now().toDateTimeISO(),
                containerRequestContext.getMethod(),
                containerRequestContext.getUriInfo()
                        .getPath(),
                containerResponseContext.getStatus(),
                request.getRemoteHost(),
                containerRequestContext.getHeaderString(HttpHeaders.USER_AGENT),
                String.format("%dms", duration)
        );
    }
}
