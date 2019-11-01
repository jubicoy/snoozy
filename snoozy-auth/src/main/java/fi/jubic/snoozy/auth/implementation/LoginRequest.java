package fi.jubic.snoozy.auth.implementation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest {
    private final String username;
    private final String password;

    public LoginRequest(
            @JsonProperty String username,
            @JsonProperty String password
    ) {
        this.username = username;
        this.password = password;
    }

    @Deprecated
    public String username() {
        return getUsername();
    }

    @JsonProperty
    public String getUsername() {
        return username;
    }

    @Deprecated
    public String password() {
        return getPassword();
    }

    @JsonProperty
    public String getPassword() {
        return password;
    }

    @Deprecated
    public Builder toBuilder() {
        return new Builder(username, password);
    }

    @Deprecated
    public static Builder builder() {
        return new Builder();
    }

    @Deprecated
    public static class Builder {
        private final String username;
        private final String password;

        private Builder() {
            this(null, null);
        }

        private Builder(
                String username,
                String password
        ) {
            this.username = username;
            this.password = password;
        }

        @Deprecated
        public Builder setUsername(String username) {
            return new Builder(
                    username,
                    password
            );
        }

        @Deprecated
        public Builder setPassword(String password) {
            return new Builder(
                    username,
                    password
            );
        }

        @Deprecated
        public LoginRequest build() {
            return new LoginRequest(username, password);
        }
    }
}
