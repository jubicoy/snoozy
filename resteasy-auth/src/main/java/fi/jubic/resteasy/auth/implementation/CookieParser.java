package fi.jubic.resteasy.auth.implementation;

import fi.jubic.resteasy.auth.TokenParser;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class CookieParser implements TokenParser {
    private final String cookieName;

    private CookieParser(String cookieName) {
        this.cookieName = cookieName;
    }

    @Override
    public Optional<String> parse(HttpServletRequest servletRequest) {
        return Stream.of(servletRequest.getCookies())
                .filter(cookie -> Objects.equals(cookie.getName(), cookieName))
                .map(Cookie::getValue)
                .findFirst();
    }

    public static CookieParser of(String cookieName) {
        return new CookieParser(cookieName);
    }
}
