package fi.jubic.resteasy.auth;

import io.undertow.server.HttpServerExchange;

import javax.ws.rs.container.ContainerRequestContext;

public interface ITokenParser {
    String parse(ContainerRequestContext containerRequestContext);
    String parse(HttpServerExchange httpServerExchange);
}
