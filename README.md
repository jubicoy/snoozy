# Snoozy

Snoozy allows building JAX-RS applications without depending on the
specific app-server or Servlet container implementation.

## Servers

Current server selection consists of a single 

## Application

Application is an extended version of `javax.ws.rs.core.Application` providing
additional feature declarations, such as static files. The additional features
are defined using a declarative api keeping things implementation agnostic.

### Example of Application class

```java
public class App extends Application {
    @Override
    public Set<Object> getClasses() {
        return Stream.of(
                // Place your filter, provider and resource classes here
        ).collect(Collectors.toSet());
    }
    
    @Override
    public Set<Object> getSingletons() {
        return Stream.of(
                // Place your filters, providers and resources here
        ).collect(Collectors.toSet());
    }

    public static void main(String[] args) {
        new UndertowServer().start(new App());
    }
}
```
