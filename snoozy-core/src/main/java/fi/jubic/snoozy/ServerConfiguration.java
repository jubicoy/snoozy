package fi.jubic.snoozy;

public interface ServerConfiguration {
    String getHostname();
    int getPort();
    default MultipartConfig getMultipartConfig() {
        return new MultipartConfig() {};
    }
}
