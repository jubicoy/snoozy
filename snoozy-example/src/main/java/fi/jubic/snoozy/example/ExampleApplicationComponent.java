package fi.jubic.snoozy.example;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component
public interface ExampleApplicationComponent {
    ExampleApplication getApplication();
}
