package fi.jubic.snoozy.converters.jsr310;

import jakarta.ws.rs.ext.ParamConverter;
import org.junit.jupiter.api.Test;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


class LocalDateConverterProviderTest {
    private final LocalDateConverterProvider provider = new LocalDateConverterProvider();

    @Test
    void applyToCorrectClass() {
        assertNull(provider.getConverter(Long.class, null, new Annotation[0]));
        assertNotNull(provider.getConverter(LocalDate.class, null, new Annotation[0]));
    }

    @Test
    void parseCompliantIso8601Dates() {
        ParamConverter<LocalDate> converter = provider.getConverter(
                LocalDate.class,
                null,
                new Annotation[0]
        );

        assertEquals(
                converter.fromString("2019-01-01"),
                LocalDate.of(2019, 1, 1)
        );
    }

    @Test
    void downCastHigherPrecisionTimestamps() {
        ParamConverter<LocalDate> converter = provider.getConverter(
                LocalDate.class,
                null,
                new Annotation[0]
        );

        assertEquals(
                converter.fromString("2019-01-01T12:00:00"),
                LocalDate.of(2019, 1, 1)
        );
        assertEquals(
                converter.fromString("2019-01-01T12:00:00.123Z"),
                LocalDate.of(2019, 1, 1)
        );
        assertEquals(
                converter.fromString("1546300800000"),
                LocalDate.of(2019, 1, 1)
        );
    }

    @Test
    void allowNullableOnlyWhenAnnotated() {
        ParamConverter<LocalDate> nonNullConverter = provider.getConverter(
                LocalDate.class,
                null,
                new Annotation[0]
        );

        ParamConverter<LocalDate> nullableConverter = provider.getConverter(
                LocalDate.class,
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
