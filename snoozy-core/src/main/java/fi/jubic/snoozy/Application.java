package fi.jubic.snoozy;

import fi.jubic.snoozy.logging.DefaultLoggingFilter;
import fi.jubic.snoozy.logging.LoggingFilter;
import fi.jubic.snoozy.staticfiles.StaticFiles;
import fi.jubic.snoozy.websocket.WebSocketHandler;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * Basic application interface. Based loosely on {@link javax.ws.rs.core.Application}
 * and {@link javax.ws.rs.core.Application#getSingletons()}.
 */
public interface Application {
    Set<Object> getSingletons();

    /**
     * Override to provide a custom startup banner. If empty is returned, startup logging verbosity
     * is reduced. Following info is not logged without a banner message:
     *
     * <ul>
     *     <li><i>Listening on</i> message</li>
     *     <li>Registered resources</li>
     *     <li>Static files</li>
     * </ul>
     */
    default Optional<String> getBanner() {
        Package pkg = getClass().getPackage();
        if (pkg.getImplementationTitle() == null) {
            return Optional.of(String.format("%n\t::%n\t:: dev mode ::%n\t::"));
        }
        return Optional.of(
                String.format(
                        "%n\t::%n\t:: %s: v%s ::%n\t::",
                        pkg.getImplementationTitle(),
                        pkg.getImplementationVersion()
                )
        );
    }

    /**
     * Override to provide filter with custom logging. Silent logger variant
     * ({@link fi.jubic.snoozy.logging.NoopLoggingFilter}) can be used to reduce verbosity for
     * testing.
     */
    default LoggingFilter getLoggingFilter() {
        return new DefaultLoggingFilter();
    }

    default Set<StaticFiles> getStaticFiles() {
        return Collections.emptySet();
    }

    default Set<WebSocketHandler> getWebSocketHandlers() {
        return Collections.emptySet();
    }
}
