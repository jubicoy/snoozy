package fi.jubic.snoozy.converters.jsr310;

import jakarta.ws.rs.ext.ParamConverter;
import org.junit.jupiter.api.Test;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocalDateTimeConverterProviderTest {
    private final LocalDateTimeConverterProvider provider = new LocalDateTimeConverterProvider();

    @Test
    void applyToCorrectClass() {
        assertNull(provider.getConverter(Long.class, null, new Annotation[0]));
        assertNotNull(provider.getConverter(LocalDateTime.class, null, new Annotation[0]));
    }

    @Test
    void parseCompliantIso8601Dates() {
        ParamConverter<LocalDateTime> converter = provider.getConverter(
                LocalDateTime.class,
                null,
                new Annotation[0]
        );

        assertEquals(
                converter.fromString("2019-01-01T12:00:00"),
                LocalDateTime.of(2019, 1, 1, 12, 0, 0)
        );
    }

    @Test
    void upDownCastTimestamps() {
        ParamConverter<LocalDateTime> converter = provider.getConverter(
                LocalDateTime.class,
                null,
                new Annotation[0]
        );

        assertEquals(
                converter.fromString("2019-01-01"),
                LocalDateTime.of(2019, 1, 1, 0, 0,0)
        );
        assertEquals(
                converter.fromString("2019-01-01T12:00:00"),
                LocalDateTime.of(2019, 1, 1, 12, 0, 0)
        );
        assertEquals(
                converter.fromString("2019-01-01T12:00:00.000Z"),
                LocalDateTime.ofInstant(
                        Instant.parse("2019-01-01T12:00:00.000Z"),
                        ZoneId.systemDefault()
                )
        );
        assertEquals(
                converter.fromString("1546336800000"),
                LocalDateTime.ofInstant(
                        Instant.parse("2019-01-01T10:00:00.000Z"),
                        ZoneId.systemDefault()
                )
        );
    }

    @Test
    void allowNullableOnlyWhenAnnotated() {
        ParamConverter<LocalDateTime> nonNullConverter = provider.getConverter(
                LocalDateTime.class,
                null,
                new Annotation[0]
        );

        ParamConverter<LocalDateTime> nullableConverter = provider.getConverter(
                LocalDateTime.class,
                null,
                AnnotatedClass.class.getAnnotations()
        );

        assertNull(nullableConverter.fromString(null));
        assertThrows(
                NullPointerException.class,
                () -> nonNullConverter.fromString(null)
        );
    }

    @Nullable
    private static class AnnotatedClass {
    }
}
