package fi.jubic.snoozy.auth;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface TokenParser {
    Optional<String> parse(HttpServletRequest servletRequest);
}
