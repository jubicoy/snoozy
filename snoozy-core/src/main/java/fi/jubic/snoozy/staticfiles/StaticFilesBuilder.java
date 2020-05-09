package fi.jubic.snoozy.staticfiles;

import fi.jubic.snoozy.MethodAccess;
import fi.jubic.snoozy.filters.UrlRewrite;

import java.util.Objects;

public class StaticFilesBuilder {
    private final String path;
    private final String prefix;
    private final ClassLoader classLoader;
    private final MethodAccess methodAccess;
    private final UrlRewrite rewrite;

    StaticFilesBuilder() {
        this(
                "/",
                "",
                null,
                null,
                null
        );
    }

    StaticFilesBuilder(
            String path,
            String prefix,
            ClassLoader classLoader,
            MethodAccess methodAccess,
            UrlRewrite rewrite
    ) {
        this.path = path;
        this.prefix = prefix;
        this.classLoader = classLoader;
        this.methodAccess = methodAccess;
        this.rewrite = rewrite;
    }

    public StaticFilesBuilder setPath(String path) {
        return new StaticFilesBuilder(
                path,
                prefix,
                classLoader,
                methodAccess,
                rewrite
        );
    }

    public StaticFilesBuilder setPrefix(String prefix) {
        return new StaticFilesBuilder(
                path,
                prefix,
                classLoader,
                methodAccess,
                rewrite
        );
    }

    public StaticFilesBuilder setClassLoader(ClassLoader classLoader) {
        return new StaticFilesBuilder(
                path,
                prefix,
                classLoader,
                methodAccess,
                rewrite
        );
    }

    public StaticFilesBuilder setMethodAccess(MethodAccess methodAccess) {
        return new StaticFilesBuilder(
                path,
                prefix,
                classLoader,
                methodAccess,
                rewrite
        );
    }

    public StaticFilesBuilder setRewrite(UrlRewrite rewrite) {
        return new StaticFilesBuilder(
                path,
                prefix,
                classLoader,
                methodAccess,
                rewrite
        );
    }

    public StaticFiles build() {
        return new DefaultStaticFiles(
                Objects.requireNonNull(path),
                Objects.requireNonNull(prefix),
                Objects.requireNonNull(classLoader),
                Objects.requireNonNull(methodAccess),
                rewrite
        );
    }
}
