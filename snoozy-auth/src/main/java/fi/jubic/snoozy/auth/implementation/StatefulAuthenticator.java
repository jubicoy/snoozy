package fi.jubic.snoozy.auth.implementation;

import fi.jubic.snoozy.auth.Authenticator;
import fi.jubic.snoozy.auth.UserPrincipal;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class StatefulAuthenticator<P extends UserPrincipal> implements Authenticator<P> {
    private List<Token<P>> tokens;

    @Inject
    public StatefulAuthenticator() {
        tokens = new ArrayList<>();
    }

    @Override
    public Optional<P> authenticate(String token) {
        filterExpired();

        Optional<Token<P>> optionalToken = tokens.stream()
                .filter(t -> t.getToken().equals(token))
                .findFirst();

        if (!optionalToken.isPresent()) {
            return Optional.empty();
        }

        Token<P> t = optionalToken.get();
        int index = tokens.indexOf(t);

        t.setExpires(DateTime.now().plusHours(1));

        tokens.set(index, t);

        return Optional.of(t.getUser());
    }

    public void addToken(Token<P> token) {
        filterExpired();

        tokens.add(token);
    }

    public void revokeToken(Token<P> token) {
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
