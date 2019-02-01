package fi.jubic.resteasy.auth;

import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class StatefulAuthenticator<U extends UserPrincipal> implements IAuthenticator<U> {
    private List<Token<U>> tokens;

    @Inject
    public StatefulAuthenticator() {
        tokens = new ArrayList<>();
    }

    @Override
    public Optional<U> authenticate(String token) {
        filterExpired();

        Optional<Token<U>> optionalToken = tokens.stream()
                .filter(t -> t.getToken().equals(token))
                .findFirst();

        if (!optionalToken.isPresent()) {
            return Optional.empty();
        }

        Token<U> t = optionalToken.get();
        int index = tokens.indexOf(t);

        t.setExpires(DateTime.now().plusHours(1));

        tokens.set(index, t);

        return Optional.of(t.getUser());
    }

    public void addToken(Token<U> token) {
        filterExpired();

        tokens.add(token);
    }

    public void revokeToken(Token<U> token) {
        filterExpired();

        tokens = tokens.stream()
                .filter(t -> !t.getToken().equals(token.getToken()))
                .collect(Collectors.toList());
    }

    private void filterExpired() {
        tokens = tokens.stream()
                .filter(t -> t.getExpires().isAfterNow())
                .collect(Collectors.toList());
    }
}
