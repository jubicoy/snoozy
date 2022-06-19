package fi.jubic.snoozy.mappers.exceptions;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotAcceptableException;
import jakarta.ws.rs.NotAllowedException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.RedirectionException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {
    private static final Logger LOGGER = LoggerFactory.getLogger(
            WebApplicationExceptionMapper.class
    );

    @SuppressWarnings("unused")
    @Context
    private HttpHeaders headers;

    @Override
    public Response toResponse(WebApplicationException exception) {
        Response.Status status = getResponseStatus(exception);

        Response.ResponseBuilder builder = Response.status(status)
                .type(headers.getMediaType())
                .entity(
                        new ExceptionView(
                                status.getStatusCode(),
                                exception.getMessage()
                        )
                );

        if (status == Response.Status.FOUND && exception instanceof RedirectionException) {
            builder = builder.header("Location", ((RedirectionException) exception).getLocation());
        }

        return builder.build();
    }

    private Response.Status getResponseStatus(
            WebApplicationException exception
    ) {
        if (exception instanceof RedirectionException) return Response.Status.FOUND;
        if (exception instanceof BadRequestException) return Response.Status.BAD_REQUEST;
        if (exception instanceof NotAuthorizedException) return Response.Status.UNAUTHORIZED;
        if (exception instanceof ForbiddenException) return Response.Status.FORBIDDEN;
        if (exception instanceof NotFoundException) return Response.Status.NOT_FOUND;
        if (exception instanceof NotAllowedException) return Response.Status.METHOD_NOT_ALLOWED;
        if (exception instanceof NotAcceptableException) return Response.Status.NOT_ACCEPTABLE;

        LOGGER.error("Internal error", exception);

        return Response.Status.INTERNAL_SERVER_ERROR;
    }
}
