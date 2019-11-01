package fi.jubic.snoozy;

public interface MultipartConfig {
    /**
     * Gets the directory location where files will be stored.
     */
    default String getCacheLocation() {
        return "";
    }

    /**
     * Gets the maximum size allowed for uploaded files.
     */
    default long getMaxFileSize() {
        return 20 * 1024 * 1024;
    }

    /**
     * Gets the maximum size allowed for multipart/form-data requests.
     */
    default long getMaxRequestSize() {
        return 22 * 1024 * 1024;
    }

    /**
     * Gets the size threshold after which files will be written to disk.
     */
    default int getSizeThreshold() {
        return 1024 * 1024;
    }
}
