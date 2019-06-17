package fi.jubic.snoozy.converters.jsr310;

import java.time.*;
import java.util.regex.Pattern;

class ParseUtil {
    private static final Pattern localDatePattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
    private static final Pattern localDateTimePattern = Pattern.compile(
            "[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}"
    );
    private static final Pattern instantPattern = Pattern.compile(
            "[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}Z"
    );
    private static final Pattern timestampPattern = Pattern.compile("[0-9]+");

    static LocalDate parseLocalDate(String string) {
        if (localDateTimePattern.matcher(string).matches()) {
            return LocalDateTime.parse(string).toLocalDate();
        }
        if (instantPattern.matcher(string).matches()) {
            return LocalDateTime
                    .ofInstant(
                            Instant.parse(string),
                            ZoneId.systemDefault()
                    )
                    .toLocalDate();
        }
        if (timestampPattern.matcher(string).matches()) {
            return LocalDateTime
                    .ofInstant(
                            Instant.ofEpochMilli(Long.parseLong(string)),
                            ZoneId.systemDefault()
                    )
                    .toLocalDate();
        }

        return LocalDate.parse(string);
    }

    static LocalDateTime parseLocalDateTime(String string) {
        if (localDatePattern.matcher(string).matches()) {
            return LocalDate.parse(string).atStartOfDay();
        }
        if (instantPattern.matcher(string).matches()) {
            return LocalDateTime
                    .ofInstant(
                            Instant.parse(string),
                            ZoneId.systemDefault()
                    );
        }
        if (timestampPattern.matcher(string).matches()) {
            return LocalDateTime
                    .ofInstant(
                            Instant.ofEpochMilli(Long.parseLong(string)),
                            ZoneId.systemDefault()
                    );
        }

        return LocalDateTime.parse(string);
    }

    static Instant parseInstant(String string) {
        if (localDatePattern.matcher(string).matches()) {
            return LocalDate.parse(string).atStartOfDay()
                    .toInstant(
                            ZoneId.systemDefault()
                                    .getRules()
                                    .getOffset(Instant.now())
                    );
        }
        if (localDateTimePattern.matcher(string).matches()) {
            return LocalDateTime.parse(string)
                    .toInstant(
                            ZoneId.systemDefault()
                                    .getRules()
                                    .getOffset(Instant.now())
                    );
        }
        if (timestampPattern.matcher(string).matches()) {
            return Instant.ofEpochMilli(Long.parseLong(string));
        }

        return Instant.parse(string);
    }
}
