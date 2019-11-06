package fi.jubic.snoozy;

import fi.jubic.snoozy.filters.UrlRewrite;

import java.util.Objects;
import javax.annotation.Nullable;

public class StaticFiles {
    private final String path;
    private final String prefix;
    private final ClassLoader classLoader;
    private final MethodAccess methodAccess;
    private final UrlRewrite rewrite;

    private StaticFiles(
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


    @Deprecated
    public String path() {
        return getPath();
    }

    public String getPath() {
        return path;
    }

    @Deprecated
    public String prefix() {
        return getPrefix();
    }

    public String getPrefix() {
        return prefix;
    }

    @Deprecated
    public ClassLoader classLoader() {
        return getClassLoader();
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Deprecated
    public MethodAccess methodAccess() {
        return getMethodAccess();
    }

    public MethodAccess getMethodAccess() {
        return methodAccess;
    }

    @Deprecated
    @Nullable
    public UrlRewrite rewrite() {
        return getRewrite();
    }

    public UrlRewrite getRewrite() {
        return rewrite;
    }

    public Builder toBuilder() {
        return new Builder(
                path,
                prefix,
                classLoader,
                methodAccess,
                rewrite
        );
    }

    public static Builder builder() {
        return new Builder()
                .setPath("/")
                .setPrefix("")
                .setMethodAccess(MethodAccess.authenticated());
    }

    public static class Builder {
        private final String path;
        private final String prefix;
        private final ClassLoader classLoader;
        private final MethodAccess methodAccess;
        private final UrlRewrite rewrite;

        private Builder() {
            this(
                    "/",
                    "",
                    null,
                    null,
                    null
            );
        }

        private Builder(
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

        public Builder setPath(String path) {
            return new Builder(
                    path,
                    prefix,
                    classLoader,
                    methodAccess,
                    rewrite
            );
        }

        public Builder setPrefix(String prefix) {
            return new Builder(
                    path,
                    prefix,
                    classLoader,
                    methodAccess,
                    rewrite
            );
        }

        public Builder setClassLoader(ClassLoader classLoader) {
            return new Builder(
                    path,
                    prefix,
                    classLoader,
                    methodAccess,
                    rewrite
            );
        }

        public Builder setMethodAccess(MethodAccess methodAccess) {
            return new Builder(
                    path,
                    prefix,
                    classLoader,
                    methodAccess,
                    rewrite
            );
        }

        public Builder setRewrite(UrlRewrite rewrite) {
            return new Builder(
                    path,
                    prefix,
                    classLoader,
                    methodAccess,
                    rewrite
            );
        }

        public StaticFiles build() {
            return new StaticFiles(
                    Objects.requireNonNull(path),
                    Objects.requireNonNull(prefix),
                    Objects.requireNonNull(classLoader),
                    Objects.requireNonNull(methodAccess),
                    rewrite
            );
        }
    }
}
