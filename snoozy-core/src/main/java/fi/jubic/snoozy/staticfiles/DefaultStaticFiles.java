package fi.jubic.snoozy.staticfiles;

import fi.jubic.snoozy.MethodAccess;
import fi.jubic.snoozy.filters.UrlRewrite;

import java.util.Optional;

public class DefaultStaticFiles implements StaticFiles {
    private final String path;
    private final String prefix;
    private final ClassLoader classLoader;
    private final MethodAccess methodAccess;
    private final UrlRewrite rewrite;

    DefaultStaticFiles(
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

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public MethodAccess getMethodAccess() {
        return methodAccess;
    }

    @Override
    public Optional<UrlRewrite> getRewrite() {
        return Optional.ofNullable(rewrite);
    }
}
