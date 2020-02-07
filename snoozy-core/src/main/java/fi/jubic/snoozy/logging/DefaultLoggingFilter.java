package fi.jubic.snoozy.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import java.util.Optional;

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
                String.format(
                        "%s %s%s %d %s %s %dms",
                        containerRequestContext.getMethod(),
                        request.getContextPath(),
                        containerRequestContext.getUriInfo()
                                .getPath(),
                        containerResponseContext.getStatus(),
                        request.getRemoteHost(),
                        Optional
                                .ofNullable(
                                        containerRequestContext.getHeaderString(
                                                HttpHeaders.USER_AGENT
                                        )
                                )
                                .orElse("-"),
                        duration
                ).replaceAll("[\r\n]","")
        );
    }
}
