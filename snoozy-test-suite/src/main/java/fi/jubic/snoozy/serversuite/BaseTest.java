package fi.jubic.snoozy.serversuite;

import fi.jubic.snoozy.Server;

public interface BaseTest<T extends Server> {
    T instance();
}
