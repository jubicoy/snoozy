package fi.jubic.snoozy.server;

import javax.ws.rs.container.ContainerRequestContext;
import java.lang.reflect.Method;

public interface ResourceMethodGetter {
    Method getMethod(ContainerRequestContext requestContext);
}
