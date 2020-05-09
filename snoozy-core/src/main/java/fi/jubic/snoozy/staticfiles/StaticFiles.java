package fi.jubic.snoozy.staticfiles;

import fi.jubic.snoozy.MethodAccess;
import fi.jubic.snoozy.filters.UrlRewrite;

import java.util.Optional;

public interface StaticFiles {
    /**
     * Absolute path prefix of the static files bundle. {@link javax.ws.rs.ApplicationPath} has no
     * effect on {@link StaticFiles} paths.
     *
     * @return Absolute URL path to static bundle root
     */
    String getPath();

    /**
     * Resource directory prefix for the static bundle. For example, prefix of "static" will expose
     * <pre>src/main/resources/static</pre> as a static bundle in a typical Maven project.
     *
     * @return Path to static bundle root in resources directory
     */
    String getPrefix();

    /**
     * ClassLoader to use when loading static files.
     */
    ClassLoader getClassLoader();

    /**
     * Authentication and authorization rules for the static files.
     */
    MethodAccess getMethodAccess();

    /**
     * Rewrite rules within the static files bundle.
     */
    default Optional<UrlRewrite> getRewrite() {
        return Optional.empty();
    }

    default StaticFilesBuilder toBuilder() {
        return new StaticFilesBuilder(
                getPath(),
                getPrefix(),
                getClassLoader(),
                getMethodAccess(),
                getRewrite().orElse(null)
        );
    }

    static StaticFilesBuilder builder() {
        return new StaticFilesBuilder();
    }
}
