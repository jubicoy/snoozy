package fi.jubic.snoozy.auth.implementation;

import com.fasterxml.jackson.annotation.JsonProperty;
import fi.jubic.snoozy.auth.UserPrincipal;

import java.time.LocalDateTime;
import java.util.UUID;

public class Token<P extends UserPrincipal> {
    @JsonProperty
    private String token;
    @JsonProperty
    private LocalDateTime expires;
    @JsonProperty
    private P user;

    public Token(P user) {
        this(user, UUID.randomUUID().toString());
    }

    public Token(P user, String token) {
        this(user, token, LocalDateTime.now().plusHours(1));
    }

    public Token(P user, String token, LocalDateTime expires) {
        this.user = user;
        this.token = token;
        this.expires = expires;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    public void setExpires(LocalDateTime expires) {
        this.expires = expires;
    }

    public P getUser() {
        return user;
    }

    public void setUser(P user) {
        this.user = user;
    }
}
