package fi.jubic.snoozy.server;

import fi.jubic.snoozy.MethodAccess;
import fi.jubic.snoozy.StaticFiles;
import fi.jubic.snoozy.auth.Authentication;
import fi.jubic.snoozy.auth.Authenticator;
import fi.jubic.snoozy.auth.Authorizer;
import fi.jubic.snoozy.auth.UserPrincipal;
import fi.jubic.snoozy.filters.StaticFilesFilter;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        this.authenticator = authentication.authenticator();
        this.authorizer = authentication.authorizer();
        this.resourceMethodGetter = resourceMethodGetter;
        this.contextPusher = contextPusher;

        this.methodAccessParser = new MethodAccessParser(true);
        this.methodAccessCache = new HashMap<>();
    }

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

        Optional<P> principal = authentication.tokenParser()
                .parse(servletRequest)
                .flatMap(authenticator::authenticate);

        principal.ifPresent(p -> contextPusher.push(authentication.userClass(), p));

        if (!isAuthorized(methodAccess, principal)) {
            unauthorized(containerRequestContext);
        }
    }

    @Override
    public boolean filter(StaticFiles staticFiles, HttpServletRequest request) {
        Optional<P> principal = authentication.tokenParser()
                .parse(request)
                .flatMap(authenticator::authenticate);

        return isAuthorized(staticFiles.methodAccess(), principal);
    }

    private boolean isAuthorized(MethodAccess methodAccess, Optional<P> principal) {
        if (methodAccess.level().equals(MethodAccess.Level.DenyAll)) {
            return false;
        }

        if (methodAccess.level().equals(MethodAccess.Level.Anonymous)) {
            return true;
        }

        if (!principal.isPresent()) return false;

        if (methodAccess.level().equals(MethodAccess.Level.Authenticated)) {
            return true;
        }

        if (methodAccess.level().equals(MethodAccess.Level.Roles)) {
            return methodAccess.values()
                    .stream()
                    .anyMatch(role -> authorizer.authorize(principal.get(), role));
        }

        return false;
    }

    private void unauthorized(ContainerRequestContext containerRequestContext) {
        containerRequestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .build()
        );
    }
}
