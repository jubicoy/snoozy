package fi.jubic.snoozy.converters.jsr310;

import jakarta.ws.rs.ext.ParamConverter;
import org.junit.jupiter.api.Test;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InstantConverterProviderTest {
    private final InstantConverterProvider provider = new InstantConverterProvider();

    @Test
    void applyToCorrectClass() {
        assertNull(provider.getConverter(Long.class, null, new Annotation[0]));
        assertNotNull(provider.getConverter(Instant.class, null, new Annotation[0]));
    }

    @Test
    void parseCompliantIso8601Dates() {
        ParamConverter<Instant> converter = provider.getConverter(
                Instant.class,
                null,
                new Annotation[0]
        );

        assertEquals(
                Instant.parse("2019-01-01T12:00:00.000Z"),
                converter.fromString("2019-01-01T12:00:00.000Z")
        );
    }

    @Test
    void upDownCastTimestamps() {
        ParamConverter<Instant> converter = provider.getConverter(
                Instant.class,
                null,
                new Annotation[0]
        );

        assertEquals(
                Instant.parse("2019-01-01T00:00:00.000Z")
                        .minusSeconds(
                                ZoneId.systemDefault()
                                        .getRules()
                                        .getOffset(Instant.now())
                                        .getTotalSeconds()
                        ),
                converter.fromString("2019-01-01")

        );
        assertEquals(
                Instant.parse("2019-01-01T12:00:00.000Z")
                        .minusSeconds(
                                ZoneId.systemDefault()
                                        .getRules()
                                        .getOffset(Instant.now())
                                        .getTotalSeconds()
                        ),
                converter.fromString("2019-01-01T12:00:00")
        );
        assertEquals(
                Instant.parse("2019-01-01T12:00:00.000Z"),
                converter.fromString("1546344000000")
        );
    }

    @Test
    void allowNullableOnlyWhenAnnotated() {
        ParamConverter<Instant> nonNullConverter = provider.getConverter(
                Instant.class,
                null,
                new Annotation[0]
        );

        ParamConverter<Instant> nullableConverter = provider.getConverter(
                Instant.class,
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
