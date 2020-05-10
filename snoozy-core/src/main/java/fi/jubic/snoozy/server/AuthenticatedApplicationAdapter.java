package fi.jubic.snoozy.server;

import fi.jubic.snoozy.AuthenticatedApplication;
import fi.jubic.snoozy.ServerConfiguration;
import fi.jubic.snoozy.auth.Authentication;
import fi.jubic.snoozy.auth.UserPrincipal;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuthenticatedApplicationAdapter<P extends UserPrincipal> extends ApplicationAdapter {
    private final AuthenticatedApplication<P> authenticatedApplication;
    private AuthFilterAdapter<P> authFilterAdapter;

    /**
     * Wrap an authenticated Application injecting the following built-ins.
     *
     * <ul>
     *     <li>Request logging</li>
     *     <li>Auth filter</li>
     * </ul>
     */
    public AuthenticatedApplicationAdapter(
            AuthenticatedApplication<P> application,
            ServerConfiguration serverConfiguration
    ) {
        super(application, serverConfiguration);
        this.authenticatedApplication = application;
    }

    public void setAuthFilterAdapter(AuthFilterAdapter<P> authFilterAdapter) {
        this.authFilterAdapter = authFilterAdapter;
    }

    public Authentication<P> getAuthentication() {
        return authenticatedApplication.getAuthentication();
    }

    @Override
    public Set<Object> getSingletons() {
        return Stream
                .concat(
                        super.getSingletons().stream(),
                        Stream.of(Objects.requireNonNull(authFilterAdapter))
                )
                .collect(Collectors.toSet());
    }
}
