package fi.jubic.snoozy.example;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import fi.jubic.easyconfig.ConfigMapper;
import fi.jubic.easyconfig.MappingException;

import javax.inject.Singleton;

@Singleton
@Component(modules = ExampleApplicationComponent.ExampleApplicationModule.class)
public interface ExampleApplicationComponent {
    ExampleApplication getApplication();

    @Module
    class ExampleApplicationModule {
        @Provides
        @Singleton
        static Configuration providerConfiguration() {
            try {
                return new ConfigMapper().read(Configuration.class);
            } catch (MappingException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
