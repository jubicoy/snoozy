package fi.jubic.resteasy.server;

public interface IConfiguration {
    DeploymentEnvironment environment();
    ServerConfiguration serverConfiguration();

    enum DeploymentEnvironment {
        DEVELOPMENT,
        PRODUCTION
    }
}
