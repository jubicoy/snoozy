package fi.jubic.snoozy.auth;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface TokenParser {
    /**
     * Extract authentication token from a {@link HttpServletRequest}.
     * @return An {@link Optional} of the token, or empty for HTTP 401.
     */
    Optional<String> parse(HttpServletRequest servletRequest);
}
