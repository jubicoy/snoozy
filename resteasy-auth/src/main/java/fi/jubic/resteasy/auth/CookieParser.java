package fi.jubic.resteasy.auth;

import io.undertow.server.HttpServerExchange;

import javax.ws.rs.container.ContainerRequestContext;

public class CookieParser implements ITokenParser {
    private final String cookie;

    private CookieParser(String cookie) {
        this.cookie = cookie;
    }

    @Override
    public String parse(ContainerRequestContext containerRequestContext) {
        return containerRequestContext.getCookies()
                .get(cookie)
                .getValue();
    }

    @Override
    public String parse(HttpServerExchange httpServerExchange) {
        return httpServerExchange.getRequestCookies()
                .get(cookie)
                .getValue();
    }

    public static Builder builder() {
        return new Builder();
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
