package fi.jubic.resteasy.auth;

import javax.ws.rs.container.ContainerRequestContext;

public class CookieParser implements ITokenParser {
    private final String cookie;

    private CookieParser(String cookie) {
        this.cookie = cookie;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String parse(ContainerRequestContext containerRequestContext) {
        return containerRequestContext.getCookies()
                .get(cookie)
                .getValue();
    }

    public static class Builder {
        private String cookie;

        public Builder setCookie(String cookie) {
            this.cookie = cookie;
            return this;
        }

        public CookieParser build() {
            return new CookieParser(cookie);
        }
    }
}
