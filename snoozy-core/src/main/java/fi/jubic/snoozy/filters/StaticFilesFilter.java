package fi.jubic.snoozy.filters;

import fi.jubic.snoozy.StaticFiles;

import javax.servlet.http.HttpServletRequest;

public interface StaticFilesFilter {
    boolean filter(StaticFiles staticFiles, HttpServletRequest request);
}
