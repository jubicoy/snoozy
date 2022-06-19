package fi.jubic.snoozy.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        long duration = Optional.ofNullable(containerRequestContext.getProperty(START_TIME))
                .map(startTime -> System.currentTimeMillis() - (long)startTime)
                .orElse(-1L);

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
