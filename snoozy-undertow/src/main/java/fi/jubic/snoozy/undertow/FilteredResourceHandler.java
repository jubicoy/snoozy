package fi.jubic.snoozy.undertow;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import fi.jubic.snoozy.filters.StaticFilesFilter;
import fi.jubic.snoozy.staticfiles.StaticFiles;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.servlet.spec.HttpServletRequestImpl;
import io.undertow.util.HttpString;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@SuppressFBWarnings("THROWS_METHOD_THROWS_CLAUSE_BASIC_EXCEPTION")
class FilteredResourceHandler extends ResourceHandler {
    private final StaticFilesFilter filter;
    private final StaticFiles staticFiles;

    FilteredResourceHandler(
            StaticFilesFilter filter,
            StaticFiles staticFiles
    ) {
        super(
                new ClassPathResourceManager(
                        staticFiles.getClassLoader(),
                        staticFiles.getPrefix()
                )
        );
        this.filter = filter;
        this.staticFiles = staticFiles;
    }

    @Override
    public void handleRequest(
            HttpServerExchange httpServerExchange
    ) throws Exception {
        HttpServletRequest request = new HttpServletRequestImpl(
                httpServerExchange,
                null
        );

        Optional<Response> optionalResponse = filter.filter(staticFiles, request);
        if (optionalResponse.isPresent()) {
            Response response = optionalResponse.get();
            httpServerExchange.setStatusCode(response.getStatus());
            response.getStringHeaders().forEach(
                    (name, value) -> httpServerExchange.getResponseHeaders()
                            .addAll(HttpString.tryFromString(name), value)
            );
            return;
        }

        staticFiles.getRewrite()
                .filter(rewrite -> httpServerExchange.getRelativePath().matches(rewrite.getFrom()))
                .ifPresent(rewrite -> httpServerExchange.setRelativePath(rewrite.getTo()));

        super.handleRequest(httpServerExchange);
    }
}
