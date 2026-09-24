# Code quality

## ADDED Requirements

### Requirement: Style is enforced
The build SHALL fail when Java code isn't formatted with google-java-format or when it has any
Checkstyle (`google_checks.xml`) warning.

#### Scenario: Unformatted change
- **WHEN** a contributor pushes code that isn't formatted
- **THEN** `mvn verify` and CI fail with a message saying to run `mvn spotless:apply`

### Requirement: Structured logging
Production code SHALL log through SLF4J. It SHALL NOT use `System.out` or `System.err`, except in
methods whose purpose is printing to the console.

#### Scenario: Fetch failure
- **WHEN** `Fetcher` fails to download a page
- **THEN** a `warn` message is logged through SLF4J

### Requirement: Continuous integration
Every push and pull request SHALL run `mvn verify` for both server projects.

#### Scenario: Pull request
- **WHEN** a pull request is opened
- **THEN** a GitHub Actions check reports the result of `mvn verify`
