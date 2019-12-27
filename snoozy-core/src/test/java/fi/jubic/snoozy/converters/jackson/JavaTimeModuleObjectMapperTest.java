package fi.jubic.snoozy.converters.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.jubic.snoozy.mappers.jackson.ObjectMapperResolver;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JavaTimeModuleObjectMapperTest {
    @Test
    void instantSerializeDeserializeTest() throws IOException {
        ObjectMapper objectMapper = new ObjectMapperResolver().getContext(Void.class);

        Instant now = Instant.now();

        String json = objectMapper.writeValueAsString(new TestModel(now));
        assertEquals(
                json,
                "{\"instant\":\"" + now.toString() + "\"}"
        );

        TestModel deserialized = objectMapper.readValue(json, TestModel.class);
        assertEquals(
                deserialized.instant,
                now
        );
    }

    static class TestModel {
        @JsonProperty
        Instant instant;

        @SuppressWarnings("unused")
        TestModel() {

        }

        TestModel(Instant instant) {
            this.instant = instant;
        }
    }
}
