package fi.jubic.snoozy;

import java.util.Set;

public abstract class Application extends javax.ws.rs.core.Application {
    public abstract Set<StaticFiles> getStaticFiles();
}
