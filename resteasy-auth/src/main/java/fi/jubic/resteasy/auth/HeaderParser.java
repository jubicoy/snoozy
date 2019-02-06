package fi.jubic.resteasy.auth;

import io.undertow.server.HttpServerExchange;

import javax.ws.rs.container.ContainerRequestContext;

public class HeaderParser implements ITokenParser {
    private final String header;

    private HeaderParser(String header) {
        this.header = header;
    }

    @Override
    public String parse(ContainerRequestContext containerRequestContext) {
        return containerRequestContext.getHeaderString(header);
    }

    @Override
    public String parse(HttpServerExchange httpServerExchange) {
        return httpServerExchange.getRequestHeaders()
                .getFirst(header);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String header;

        public Builder setHeader(String header) {
            this.header = header;
            return this;
        }

        public HeaderParser build() {
            return new HeaderParser(header);
        }
    }
}
