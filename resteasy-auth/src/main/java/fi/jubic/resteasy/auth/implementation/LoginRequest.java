package fi.jubic.resteasy.auth.implementation;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fi.jubic.easyvalue.EasyProperty;
import fi.jubic.easyvalue.EasyValue;

@EasyValue
@JsonDeserialize(as = EasyValue_LoginRequest.class)
@JsonSerialize(as = EasyValue_LoginRequest.class)
public abstract class LoginRequest {
    @EasyProperty
    public abstract String username();
    @EasyProperty
    public abstract String password();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends EasyValue_LoginRequest.Builder {
    }
}
