package fi.jubic.snoozy.experimental.converters;

import fi.jubic.snoozy.experimental.converters.param.LocalTimeConverterProvider;
import fi.jubic.snoozy.experimental.converters.param.OffsetDateTimeConverterProvider;
import fi.jubic.snoozy.experimental.converters.param.OffsetTimeConverterProvider;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Converters {
    public static Set<Object> builtins() {
        return Stream.of(
                new OffsetDateTimeConverterProvider(),
                new OffsetTimeConverterProvider(),
                new LocalTimeConverterProvider()
        ).collect(Collectors.toSet());
    }
}
