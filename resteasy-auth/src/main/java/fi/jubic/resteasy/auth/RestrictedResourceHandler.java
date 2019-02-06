package fi.jubic.resteasy.auth;

import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RestrictedResourceHandler extends ResourceHandler {
    private final List<String> includes;
    private final List<String> excludes;
    private final ITokenParser tokenParser;
    private final IAuthenticator authenticator;

    private RestrictedResourceHandler(
            ClassLoader classLoader,
            String prefix,
            List<String> includes,
            List<String> excludes,
            ITokenParser tokenParser,
            IAuthenticator authenticator
    ) {
        super(new ClassPathResourceManager(classLoader, prefix));
        addWelcomeFiles("index.html");

        this.includes = includes;
        this.excludes = excludes;
        this.tokenParser = tokenParser;
        this.authenticator = authenticator;
    }

    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception {
        String path = httpServerExchange.getRelativePath();

        for (String include : includes) {
            if (!path.matches(include)) {
                continue;
            }

            String token = tokenParser.parse(httpServerExchange);
            Optional user = authenticator.authenticate(token);

            if (user.isPresent()) {
                break;
            }

            httpServerExchange.setStatusCode(StatusCodes.UNAUTHORIZED);
            return;
        }

        super.handleRequest(httpServerExchange);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ClassLoader classLoader;
        private String prefix;
        private List<String> includes;
        private List<String> excludes;
        private ITokenParser tokenParser;
        private IAuthenticator authenticator;

        public Builder() {
            prefix = "";
            includes = Collections.unmodifiableList(Collections.emptyList());
            excludes = Collections.unmodifiableList(Collections.emptyList());
            tokenParser = HeaderParser.builder()
                    .setHeader(Headers.AUTHORIZATION_STRING)
                    .build();
        }

        public Builder setClassLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
            return this;
        }

        public Builder setPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder setIncludes(List<String> includes) {
            this.includes = includes;
            return this;
        }

        public Builder setExcludes(List<String> excludes) {
            this.excludes = excludes;
            return this;
        }

        public Builder setTokenParser(ITokenParser tokenParser) {
            this.tokenParser = tokenParser;
            return this;
        }

        public Builder setAuthenticator(IAuthenticator authenticator) {
            this.authenticator = authenticator;
            return this;
        }

        public RestrictedResourceHandler build() {
            return new RestrictedResourceHandler(classLoader, prefix, includes, excludes, tokenParser, authenticator);
        }
    }
}
