package fi.jubic.snoozy.mappers.exceptions;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {
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
        if (exception instanceof InternalServerErrorException) {
            return Response.Status.INTERNAL_SERVER_ERROR;
        }

        throw new IllegalArgumentException("Unsupported WebApplicationException");
    }
}
