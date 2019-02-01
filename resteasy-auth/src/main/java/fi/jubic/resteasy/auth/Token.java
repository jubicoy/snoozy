package fi.jubic.resteasy.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.util.UUID;

public class Token<U extends UserPrincipal> {
    @JsonProperty
    private String token;
    @JsonProperty
    private DateTime expires;
    @JsonProperty
    private U user;

    public Token(U user) {
        this(user, UUID.randomUUID().toString());
    }

    public Token(U user, String token) {
        this(user, token, DateTime.now().plusHours(1));
    }

    public Token(U user, String token, DateTime expires) {
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

    public U getUser() {
        return user;
    }

    public void setUser(U user) {
        this.user = user;
    }
}
