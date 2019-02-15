package fi.jubic.resteasy.server;

import javax.ws.rs.container.ContainerRequestContext;
import java.lang.reflect.Method;

public interface ResourceMethodGetter {
    Method getMethod(ContainerRequestContext requestContext);
}
