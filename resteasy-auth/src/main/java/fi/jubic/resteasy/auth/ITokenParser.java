package fi.jubic.resteasy.auth;

import javax.ws.rs.container.ContainerRequestContext;

public interface ITokenParser {
    String parse(ContainerRequestContext containerRequestContext);
}
