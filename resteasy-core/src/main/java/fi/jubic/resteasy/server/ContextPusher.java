package fi.jubic.resteasy.server;

public interface ContextPusher {
    <T> void push(Class<T> klass, T object);
}
