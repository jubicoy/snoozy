package fi.jubic.snoozy.converters;

import fi.jubic.snoozy.converters.jsr310.InstantConverterProvider;
import fi.jubic.snoozy.converters.jsr310.LocalDateConverterProvider;
import fi.jubic.snoozy.converters.jsr310.LocalDateTimeConverterProvider;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Converters {
    /**
     * Provides built-in parameter converters.
     */
    public static Set<Object> builtins() {
        return Stream.of(
                new LocalDateConverterProvider(),
                new LocalDateTimeConverterProvider(),
                new InstantConverterProvider()
        ).collect(Collectors.toSet());
    }
}
