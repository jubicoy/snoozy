package fi.jubic.snoozy;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MethodAccess {
    public enum Level {
        DenyAll,
        Anonymous,
        Authenticated,
        Roles
    }

    private final Level level;
    private final Set<String> values;

    private MethodAccess(
            Level level,
            Set<String> values
    ) {
        this.level = level;
        this.values = values;
    }

    @Deprecated
    public Level level() {
        return getLevel();
    }

    public Level getLevel() {
        return level;
    }

    @Deprecated
    public Set<String> values() {
        return getValues();
    }

    public Set<String> getValues() {
        return Collections.unmodifiableSet(values);
    }

    /**
     * Creates an access rule denying all access to the resource.
     */
    public static MethodAccess denyAll() {
        return new MethodAccess(
                Level.DenyAll,
                Collections.emptySet()
        );
    }

    /**
     * Creates an access rule allowing all access, including anonymous access.
     */
    public static MethodAccess anonymous() {
        return new MethodAccess(
                Level.Anonymous,
                Collections.emptySet()
        );
    }

    /**
     * Creates an access rule requiring an authenticated user.
     */
    public static MethodAccess authenticated() {
        return new MethodAccess(
                Level.Authenticated,
                Collections.emptySet()
        );
    }

    /**
     * Creates an access rule requiring a principal, authenticated or anonymous,
     * that maps to any of the given roles.
     */
    public static MethodAccess roles(String... roles) {
        return new MethodAccess(
                Level.Roles,
                Stream.of(roles).collect(Collectors.toSet())
        );
    }
}
