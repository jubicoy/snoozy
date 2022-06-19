package fi.jubic.snoozy.logging;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseFilter;

public interface LoggingFilter extends ContainerRequestFilter, ContainerResponseFilter {
    @Override
    default void filter(ContainerRequestContext containerRequestContext) {

    }
}
