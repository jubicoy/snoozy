package fi.jubic.snoozy.auth.implementation;

import com.fasterxml.jackson.annotation.JsonProperty;
import fi.jubic.snoozy.auth.UserPrincipal;
import org.joda.time.DateTime;

import java.util.UUID;

public class Token<P extends UserPrincipal> {
    @JsonProperty
    private String token;
    @JsonProperty
    private DateTime expires;
    @JsonProperty
    private P user;

    public Token(P user) {
        this(user, UUID.randomUUID().toString());
    }

    public Token(P user, String token) {
        this(user, token, DateTime.now().plusHours(1));
    }

    public Token(P user, String token, DateTime expires) {
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

    public DateTime getExpires() {
        return expires;
    }

    public void setExpires(DateTime expires) {
        this.expires = expires;
    }

    public P getUser() {
        return user;
    }

    public void setUser(P user) {
        this.user = user;
    }
}
