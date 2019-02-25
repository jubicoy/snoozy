package fi.jubic.snoozy.undertow;

import fi.jubic.snoozy.StaticFiles;
import fi.jubic.snoozy.filters.StaticFilesFilter;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.servlet.spec.HttpServletRequestImpl;

import javax.servlet.http.HttpServletRequest;

class FilteredResourceHandler extends ResourceHandler {
    private final StaticFilesFilter filter;
    private final StaticFiles staticFiles;

    FilteredResourceHandler(
            StaticFilesFilter filter,
            StaticFiles staticFiles
    ) {
        super(
                new ClassPathResourceManager(
                        staticFiles.classLoader(),
                        staticFiles.prefix()
                )
        );
        this.filter = filter;
        this.staticFiles = staticFiles;
    }

    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception {
        HttpServletRequest request = new HttpServletRequestImpl(
                httpServerExchange,
                null
        );

        if (!filter.filter(staticFiles, request)) {
            httpServerExchange.setStatusCode(401);
            return;
        }

        super.handleRequest(httpServerExchange);
    }
}
