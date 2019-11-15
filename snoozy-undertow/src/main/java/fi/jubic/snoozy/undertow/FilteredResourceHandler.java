package fi.jubic.snoozy.undertow;

import fi.jubic.snoozy.StaticFiles;
import fi.jubic.snoozy.filters.StaticFilesFilter;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.servlet.spec.HttpServletRequestImpl;
import io.undertow.servlet.spec.HttpServletResponseImpl;
import io.undertow.util.HttpString;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

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

        if (!filter.filter(staticFiles, request)) {
            Response response = filter.getResponseSupplier().get();
            httpServerExchange.setStatusCode(response.getStatus());
            response.getStringHeaders().forEach(
                    (name, value) -> httpServerExchange.getResponseHeaders()
                            .addAll(HttpString.tryFromString(name), value)
            );
            return;
        }

        if (
                staticFiles.getRewrite() != null
                && httpServerExchange.getRelativePath()
                        .matches(staticFiles.getRewrite().getFrom())
        ) {
            httpServerExchange.setRelativePath(staticFiles.getRewrite().getTo());
        }

        super.handleRequest(httpServerExchange);
    }
}
