package fi.jubic.snoozy;

import fi.jubic.easyvalue.EasyProperty;
import fi.jubic.easyvalue.EasyValue;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EasyValue(excludeJson = true)
public abstract class MethodAccess {
    public enum Level {
        DenyAll,
        Anonymous,
        Authenticated,
        Roles
    }
    @EasyProperty
    public abstract Level level();
    @EasyProperty
    public abstract Set<String> values();

    public static MethodAccess denyAll() {
        // return new EasyValue_MethodAccess(Level.DenyAll, Collections.emptySet());
        return new Builder()
                .setLevel(Level.DenyAll)
                .setValues(Collections.emptySet())
                .build();
    }

    public static MethodAccess anonymous() {
        return new Builder()
                .setLevel(Level.Anonymous)
                .setValues(Collections.emptySet())
                .build();
    }

    public static MethodAccess authenticated() {
        return new Builder()
                .setLevel(Level.Authenticated)
                .setValues(Collections.emptySet())
                .build();
    }

    public static MethodAccess roles(String... roles) {
        return new Builder()
                .setLevel(Level.Roles)
                .setValues(Stream.of(roles).collect(Collectors.toSet()))
                .build();
    }

    static class Builder extends EasyValue_MethodAccess.Builder {

    }
}
