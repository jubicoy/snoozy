package fi.jubic.resteasy.filters;

import fi.jubic.resteasy.StaticFiles;

import javax.servlet.http.HttpServletRequest;

public interface StaticFilesFilter {
    boolean filter(StaticFiles staticFiles, HttpServletRequest request);
}
