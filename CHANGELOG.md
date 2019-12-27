# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres poorly to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
- Add support for configurable request logging.
- Add support for configurable startup banner.

### Changed
- `Application` no longer extends `javax.ws.rs.core.Application`.
  `getClasses()` and `getProperties()` methods are removed.
- Move `AuthenticatedApplication` to root package.

### Fixed
- Fix unchecked cast in `WebApplicationException` mapping.
- Fix CRLF log injection.

## [0.5.0] 2019-11-15
### Added
- Configurable unauthorized response message.
- Add 'default' frontend `StaticFiles` definition.

### Changed
- Set `@ApplicationPath` manually instead of mirroring the value to 
  `ApplicationAdapter`.

### Removed
- Remove `fi.jubic:easyvalue` dependency.
- Remove experimental converters.

### Fixed
- Fix invalid paths when `@ApplicationPath` is defined with a leading
  slash.

## [0.4.0] - 2019-11-01
### Added
- Support multipart requests using Servlet API

### Fixed
- Fix `ApplicationAdapter` generation when `Application` is an inner
  class.

## [0.3.1] - 2019-10-18

## [0.3.0] - 2019-10-15
### Changed
- Introduced breaking changes in `fi.jubic:easyconfig` dependency
  upgrade.

## [0.2.1] - 2019-10-15

## [0.2.0] - 2019-10-14
### Added
- Improved Java 11 support.
- Added `WebApplicationException` response mapping.
- Added `java.time.Instant` parameter converter.
- Print application version on startup.

### Changed
- Moved other `java.time.*` parameter converters out of `experimental`
  package.
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