# Snoozy

![Build](https://github.com/jubicoy/snoozy/workflows/Build/badge.svg)

Snoozy allows building JAX-RS applications without depending on the
specific app-server or Servlet container implementation.

## Servers

Current server selection consists of:
* Undertow

## Application

Application is an altered version of `jakarta.ws.rs.core.Application` providing
additional feature declarations, such as static files. The additional features
are defined using a declarative api keeping things implementation agnostic.

`jakarta.ws.rs.core.Application#getClasses()` and
`jakarta.ws.rs.core.Application#getProperties()` equivalent methods have been
removed from the `Application` class. `getClasses()` approach for defining
resources relies on the implementation specific dependency injection. Using
only `getSingletons()` both mandates and allows external instance initialization.
Once `getClasses()` was removed there was no reason to keep `getProperties()`
around anymore.


### Example of Application class

```java
public class App extends Application {
    @Override
    public Set<Object> getSingletons() {
        return Stream.of(
                // Place your filters, providers and resources here
        ).collect(Collectors.toSet());
    }

    @Override
    public Set<StaticFiles> getStaticFiles() {
        return Stream.of(
                // Place your StaticFiles objects here
        ).collect(Collectors.toSet());
    }

    public static void main(String[] args) {
        new UndertowServer().start(new App());
    }
}
```

## AuthenticatedApplication&lt;P extends UserPrincipal&gt;

AuthenticatedApplication is an extended version of our Application
implementation that provides additional feature declarations for authentication

### Example of AuthenticatedApplication

```java
public class App extends AuthenticatedApplication<User> {
    @Override
    public Set<Object> getSingletons() {
        return Stream.of(
                // Place your filters, providers and resources here
        ).collect(Collectors.toSet());
    }

    @Override
    public Set<StaticFiles> getStaticFiles() {
        return Stream.of(
                // Place your StaticFiles objects here
        ).collect(Collectors.toSet());
    }

    @Override
    public Authentication<User> getAuthentication() {
        return Authentication.<User>builder()
                .setAuthenticator(new StatefulAuthenticator<>())
                .setAuthorizer(new DefaultAuthorizer<>())
                .setTokenParser(HeaderParser.of("Authorization"))
                .setUserClass(User.class)
                .build();
    }

    public static void main(String[] args) {
        new UndertowServer().start(new App());
    }
}
```

## StaticFiles

StaticFiles are used to define URL paths, resource paths and access levels of
static files.

Notes:
* By default, methodAccess is set to authenticated.
* path is the relative URL path
* prefix is the relative resource path
* rewrite allows you to define a regex rule to rewrite matching paths. For
  example, define rules to return index.html on other paths than just `/` and
  `/index.html` to support client-side routing.

### Example of a StaticFiles declaration

```java
@Override
public Set<StaticFiles> getStaticFiles() {
    // Files will be served from the folder "public" under "resources" to
    // everybody
    StaticFiles files = StaticFiles.builder()
            .setPrefix("public")
            .setClassLoader(this.getClass().getClassLoader())
            .setMethodAccess(MethodAccess.anonymous())
            .setRewrite(
                // Deprecated UrlRewrite usage.
                UrlRewrite.build()
                    // Everything except paths starting with /api or /assets and
                    // paths ending with .html or .png will be rewrited to
                    // /index.html
                    .setFrom("^\/(?!(((api|assets).*)|.*\.(html|png)$)).*$")
                    .setTo("/index.html")
                    .build()
            )
            .build();

    return Stream.of(files)
            .collect(Collectors.toSet());
}
```

## Authentication&lt;P extends UserPrincipal&gt;

Authentication is used to define the behaviour of the authenticator.

Notes:
* If available, a User Principal is injected into context and can be accessed in
  resources via the JAX-RS @Context annotation. e.g `@Context User user`

Properties:
* Authenticator handles the actual authentication. It takes a token and if
  valid, returns the corresponding user. A simple in-memory stateful
  authenticator is provided.
* Authorizer checks that the user has the correct role and return true or false.
  This can be used to defined tiered access level where an admin can access
  all resources that a user can, but a user cannot access everything that an
  admin can. A simple string comparison authorizer is provided.
* TokenParser takes the actual request and if valid, returns a token. Simple
  header and cookie parsers are provided.
* UserClass is a class extending the UserPrincipal class. It must contain at
  least a name and role, but additional metadata may be kept.

### Example of Authentication

```java
@Override
public Authentication<User> getAuthentication() {
    return Authentication.<User>builder()
            .setAuthenticator(new StatefulAuthenticator<>())
            .setAuthorizer(new DefaultAuthorizer<>())
            .setTokenParser(HeaderParser.of("Authorization"))
            .setUserClass(User.class)
            .build();
}
```

## Additional Resources

### Dependency Injection

Our choice of DI framework is Dagger 2. Here is a small getting started guide on
how to use Dagger 2 with Snoozy.

* Add the following to your pom.xml dependencies

```xml
<dependency>
    <groupId>com.google.dagger</groupId>
    <artifactId>dagger</artifactId>
    <version>2.21</version>
</dependency>
<dependency>
    <groupId>com.google.dagger</groupId>
    <artifactId>dagger-compiler</artifactId>
    <version>2.21</version>
    <scope>provided</scope>
</dependency>
```
* Create a Dagger Component which at the very least has a method that will
  provide your Application object.

```java
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component
public interface ApplicationComponent {
    Application getApplication();
}
```

* Instead of instantiating an Application object yourself in your main method,
  let Dagger do the work for you.

```java
Application application = DaggerApplication.create()
        .getApplication();
```

__NOTE:__ If you have not ran `mvn package` after the previous step, your IDE might
complain about DaggerApplication. This class is generated during compile time
and will be present in the maven target folder.

* In some cases you might want Dagger to instantiate an object that it does not
  know how to. For example, your configuration object or and interface. In these
  cases you will have to create a Dagger module which provides a factory method:

```java
import dagger.Module;
import dagger.Provides;
import fi.jubic.easyconfig.ConfigMapper;
import fi.jubic.easyconfig.MappingException;

import javax.inject.Singleton;

@Module
class ApplicationModule {
    @Provides
    @Singleton
    static Configuration provideConfiguration() {
        try {
            return new ConfigMapper().read(Configuration.class);
        } catch (MappingException e) {}

        return null;
    }
}
```

You will also have to register this Module in the Component we previously
created.

```java
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    Application getApplication();
}
```

__NOTE:__ If you want Dagger to instantiate an object of your own which has no
dependencies. You can just add @Inject to a constructor with no parameters.
