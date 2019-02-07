package fi.jubic.resteasy.server;

import fi.jubic.easyvalue.EasyProperty;
import fi.jubic.easyvalue.EasyValue;

@EasyValue
public abstract class StaticFiles {
    @EasyProperty
    public abstract String prefix();
    @EasyProperty
    public abstract ClassLoader classLoader();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new Builder()
                .setPrefix("");
    }

    public static class Builder extends EasyValue_StaticFiles.Builder {

    }
}
