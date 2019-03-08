package fi.jubic.snoozy.experimental.converters.param;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class LocalDateTimeConverterProvider implements ParamConverterProvider {
    @Override
    public <T> ParamConverter<T> getConverter(
            Class<T> aClass,
            Type type,
            Annotation[] annotations
    ) {
        if (!LocalDateTime.class.equals(aClass)) return null;

        return new ParamConverter<T>() {
            @Override
            public T fromString(String s) {
                return aClass.cast(
                        OffsetDateTime.parse(s)
                                .atZoneSameInstant(ZoneId.systemDefault())
                                .toLocalDateTime()
                );
            }

            @Override
            public String toString(T t) {
                return t.toString();
            }
        };
    }
}
