package fi.jubic.snoozy.example;

import fi.jubic.easyschedule.StartupScheduler;
import fi.jubic.easyschedule.TaskScheduler;
import fi.jubic.easyschedule.dbunit.DbUnitTask;
import fi.jubic.easyschedule.liquibase.LiquibaseTask;
import fi.jubic.snoozy.MethodAccess;
import fi.jubic.snoozy.StaticFiles;
import fi.jubic.snoozy.auth.AuthenticatedApplication;
import fi.jubic.snoozy.auth.Authentication;
import fi.jubic.snoozy.auth.implementation.DefaultAuthorizer;
import fi.jubic.snoozy.auth.implementation.HeaderParser;
import fi.jubic.snoozy.auth.implementation.StatefulAuthenticator;
import fi.jubic.snoozy.example.resources.AuthenticationResource;
import fi.jubic.snoozy.example.resources.HelloWorldResource;
import fi.jubic.snoozy.filters.UrlRewrite;
import fi.jubic.snoozy.undertow.UndertowServer;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationPath("/api")
public class ExampleApplication extends AuthenticatedApplication<User> {
    @Inject
    Configuration configuration;

    @Inject
    StatefulAuthenticator<User> authenticator;

    @Inject
    HelloWorldResource helloWorldResource;
    @Inject
    AuthenticationResource authenticationResource;

    @Inject
    ExampleApplication() {
    }

    @Override
    public Set<Object> getSingletons() {
        return Stream.of(helloWorldResource, authenticationResource)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<StaticFiles> getStaticFiles() {
        StaticFiles publicFiles = StaticFiles.builder()
                .setPrefix("public")
                .setClassLoader(this.getClass().getClassLoader())
                .setMethodAccess(MethodAccess.anonymous())
                .setRewrite(
                        UrlRewrite.builder()
                                .setFrom("^\\/(?!(api|static|img|html|images|fonts)|.*\\.html|.*\\.json).*$")
                                .setTo("index.html")
                                .build()
                )
                .build();

        StaticFiles authFiles = StaticFiles.builder()
                .setPrefix("auth")
                .setPath("auth")
                .setClassLoader(this.getClass().getClassLoader())
                .build();

        return Stream.of(publicFiles, authFiles)
                .collect(Collectors.toSet());
    }

    @Override
    public Authentication<User> getAuthentication() {
        return Authentication.<User>builder()
                .setAuthenticator(authenticator)
                .setAuthorizer(new DefaultAuthorizer<>())
                .setTokenParser(HeaderParser.of("Authorization"))
                .setUserClass(User.class)
                .build();
    }

    public static void main(String[] args) {
        ExampleApplication application = DaggerExampleApplicationComponent
                .create()
                .getApplication();
        Configuration config = application.configuration;

        TaskScheduler scheduler = new StartupScheduler()
                .registerStartupTask(
                        new LiquibaseTask(
                                config.getJooqConfiguration(),
                                "migrations.xml"
                        )
                )
                .registerStartupTask(
                        new DbUnitTask(
                                config.getJooqConfiguration(),
                                ExampleApplication.class.getClassLoader(),
                                "dataset.xml",
                                "dataset.dtd"
                        )
                );
        scheduler.start();

        new UndertowServer().start(application, config);
    }
}
