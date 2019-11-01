package fi.jubic.snoozy.undertow;

import fi.jubic.snoozy.serversuite.ServerTestSuite;

public class SuiteTest implements ServerTestSuite<UndertowServer> {
    @Override
    public UndertowServer instance() {
        return new UndertowServer();
    }
}
