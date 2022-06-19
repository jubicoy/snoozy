package fi.jubic.snoozy.websocket;

import jakarta.servlet.http.HttpServletRequest;

public interface WebSocketHandler {
    void onConnect(HttpServletRequest request, Session session);

    void onMessage(Session session, String message);

    void onClose(Session session, CloseReason closeReason);

    void onError(Session session, Throwable throwable);

    /**
     * Construct a no-op WebSocket handler.
     */
    static WebSocketHandler defaultHandler() {
        return new WebSocketHandler() {
            @Override
            public void onConnect(HttpServletRequest request, Session session) {
                session.close(
                        CloseReason.of(
                                CloseReason.StatusCode.NORMAL_CLOSURE,
                                "Not implemented"
                        )
                );
            }

            @Override
            public void onMessage(Session session, String message) {

            }

            @Override
            public void onClose(Session session, CloseReason closeReason) {

            }

            @Override
            public void onError(Session session, Throwable throwable) {

            }
        };
    }
}

