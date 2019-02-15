package fi.jubic.resteasy;

import java.util.Set;

public abstract class Application extends javax.ws.rs.core.Application {
    public abstract Set<StaticFiles> getStaticFiles();
}
