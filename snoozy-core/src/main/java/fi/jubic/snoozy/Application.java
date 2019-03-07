package fi.jubic.snoozy;

import fi.jubic.snoozy.websocket.WebSocketHandler;

import java.util.Collections;
import java.util.Set;

/**
 * Extended JAX-RS {@link javax.ws.rs.core.Application} providing static files
 * and websockets.
 */
public abstract class Application extends javax.ws.rs.core.Application {
    public Set<StaticFiles> getStaticFiles() {
        return Collections.emptySet();
    }
    public Set<WebSocketHandler> getWebSocketHandlers() {
        return Collections.emptySet();
    }
}
