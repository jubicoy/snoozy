package fi.jubic.snoozy.experimental.converters.param;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;

public class LocalDateConverterProvider implements ParamConverterProvider {
    @Override
    public <T> ParamConverter<T> getConverter(
            Class<T> aClass,
            Type type,
            Annotation[] annotations
    ) {
        if (!LocalDate.class.equals(aClass)) return null;

        return new ParamConverter<T>() {
            @Override
            public T fromString(String s) {
                return aClass.cast(LocalDate.parse(s));
            }

            @Override
            public String toString(T t) {
                return t.toString();
            }
        };
    }
}
