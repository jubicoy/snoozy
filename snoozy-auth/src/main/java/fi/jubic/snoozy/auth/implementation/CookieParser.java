package fi.jubic.snoozy.auth.implementation;

import fi.jubic.snoozy.auth.TokenParser;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

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
