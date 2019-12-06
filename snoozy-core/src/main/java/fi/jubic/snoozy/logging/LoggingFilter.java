package fi.jubic.snoozy.logging;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseFilter;

public interface LoggingFilter extends ContainerRequestFilter, ContainerResponseFilter {
    @Override
    default void filter(ContainerRequestContext containerRequestContext) {

    }
}
