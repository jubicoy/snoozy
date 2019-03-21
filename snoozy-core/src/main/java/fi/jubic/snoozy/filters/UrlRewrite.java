package fi.jubic.snoozy.filters;

import fi.jubic.easyvalue.EasyProperty;
import fi.jubic.easyvalue.EasyValue;

@EasyValue(excludeJson = true)
public abstract class UrlRewrite {
    @EasyProperty
    public abstract String from();
    @EasyProperty
    public abstract String to();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends EasyValue_UrlRewrite.Builder {

    }
}
