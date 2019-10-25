package fi.jubic.snoozy.test;

public interface ServerConsumer {
    void consume(String hostname, int port) throws Exception;
}
