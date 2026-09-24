# Change: Add a working test suite

## Why
The project's tests give no real protection today:
- **`server/search-engine` runs 0 tests.** Its test classes are named `*Tester`, and Surefire only
  picks up `*Test`, `Test*`, and `*TestCase`. `mvn test` reports success anyway.
- **Several tests depend on the live internet.** `RobotsHandlerTester` downloads google.com and
  facebook.com robots.txt files. `DocumentProcessorTester` fetches Wikipedia.
- **`DocumentProcessorTester` can never fail.** `assertThat(x != null)` asserts nothing without
  `.isTrue()`. Its URL `www.wikipedia.org/wiki/Education` also has no scheme, so `fetchDocument`
  returns null.
- **The searcher's only test, `SearcherApplicationTests.contextLoads`, always fails.** On JDK 21 it
  first hits a cglib `InaccessibleObjectException`. With
  `--add-opens java.base/java.lang=ALL-UNNAMED` it gets past that, then fails because no
  datasource is configured. It needs a live Postgres, so it can't be a unit test.

## What changes
- Rename the `*Tester` classes to `*Test`.
- Replace network access in tests with HTML and robots.txt fixtures in `src/test/resources`.
- Replace `contextLoads` with unit tests.
- Add unit tests for the pure logic in both projects, with no database or network.
- Add a JaCoCo coverage report (report only, no threshold).

## Impact
- Test code and `pom.xml` only. No production code changes.
- Tests record **current** behavior. Where current behavior is a known bug (see
  `fix-known-bugs`), assert only the parts the bug doesn't affect, and add a comment
  `// Known bug: see openspec/changes/fix-known-bugs`. Never use `@Ignore`.
