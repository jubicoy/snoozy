# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres poorly to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.10.3] - 2024-09-05
### Changed
- Bump fi.jubic:easyparent from 0.1.13 to 0.1.14.
- Bump io.undertow:undertow-core from 2.3.16.Final to 2.3.17.Final.

## [0.10.2] - 2024-08-31
### Changed
- Bump io.undertow:undertow-core from 2.3.13.Final to 2.3.16.Final.
- Bump io.swagger.core.v3:swagger-jaxrs2-jakarta from 2.2.21 to 2.2.23.
- Bump resteasy.version from 6.2.8.Final to 6.2.10.Final.
- Bump jakarta.servlet:jakarta.servlet-api from 6.0.0 to 6.1.0.
- Bump com.fasterxml.jackson.datatype:jackson-datatype-jsr310 from 2.17.1 to 2.17.2.
- Bump org.junit.jupiter:junit-jupiter from 5.10.2 to 5.11.0.
- Bump org.junit.jupiter:junit-jupiter-engine from 5.10.2 to 5.11.0.
- Bump fi.jubic:easyparent from 0.1.12 to 0.1.13.
- Bump ch.qos.logback:logback-classic from 1.5.6 to 1.5.7.
- Bump org.slf4j:slf4j-api from 2.0.13 to 2.0.16.

## [0.10.1] - 2024-05-06
### Changed
- Bump fi.jubic:easyparent from 0.1.11 to 0.1.12.
- Bump com.fasterxml.jackson.datatype:jackson-datatype-jsr310 from 2.16.0 to 2.17.1.
- Bump io.swagger.core.v3:swagger-jaxrs2-jakarta from 2.2.19 to 2.2.21.
- Bump resteasy.version from 6.2.6.Final to 6.2.8.Final.
- Bump org.slf4j:slf4j-api from 2.0.9 to 2.0.13.
- Bump io.undertow:undertow-core from 2.3.10.Final to 2.3.13.Final.
- Bump ch.qos.logback:logback-classic from 1.4.14 to 1.5.6.

## [0.10.0] - 2023-12-13
### Changed
- Bump fi.jubic:easyparent from 0.1.10 to 0.1.11.
- Bump io.swagger.core.v3:swagger-jaxrs2-jakarta from 2.2.9 to 2.2.19.
- Bump com.fasterxml.jackson.datatype:jackson-datatype-jsr310 from 2.15.2 to 2.16.0.
- Bump org.slf4j:slf4j-api from 1.7.36 to 2.0.9.
- Bump ch.qos.logback:logback-classic from 1.4.6 to 1.4.14.
- Bump jakarta.servlet-api from 5.0.0 to 6.0.0.
- Bump resteasy.version from 6.2.3.Final to 6.2.6.Final.
- Bump io.undertow:undertow-core from 2.3.5.Final to 2.3.10.Final.

## [0.9.1] - 2022-09-02
### Security
- Update RESTEasy and Undertow

## [0.9.0] - 2022-06-20
### Changed
- Use jakarta.servlet and jakarta.ws.rs instead of the corresponding javax classes.

### Security
- Update parent and dependencies.

## [0.8.2] - 2022-04-20
### Fixed
- Log all unexpected exceptions.

## [0.8.1] - 2022-01-04
### Fixed
- Update parent to fix owasp dependency check run.

## [0.8.0] - 2022-01-04
### Removed
- Dropped Java 8 support.

### Fixed
- Rethrow unsupported exceptions when mapping exceptions to responses.

### Security
- Update dependencies.

## [0.7.2] - 2021-12-10
### Added
- Full Java 17 support.

### Security
- Update dependencies.
- Use Undertow 2.2.9 release instead of the version shipping with RESTEasy.

## [0.7.1] - 2020-12-29
### Security
- Update dependencies.
- Use an Undertow 2.2.X release instead of 2.0.X bundled with RESTEasy.

## [0.7.0] - 2020-05-30
### Added
- Swagger intergation. The OpenAPI JSON is automatically generated and served along with Swagger UI when in development mode or explicitly configured.
- `AuthenticatedApplicationAdapter` for explicitly wrapping an `AuthenticatedApplication`.

### Changed
- Refactor `StaticFiles` into an interface and use the value class version as a default implementation.
- Refactor `Server` to allow its implementations to be defined using only the `ApplicationAdapter` or `AuthenticatedApplicationAdapter`.
- Refactor `Authentication` into an interface and use the value class version as a default implementation.
- Return `Optional` of auth error response from `StaticFilesFilter`.

### Removed
- Deprecated `StaticFiles` getters without the `get` prefix.
- Deprecated `Authentication` getters without the `get` prefix.
- Deprecated getters and builder from `UrlRewrite`.

### Fixed
- Return correct HTTP 401/403 from `AuthFilterAdapter`.

## [0.6.2] - 2020-03-06
### Security
- Update RESTEasy.

## [0.6.1] - 2020-02-07
### Fix
- Fix NPEs with missing `User-Agent` header.

## [0.6.0] - 2020-02-03
### Added
- Add support for configurable request logging.
- Add support for configurable startup banner.

### Changed
- `Application` no longer extends `javax.ws.rs.core.Application`. `getClasses()` and `getProperties()` methods are removed.
- Move `AuthenticatedApplication` to root package.

### Fixed
- Fix unchecked cast in `WebApplicationException` mapping.
- Fix CRLF log injection.

## [0.5.0] 2019-11-15
### Added
- Configurable unauthorized response message.
- Add 'default' frontend `StaticFiles` definition.

### Changed
- Set `@ApplicationPath` manually instead of mirroring the value to `ApplicationAdapter`.

### Removed
- Remove `fi.jubic:easyvalue` dependency.
- Remove experimental converters.

### Fixed
- Fix invalid paths when `@ApplicationPath` is defined with a leading slash.

## [0.4.0] - 2019-11-01
### Added
- Support multipart requests using Servlet API

### Fixed
- Fix `ApplicationAdapter` generation when `Application` is an inner
  class.

## [0.3.1] - 2019-10-18

## [0.3.0] - 2019-10-15
### Changed
- Introduced breaking changes in `fi.jubic:easyconfig` dependency upgrade.

## [0.2.1] - 2019-10-15

## [0.2.0] - 2019-10-14
### Added
- Improved Java 11 support.
- Added `WebApplicationException` response mapping.
- Added `java.time.Instant` parameter converter.
- Print application version on startup.

### Changed
- Moved other `java.time.*` parameter converters out of `experimental` package.
- Reflection is no longer used for `ApplicationAdapter` creation.

## [0.1.2] - 2019-04-10
### Fixed
- `PathParam` handling when logging registered resources.

## [0.1.1] - 2019-03-22
### Added
- `UrlRewrite` to `StaticFiles` for serving browser routed frontends.

### Changed
- Use `slf4j` and `logback` for logging provider instead of `log4j`.

## [0.1.0] - 2019-03-08
