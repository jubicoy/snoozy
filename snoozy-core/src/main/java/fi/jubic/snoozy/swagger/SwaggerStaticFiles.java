package fi.jubic.snoozy.swagger;

import fi.jubic.snoozy.Application;
import fi.jubic.snoozy.MethodAccess;
import fi.jubic.snoozy.staticfiles.StaticFiles;

public class SwaggerStaticFiles implements StaticFiles {
    @Override
    public String getPath() {
        return "swagger";
    }

    @Override
    public String getPrefix() {
        return "swagger";
    }

    @Override
    public ClassLoader getClassLoader() {
        return Application.class.getClassLoader();
    }

    @Override
    public MethodAccess getMethodAccess() {
        return MethodAccess.anonymous();
    }
}
