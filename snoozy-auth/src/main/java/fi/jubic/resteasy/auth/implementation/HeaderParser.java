package fi.jubic.resteasy.auth.implementation;

import fi.jubic.resteasy.auth.TokenParser;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class HeaderParser implements TokenParser {
    private final String header;

    private HeaderParser(String header) {
        this.header = header;
    }

    @Override
    public Optional<String> parse(HttpServletRequest servletRequest) {
        return Optional.ofNullable(servletRequest.getHeader(header))
                .filter(h -> h.length() > 0);
    }

    public static HeaderParser of(String header) {
        return new HeaderParser(header);
    }
}
