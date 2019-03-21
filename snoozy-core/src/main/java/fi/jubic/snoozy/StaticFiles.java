package fi.jubic.snoozy;

import fi.jubic.easyvalue.EasyProperty;
import fi.jubic.easyvalue.EasyValue;
import fi.jubic.snoozy.filters.UrlRewrite;

import javax.annotation.Nullable;

@EasyValue(excludeJson = true)
public abstract class StaticFiles {
    @EasyProperty
    public abstract String path();
    @EasyProperty
    public abstract String prefix();
    @EasyProperty
    public abstract ClassLoader classLoader();
    @EasyProperty
    public abstract MethodAccess methodAccess();
    @EasyProperty
    @Nullable
    public abstract UrlRewrite rewrite();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new Builder()
                .setPath("/")
                .setPrefix("")
                .setMethodAccess(MethodAccess.authenticated());
    }

    public static class Builder extends EasyValue_StaticFiles.Builder {

    }
}
