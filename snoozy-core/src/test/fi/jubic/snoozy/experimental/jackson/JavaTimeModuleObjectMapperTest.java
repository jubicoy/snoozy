package fi.jubic.snoozy.experimental.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.time.Instant;

import static org.hamcrest.core.Is.is;

public class JavaTimeModuleObjectMapperTest {
    @Test
    public void instantSerializeDeserializeTest() throws IOException {
        ObjectMapper objectMapper = new ObjectMapperResolver().getContext(Void.class);

        Instant now = Instant.now();

        String json = objectMapper.writeValueAsString(new TestModel(now));
        Assert.assertThat(json, is("{\"instant\":\"" + now.toString() + "\"}"));

        TestModel deserialized = objectMapper.readValue(json, TestModel.class);
        Assert.assertThat(deserialized.instant, is(now));
    }

    static class TestModel {
        @JsonProperty
        Instant instant;

        TestModel() {}

        TestModel(Instant instant) {
            this.instant = instant;
        }
    }
}
