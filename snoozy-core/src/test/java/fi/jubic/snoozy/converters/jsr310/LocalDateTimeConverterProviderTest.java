package fi.jubic.snoozy.converters.jsr310;

import org.junit.jupiter.api.Test;

import javax.annotation.Nullable;
import javax.ws.rs.ext.ParamConverter;
import java.lang.annotation.Annotation;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;


class LocalDateTimeConverterProviderTest {
    private final LocalDateTimeConverterProvider provider = new LocalDateTimeConverterProvider();

    @Test
    void applyToCorrectClass() {
        assertNull(provider.getConverter(Long.class, null, new Annotation[0]));
        assertNotNull(provider.getConverter(LocalDateTime.class, null, new Annotation[0]));
    }

    @Test
    void parseCompliantISO8601Dates() {
        ParamConverter<LocalDateTime> converter = provider.getConverter(
                LocalDateTime.class,
                null,
                new Annotation[0]
        );

        assertEquals(
                converter.fromString("2019-01-01T12:00:00"),
                LocalDateTime.of(2019, 1, 1, 12, 0 , 0)
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
                LocalDateTime.of(2019, 1, 1, 0, 0 ,0)
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
                LocalDateTime.of(2019, 1, 1, 12, 0 ,0)
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
