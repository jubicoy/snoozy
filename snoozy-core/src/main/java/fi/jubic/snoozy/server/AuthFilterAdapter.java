package fi.jubic.snoozy.server;

import fi.jubic.snoozy.MethodAccess;
import fi.jubic.snoozy.auth.Authentication;
import fi.jubic.snoozy.auth.Authenticator;
import fi.jubic.snoozy.auth.Authorizer;
import fi.jubic.snoozy.auth.UserPrincipal;
import fi.jubic.snoozy.filters.StaticFilesFilter;
import fi.jubic.snoozy.staticfiles.StaticFiles;

import javax.annotation.Nullable;
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

        Optional<P> optionalPrincipal = authentication.getTokenParser()
                .parse(servletRequest)
                .flatMap(authenticator::authenticate);

        optionalPrincipal.ifPresent(
                principal -> contextPusher.push(authentication.getUserClass(), principal)
        );

        getAuthErrorResponseSupplier(methodAccess, optionalPrincipal.orElse(null))
                .ifPresent(containerRequestContext::abortWith);
    }

    @Override
    public Optional<Response> filter(StaticFiles staticFiles, HttpServletRequest request) {
        Optional<P> optionalPrincipal = authentication.getTokenParser()
                .parse(request)
                .flatMap(authenticator::authenticate);

        return getAuthErrorResponseSupplier(
                staticFiles.getMethodAccess(),
                optionalPrincipal.orElse(null)
        );
    }

    private Optional<Response> getAuthErrorResponseSupplier(
            MethodAccess methodAccess,
            @Nullable P principal
    ) {
        if (methodAccess.getLevel().equals(MethodAccess.Level.DenyAll)) {
            return Optional.of(authentication.getForbidden().get());
        }

        if (methodAccess.getLevel().equals(MethodAccess.Level.Anonymous)) {
            return Optional.empty();
        }

        if (principal == null) {
            return Optional.of(authentication.getUnauthorized().get());
        }

        if (methodAccess.getLevel().equals(MethodAccess.Level.Authenticated)) {
            return Optional.empty();
        }

        if (methodAccess.getLevel().equals(MethodAccess.Level.Roles)) {
            boolean roleAuthorized = methodAccess.getValues()
                    .stream()
                    .anyMatch(role -> authorizer.authorize(principal, role));
            if (roleAuthorized) {
                return Optional.empty();
            }
        }

        return Optional.of(authentication.getForbidden().get());
    }
}
