package fi.jubic.snoozy.experimental.optional;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Dropwizard 2.0.0-rc0 Optional converters and writers
 */
public final class Optionals {
    public static Set<Object> all() {
        return Stream.of(
                new EmptyOptionalExceptionMapper(),
                new EmptyOptionalNoContentExceptionMapper(),
                new OptionalDoubleMessageBodyWriter(),
                new OptionalDoubleParamConverterProvider(),
                new OptionalIntMessageBodyWriter(),
                new OptionalIntParamConverterProvider(),
                new OptionalLongMessageBodyWriter(),
                new OptionalLongParamConverterProvider()
        ).collect(Collectors.toSet());
    }
}
