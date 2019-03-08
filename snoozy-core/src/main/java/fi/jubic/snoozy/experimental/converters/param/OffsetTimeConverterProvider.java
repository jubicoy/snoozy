package fi.jubic.snoozy.experimental.converters.param;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.OffsetTime;

public class OffsetTimeConverterProvider implements ParamConverterProvider {
    @Override
    public <T> ParamConverter<T> getConverter(
            Class<T> aClass,
            Type type,
            Annotation[] annotations
    ) {
        if (!OffsetTime.class.equals(aClass)) return null;

        return new ParamConverter<T>() {
            @Override
            public T fromString(String s) {
                return aClass.cast(
                        OffsetTime.parse(s)
                );
            }

            @Override
            public String toString(T t) {
                return t.toString();
            }
        };
    }
}
