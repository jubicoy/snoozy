package fi.jubic.snoozy.test;

import java.net.URI;

public class UriBuilder {
    private final String hostname;
    private final int port;

    public UriBuilder(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public URI build(String path) {
        return URI.create(
                String.format(
                        "http://%s:%d%s",
                        hostname,
                        port,
                        path
                )
        );
    }
}
