# Testing

## ADDED Requirements

### Requirement: Tests are discovered and run
Every test class SHALL be named `*Test` so Maven Surefire runs it.

#### Scenario: Running the indexer tests
- **WHEN** `mvn test` runs in `server/search-engine`
- **THEN** the reported test count is greater than zero

### Requirement: Tests are hermetic
Unit tests SHALL NOT use the network or a database. They MUST use local fixtures or fakes.

#### Scenario: Offline build
- **WHEN** `mvn verify` runs with no network access and no Postgres
- **THEN** all tests pass in both projects

### Requirement: Assertions are meaningful
Each test SHALL contain at least one assertion that can fail.

#### Scenario: Description test
- **WHEN** `DocumentProcessor.getDescription()` returns an unexpected value
- **THEN** `DocumentProcessorTest` fails

### Requirement: Coverage is reported
Both builds SHALL produce a JaCoCo report during `mvn verify`.

#### Scenario: Coverage report
- **WHEN** `mvn verify` finishes
- **THEN** `target/site/jacoco/index.html` exists
