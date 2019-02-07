package fi.jubic.resteasy.server;

import fi.jubic.resteasy.auth.Auth;
import fi.jubic.resteasy.auth.AuthFilter;
import fi.jubic.resteasy.auth.UserPrincipal;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import javax.annotation.Nullable;
import javax.ws.rs.container.ContainerRequestFilter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Application<U extends UserPrincipal> extends javax.ws.rs.core.Application {
    public abstract Set<Object> getResources();

    @Nullable
    public abstract Auth<U> getAuth();
    @Nullable
    public abstract StaticFiles getStaticFiles();

    @Override
    public Set<Object> getSingletons() {
        Set<Object> filters = new HashSet<>();
        filters.add(new LoggingFilter());

        getAuthFilter().ifPresent(filters::add);

        return Stream.concat(getResources().stream(), filters.stream())
            .collect(Collectors.toSet());
    }

    public Configuration getLoggerConfiguration() {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder()
                .setStatusLevel(Level.INFO);

        AppenderComponentBuilder appenderBuilder = builder.newAppender("Stdout", "CONSOLE")
                .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);

        builder.add(appenderBuilder);

        builder.add(
                builder.newRootLogger(Level.INFO)
                    .add(builder.newAppenderRef("Stdout"))
        );

        return builder.build();
    }

    private Optional<ContainerRequestFilter> getAuthFilter() {
        Auth<U> auth = getAuth();

        if (auth == null) {
            return Optional.empty();
        }

        AuthFilter.Builder<U> builder = AuthFilter.<U>builder()
                .setAuthenticator(auth.authenticator())
                .setPrincipalClass(auth.userClass());

        if (auth.authorizer() != null) {
            builder = builder.setAuthorizer(auth.authorizer());
        }

        if (auth.tokenParser() != null) {
            builder = builder.setTokenParser(auth.tokenParser());
        }

        return Optional.of(builder.build());
    }
}
