package fi.jubic.snoozy.logging;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultLoggingFilter implements LoggingFilter {
    private static final Logger logger = LoggerFactory.getLogger(
            DefaultLoggingFilter.class
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
        }
        catch (NullPointerException ignored) {

        }

        long duration = System.currentTimeMillis() - startTime;

        logger.info(
                "{} {}{} {} {} {} {}ms",
                containerRequestContext.getMethod()
                        .replaceAll("[\r\n]",""),
                request.getContextPath()
                        .replaceAll("[\r\n]",""),
                containerRequestContext.getUriInfo()
                        .getPath()
                        .replaceAll("[\r\n]",""),
                containerResponseContext.getStatus(),
                request.getRemoteHost()
                        .replaceAll("[\r\n]",""),
                containerRequestContext.getHeaderString(HttpHeaders.USER_AGENT)
                        .replaceAll("[\r\n]",""),
                duration
        );
    }
}
