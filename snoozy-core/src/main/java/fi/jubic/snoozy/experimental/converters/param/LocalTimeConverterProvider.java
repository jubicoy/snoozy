package fi.jubic.snoozy.experimental.converters.param;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.OffsetTime;

public class LocalTimeConverterProvider implements ParamConverterProvider {
    @Override
    public <T> ParamConverter<T> getConverter(
            Class<T> aClass,
            Type type,
            Annotation[] annotations
    ) {
        if (!LocalTime.class.equals(aClass)) return null;

        return new ParamConverter<T>() {
            @Override
            public T fromString(String s) {
                try {
                    return aClass.cast(
                            LocalTime.parse(s)
                    );
                } catch (RuntimeException ignore) {
                    return aClass.cast(
                            OffsetTime.parse(s)
                                    .withOffsetSameInstant(OffsetTime.now().getOffset())
                                    .toLocalTime()
                    );
                }
            }

            @Override
            public String toString(T t) {
                return t.toString();
            }
        };
    }
}
