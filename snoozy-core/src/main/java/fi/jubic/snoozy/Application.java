package fi.jubic.snoozy;

import fi.jubic.snoozy.websocket.WebSocketHandler;

import java.util.Collections;
import java.util.Set;

/**
 * Basic application interface. Based loosely on {@link javax.ws.rs.core.Application}
 * and {@link javax.ws.rs.core.Application#getSingletons()}.
 */
public interface Application {
    Set<Object> getSingletons();

    default Set<StaticFiles> getStaticFiles() {
        return Collections.emptySet();
    }

    default Set<WebSocketHandler> getWebSocketHandlers() {
        return Collections.emptySet();
    }
}
