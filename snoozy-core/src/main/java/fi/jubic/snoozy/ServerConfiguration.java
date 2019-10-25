package fi.jubic.snoozy;

import fi.jubic.easyconfig.annotations.EasyConfigProperty;

public class ServerConfiguration {
    private String hostname;
    private int port;

    public ServerConfiguration() {

    }

    public ServerConfiguration(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    @EasyConfigProperty(value = "HOST", defaultValue = "localhost")
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    @EasyConfigProperty(value = "PORT", defaultValue = "8080")
    public void setPort(int port) {
        this.port = port;
    }
}
