package fi.jubic.snoozy.converters.jsr310;

import org.junit.jupiter.api.Test;

import javax.annotation.Nullable;
import javax.ws.rs.ext.ParamConverter;
import java.lang.annotation.Annotation;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;


class InstantConverterProviderTest {
    private final InstantConverterProvider provider = new InstantConverterProvider();

    @Test
    void applyToCorrectClass() {
        assertNull(provider.getConverter(Long.class, null, new Annotation[0]));
        assertNotNull(provider.getConverter(Instant.class, null, new Annotation[0]));
    }

    @Test
    void parseCompliantISO8601Dates() {
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
