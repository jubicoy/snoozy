package fi.jubic.snoozy.server;

public interface ContextPusher {
    <T> void push(Class<T> klass, T object);
}
