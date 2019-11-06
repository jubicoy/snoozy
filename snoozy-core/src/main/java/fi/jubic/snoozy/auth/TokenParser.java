package fi.jubic.snoozy.auth;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

public interface TokenParser {
    Optional<String> parse(HttpServletRequest servletRequest);
}
