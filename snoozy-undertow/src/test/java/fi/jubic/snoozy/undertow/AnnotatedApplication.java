package fi.jubic.snoozy.undertow;

import fi.jubic.snoozy.Application;

import javax.ws.rs.ApplicationPath;
import java.util.Collections;
import java.util.Set;

@ApplicationPath("/test")
public class AnnotatedApplication extends Application {
    @Override
    public Set<Object> getSingletons() {
        return Collections.singleton(new TestResource());
    }
}