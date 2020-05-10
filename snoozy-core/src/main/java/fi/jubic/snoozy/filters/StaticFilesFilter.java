package fi.jubic.snoozy.filters;

import fi.jubic.snoozy.staticfiles.StaticFiles;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.util.Optional;

public interface StaticFilesFilter {
    /**
     * Filter {@link StaticFiles} requests based on authentication and authorization.
     * @param staticFiles Static files bundle being accessed.
     * @param request Servlet request.
     * @return An empty {@link Optional} if the user is authorized to access the static files,
     *         otherwise {@link Optional} of the desired error {@link Response}.
     */
    Optional<Response> filter(StaticFiles staticFiles, HttpServletRequest request);
}
