package fi.jubic.snoozy.converters.jsr310;

import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Stream;

public class LocalDateConverterProvider implements ParamConverterProvider {
    @Override
    public <T> ParamConverter<T> getConverter(
            Class<T> clazz,
            Type type,
            Annotation[] annotations
    ) {
        if (!LocalDate.class.equals(clazz)) return null;

        boolean isNullable = Stream.of(annotations)
                .anyMatch(
                        annotation -> Objects.equals(
                                annotation.annotationType(),
                                Nullable.class
                        )
                );

        return new ParamConverter<T>() {
            @Override
            public T fromString(@Nullable String s) {
                if (s == null && isNullable) {
                    return clazz.cast(null);
                }
                return clazz.cast(
                        ParseUtil.parseLocalDate(s)
                );
            }

            @Override
            public String toString(T t) {
                return t.toString();
            }
        };
    }
}
