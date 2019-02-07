## Example of Application class

```java
@ApplicationPath("/api")
public class App extends Application {
    @Inject
    Configuration configuration;

    @Inject
    StatefulAuthenticator<User> authenticator;

    @Inject
    AuthenticationResource authenticationResource;

    @Inject
    public App() {

    }

    @Override
    public Set<Object> getResources() {
        Set<Object> resources = new HashSet<>();
        Collections.addAll(
                resources,
                authenticationResource
        );

        return resources;
    }

    @Override
    public IConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public Auth<User> getAuth() {
        List<String> excludes = Arrays.asList("^/$", "^/index.html");

        return Auth.<User>builder()
                .setUserClass(User.class)
                .setAuthenticator(authenticator)
                .setExcludes(excludes)
                .build();
    }

    @Override
    public StaticFiles getStaticFiles() {
        return StaticFiles.builder()
                .setClassLoader(App.class.getClassLoader())
                .build();
    }

    public static void main(String[] args) {
        App app = DaggerAppComponent.create()
                .getApp();

        new UndertowServer().start(app);
    }
}
```