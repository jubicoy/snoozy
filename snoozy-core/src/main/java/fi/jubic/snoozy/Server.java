package fi.jubic.snoozy;

import fi.jubic.snoozy.auth.UserPrincipal;
import fi.jubic.snoozy.server.ApplicationAdapter;
import fi.jubic.snoozy.server.AuthFilterAdapter;
import fi.jubic.snoozy.server.AuthenticatedApplicationAdapter;

public interface Server {
    /**
     * Start an {@link Application} in a new thread(s).
     */
    default void start(
            Application application,
            ServerConfigurator serverConfigurator
    ) {
        start(application, serverConfigurator.getServerConfiguration());
    }

    /**
     * Start an {@link Application} in a new thread(s).
     */
    default void start(
            Application application,
            ServerConfiguration serverConfiguration
    ) {
        ApplicationAdapter applicationAdapter = new ApplicationAdapter(
                application,
                serverConfiguration
        );
        start(
                applicationAdapter,
                serverConfiguration
        );
        applicationAdapter.logStartup();
    }

    /**
     * Start an {@link AuthenticatedApplication} in a new thread(s).
     */
    default <P extends UserPrincipal> void start(
            AuthenticatedApplication<P> application,
            ServerConfigurator serverConfigurator
    ) {
        start(application, serverConfigurator.getServerConfiguration());
    }

    /**
     * Start an {@link AuthenticatedApplication} in a new thread(s).
     */
    default <P extends UserPrincipal> void start(
            AuthenticatedApplication<P> application,
            ServerConfiguration serverConfiguration
    ) {
        AuthenticatedApplicationAdapter<P> applicationAdapter
                = new AuthenticatedApplicationAdapter<>(application, serverConfiguration);
        start(
                applicationAdapter,
                serverConfiguration
        );
        applicationAdapter.logStartup();
    }

    /**
     * <p>
     *     Start an {@link ApplicationAdapter} in a new thread(s). The main control flow can reach
     *     its end after calling this function without stopping the application.
     * </p>
     *
     * <p>
     *     A {@link Server} implementation should not override the non-adapter methods to avoid
     *     picking up unnecessary housekeeping responsibilities like post-start logging.
     * </p>
     */
    void start(
            ApplicationAdapter applicationAdapter,
            ServerConfiguration serverConfiguration
    );

    /**
     * <p>
     *     Start an {@link AuthenticatedApplicationAdapter} in a new thread(s). The main control
     *     flow can reach its end after calling this function without stopping the application.
     * </p>
     *
     * <p>
     *     A {@link Server} implementation should not override the non-adapter methods to avoid
     *     picking up unnecessary housekeeping responsibilities like post-start logging.
     * </p>
     *
     * <p>
     *     The {@link Server} implementation needs to call
     *     {@link AuthenticatedApplicationAdapter#setAuthFilterAdapter(AuthFilterAdapter)} to when
     *     starting the application to setup server specific auth support.
     * </p>
     */
    <P extends UserPrincipal> void start(
            AuthenticatedApplicationAdapter<P> applicationAdapter,
            ServerConfiguration serverConfiguration
    );

    void stop();
}
