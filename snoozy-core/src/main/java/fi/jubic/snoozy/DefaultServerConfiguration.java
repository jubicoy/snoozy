package fi.jubic.snoozy;

public class DefaultServerConfiguration implements ServerConfiguration {
    private String hostname;
    private int port;

    public DefaultServerConfiguration() {

    }

    public DefaultServerConfiguration(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public String getHostname() {
        return hostname;
    }

    @Override
    public int getPort() {
        return port;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
