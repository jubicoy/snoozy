package fi.jubic.snoozy.mappers;

import fi.jubic.snoozy.mappers.exceptions.WebApplicationExceptionMapper;
import fi.jubic.snoozy.mappers.jackson.ObjectMapperResolver;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Mappers {
    public static Set<Object> builtins() {
        return Stream.of(
                new WebApplicationExceptionMapper(),
                new ObjectMapperResolver()
        ).collect(Collectors.toSet());
    }
}
