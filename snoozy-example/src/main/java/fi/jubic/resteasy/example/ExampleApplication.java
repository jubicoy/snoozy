package fi.jubic.resteasy.example;

import fi.jubic.easyconfig.ConfigMapper;
import fi.jubic.easyconfig.MappingException;
import fi.jubic.resteasy.MethodAccess;
import fi.jubic.resteasy.StaticFiles;
import fi.jubic.resteasy.auth.AuthenticatedApplication;
import fi.jubic.resteasy.auth.Authentication;
import fi.jubic.resteasy.auth.implementation.DefaultAuthorizer;
import fi.jubic.resteasy.auth.implementation.HeaderParser;
import fi.jubic.resteasy.auth.implementation.StatefulAuthenticator;
import fi.jubic.resteasy.undertow.UndertowServer;
import fi.jubic.snoozy.StartupScheduler;
import fi.jubic.snoozy.TaskScheduler;
import fi.jubic.snoozy.dbunit.DbUnitTask;
import fi.jubic.snoozy.liquibase.LiquibaseTask;

import javax.ws.rs.ApplicationPath;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationPath("/api")
public class ExampleApplication extends AuthenticatedApplication<User> {
    private StatefulAuthenticator<User> authenticator;

    public ExampleApplication() {
        authenticator = new StatefulAuthenticator<>();
    }

    @Override
    public Set<Object> getSingletons() {
        return Stream.of(
                new HelloWorldResource(authenticator)
        ).collect(Collectors.toSet());
    }

    @Override
    public Set<StaticFiles> getStaticFiles() {
        StaticFiles publicFiles = StaticFiles.builder()
                .setPrefix("public")
                .setClassLoader(this.getClass().getClassLoader())
                .setMethodAccess(MethodAccess.anonymous())
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

    public static void main(String[] args) throws MappingException {
        Configuration config = new ConfigMapper()
                .read(Configuration.class);

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

        ExampleApplication application = new ExampleApplication();

        new UndertowServer().start(application, config);
    }
}
