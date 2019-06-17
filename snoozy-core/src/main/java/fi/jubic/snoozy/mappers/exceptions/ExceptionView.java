package fi.jubic.snoozy.mappers.exceptions;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fi.jubic.easyvalue.EasyProperty;
import fi.jubic.easyvalue.EasyValue;

@EasyValue
@JsonSerialize(as = EasyValue_ExceptionView.class)
public abstract class ExceptionView {
    @EasyProperty
    public abstract int code();
    @EasyProperty
    public abstract String message();

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends EasyValue_ExceptionView.Builder {

    }
}
