package fi.jubic.snoozy.filters;

import fi.jubic.snoozy.StaticFiles;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.util.function.Supplier;

public interface StaticFilesFilter {
    boolean filter(StaticFiles staticFiles, HttpServletRequest request);
    Supplier<Response> getResponseSupplier();
}
