package fi.jubic.snoozy.auth.implementation;

import fi.jubic.snoozy.auth.Authenticator;
import fi.jubic.snoozy.auth.UserPrincipal;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Singleton
public class StatefulAuthenticator<P extends UserPrincipal> implements Authenticator<P> {
    private List<Token<P>> tokens;

    @Inject
    public StatefulAuthenticator() {
        tokens = new CopyOnWriteArrayList<>();
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


        t.setExpires(LocalDateTime.now().plusHours(1L));

        tokens.set(index, t);

        return Optional.of(t.getUser());
    }

    /**
     * Add a token to the token collection. Filters expired tokens.
     */
    public void addToken(Token<P> token) {
        filterExpired();

        tokens.add(token);
    }

    /**
     * Revoke a token. Filters expired tokens.
     */
    public void revokeToken(Token<P> token) {
        filterExpired();

        tokens = tokens.stream()
                .filter(t -> !t.getToken().equals(token.getToken()))
                .collect(Collectors.toList());
    }

    private void filterExpired() {
        LocalDateTime now = LocalDateTime.now();
        tokens = tokens.stream()
                .filter(t -> t.getExpires().isAfter(now))
                .collect(Collectors.toList());
    }
}
