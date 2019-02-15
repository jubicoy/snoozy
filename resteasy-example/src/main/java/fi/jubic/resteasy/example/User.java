package fi.jubic.resteasy.example;

import fi.jubic.easyvalue.EasyProperty;
import fi.jubic.easyvalue.EasyValue;
import fi.jubic.resteasy.auth.UserPrincipal;

@EasyValue(excludeJson = true)
public abstract class User implements UserPrincipal {
    @EasyProperty
    public abstract String name();
    @EasyProperty
    public abstract String role();

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getRole() {
        return role();
    }

    public static class Builder extends EasyValue_User.Builder {
    }
}
