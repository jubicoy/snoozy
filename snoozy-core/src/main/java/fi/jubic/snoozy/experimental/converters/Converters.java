package fi.jubic.snoozy.experimental.converters;

import fi.jubic.snoozy.experimental.converters.param.*;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Converters {
    public static Set<Object> all() {
        return Stream.of(
                new LocalTimeConverterProvider(),
                new OffsetTimeConverterProvider(),
                new LocalDateConverterProvider(),
                new LocalDateTimeConverterProvider(),
                new OffsetDateTimeConverterProvider()
        ).collect(Collectors.toSet());
    }
}
