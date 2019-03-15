package fi.jubic.snoozy.example;

import fi.jubic.easyvalue.EasyProperty;
import fi.jubic.easyvalue.EasyValue;
import fi.jubic.snoozy.auth.UserPrincipal;

@EasyValue(excludeJson = true)
public abstract class User implements UserPrincipal {
    @EasyProperty
    public abstract Long id();
    @EasyProperty
    public abstract String name();
    @EasyProperty
    public abstract String role();
    @EasyProperty
    public abstract String hash();
    @EasyProperty
    public abstract String salt();

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
