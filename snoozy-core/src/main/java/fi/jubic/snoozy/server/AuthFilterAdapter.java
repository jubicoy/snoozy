package fi.jubic.snoozy.server;

import fi.jubic.snoozy.MethodAccess;
import fi.jubic.snoozy.auth.Authentication;
import fi.jubic.snoozy.auth.Authenticator;
import fi.jubic.snoozy.auth.Authorizer;
import fi.jubic.snoozy.auth.UserPrincipal;
import fi.jubic.snoozy.filters.StaticFilesFilter;
import fi.jubic.snoozy.staticfiles.StaticFiles;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class AuthFilterAdapter<P extends UserPrincipal>
        implements ContainerRequestFilter, StaticFilesFilter {
    private final Authorizer<P> authorizer;
    private final Authenticator<P> authenticator;
    private final Authentication<P> authentication;
    private final ResourceMethodGetter resourceMethodGetter;
    private final ContextPusher contextPusher;

    private final MethodAccessParser methodAccessParser;
    private final Map<Method, MethodAccess> methodAccessCache;

    @Context
    HttpServletRequest servletRequest;

    private AuthFilterAdapter(
            Authentication<P> authentication,
            ResourceMethodGetter resourceMethodGetter,
            ContextPusher contextPusher
    ) {
        this.authentication = authentication;
        this.authenticator = authentication.getAuthenticator();
        this.authorizer = authentication.getAuthorizer();
        this.resourceMethodGetter = resourceMethodGetter;
        this.contextPusher = contextPusher;

        this.methodAccessParser = new MethodAccessParser(true);
        this.methodAccessCache = new HashMap<>();
    }

    /**
     * Creator method for building an auth filter adapter.
     *
     * @param <P> User principal type
     */
    public static <P extends UserPrincipal> AuthFilterAdapter<P> of(
            Authentication<P> authentication,
            ResourceMethodGetter resourceMethodGetter,
            ContextPusher contextPusher
    ) {
        return new AuthFilterAdapter<>(
                authentication,
                resourceMethodGetter,
                contextPusher
        );
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        Method method = resourceMethodGetter.getMethod(containerRequestContext);

        MethodAccess methodAccess;
        if (methodAccessCache.containsKey(method)) {
            methodAccess = methodAccessCache.get(method);
        }
        else {
            methodAccess = methodAccessParser.parseAccess(method);
            methodAccessCache.put(method, methodAccess);
        }

        Optional<P> principal = authentication.getTokenParser()
                .parse(servletRequest)
                .flatMap(authenticator::authenticate);

        principal.ifPresent(p -> contextPusher.push(authentication.getUserClass(), p));

        if (!isAuthorized(methodAccess, principal)) {
            containerRequestContext.abortWith(authentication.getUnauthorized().get());
        }
    }

    @Override
    public boolean filter(StaticFiles staticFiles, HttpServletRequest request) {
        Optional<P> principal = authentication.getTokenParser()
                .parse(request)
                .flatMap(authenticator::authenticate);

        return isAuthorized(staticFiles.getMethodAccess(), principal);
    }

    @Override
    public Supplier<Response> getResponseSupplier() {
        return authentication.getUnauthorized();
    }

    private boolean isAuthorized(MethodAccess methodAccess, Optional<P> principal) {
        if (methodAccess.getLevel().equals(MethodAccess.Level.DenyAll)) {
            return false;
        }

        if (methodAccess.getLevel().equals(MethodAccess.Level.Anonymous)) {
            return true;
        }

        if (!principal.isPresent()) return false;

        if (methodAccess.getLevel().equals(MethodAccess.Level.Authenticated)) {
            return true;
        }

        if (methodAccess.getLevel().equals(MethodAccess.Level.Roles)) {
            return methodAccess.getValues()
                    .stream()
                    .anyMatch(role -> authorizer.authorize(principal.get(), role));
        }

        return false;
    }
}
