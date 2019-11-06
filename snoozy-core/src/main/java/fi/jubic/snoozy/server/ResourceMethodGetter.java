package fi.jubic.snoozy.server;

import java.lang.reflect.Method;
import javax.ws.rs.container.ContainerRequestContext;

public interface ResourceMethodGetter {
    Method getMethod(ContainerRequestContext requestContext);
}
