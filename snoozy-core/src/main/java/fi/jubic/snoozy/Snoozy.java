package fi.jubic.snoozy;

import fi.jubic.snoozy.converters.Converters;
import fi.jubic.snoozy.mappers.Mappers;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Snoozy {
    public static Set<Object> builtins() {
        return Stream.concat(
                Converters.builtins().stream(),
                Mappers.builtins().stream()
        ).collect(Collectors.toSet());
    }
}
