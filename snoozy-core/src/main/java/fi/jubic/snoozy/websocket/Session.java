package fi.jubic.snoozy.websocket;

public interface Session {
    void send(String message);

    void send(byte[] bytes);

    void close(CloseReason closeReason);

    void disconnect();
}

