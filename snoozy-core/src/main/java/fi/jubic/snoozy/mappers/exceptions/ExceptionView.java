package fi.jubic.snoozy.mappers.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExceptionView {
    private final int code;
    private final String message;

    public ExceptionView(
            @JsonProperty int code,
            @JsonProperty String message
    ) {
        this.code = code;
        this.message = message;
    }

    @JsonProperty
    public int getCode() {
        return code;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }
}
