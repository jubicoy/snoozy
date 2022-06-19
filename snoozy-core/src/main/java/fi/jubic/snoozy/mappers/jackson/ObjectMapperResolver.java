package fi.jubic.snoozy.mappers.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.ws.rs.ext.ContextResolver;

@SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "Intended behavior"
)
public class ObjectMapperResolver implements ContextResolver<ObjectMapper> {
    private final ObjectMapper objectMapper;

    /**
     * Construct a resolver for the default {@link ObjectMapper}.
     */
    public ObjectMapperResolver() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    public ObjectMapper getContext(Class<?> clazz) {
        return objectMapper;
    }
}
