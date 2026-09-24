# Tasks

Paths: `SE` = `server/search-engine/src/main/java/com/samjsoares/soar`,
`SR` = `server/searcher/seacher/src/main/java/com/samjsoares/soar/searcher`.

## 1. Logging
- [ ] 1.1 Replace `System.out` and `System.err` with an SLF4J `Logger` (the pattern `Crawler`
      already uses) in `SE/core/Fetcher.java`, `SE/core/RobotsHandler.java`, and
      `SE/core/InMemoryIndexer.java`. Failures log at `warn`; the rest log at `debug`/`info`.
- [ ] 1.2 Keep `System.out` in the CLI-style output methods: `TermProcessor.printCounts`,
      `TermProcessor.main`, `InMemoryIndexer.printIndex`, and `SR/driver/SearcherDriver.java`.
      Printing is what they're for.
- [ ] 1.3 Use SLF4J `{}` placeholders instead of string concatenation in log calls.

## 2. Dead code
- [ ] 2.1 Remove the unused `UrlUtil.shouldContainHttp`.
- [ ] 2.2 Remove `Fetcher.saveToFile` and its commented-out call, plus the `CHAR_SET` constant if
      nothing else uses it. **Keep** `readDocument`/`read`, which the crawler's offline mode uses.
- [ ] 2.3 Remove commented-out code (`// System.out...`, `//private final URL source;`, and so on).
- [ ] 2.4 `DatabaseIndexer.get`, `printIndex`, and `getCounts` are empty `Indexer` stubs. Leave
      them, but add a one-line comment saying they aren't supported by the database indexer.

## 3. Checkstyle cleanup (then enforce)
- [ ] 3.1 Replace star imports (`java.util.*`) with explicit imports.
- [ ] 3.2 Empty `catch` blocks in `UrlUtil`: add a comment explaining why the exception is
      ignored (it means "invalid URL → null").
- [ ] 3.3 Rename `URLServer` → `UrlServer`, `URLServerImpl` → `UrlServerImpl`, and
      `getRobotsTxtURL` → `getRobotsTxtUrl` (the AbbreviationAsWordInName rule). One commit.
- [ ] 3.4 Add Javadoc to public classes and public methods. Keep each to one or two sentences and
      don't write any that only restate the name. Fill in or delete empty `@param`/`@return` tags.
- [ ] 3.5 In both poms, set `<violationSeverity>warning</violationSeverity>` on the Checkstyle
      plugin so warnings fail the build. Check that `mvn verify` passes in both projects.

## 4. Small modernizations
- [ ] 4.1 Make fields `final` where they're only assigned in the constructor or initializer:
      `Crawler`, `Fetcher`, `RobotsHandler`, `DatabaseIndexer`, `SearcherImpl`, the DAOs, and
      `ScaledStemmer`.
- [ ] 4.2 `ScaledStemmer`: change the raw `new LRUCacheMap(256)` to `new LRUCacheMap<>(256)`.
- [ ] 4.3 `SearchController`: switch to constructor injection and make `gson` `static final`.
- [ ] 4.4 Make utility classes (`UrlUtil`, `StopWordsUtil`, `JdbcUtil`, `CollectionsUtil`, and the
      `constant` classes) `final` with a private constructor.
- [ ] 4.5 `StopWordsUtil`: make the array and set `private static final`, and wrap the set with
      `Collections.unmodifiableSet`. Leave the duplicate words in the list; the set ignores them.
- [ ] 4.6 `TermProcessor`: rename the local variable `termInfoInfo` → `termInfo`.
- [ ] 4.7 `DocumentProcessor`: rename `truncateWithElipsis` → `truncateWithEllipsis`.

## 5. Repository hygiene
- [ ] 5.1 Delete `server/search-engine/lib/` (18 old Spring 4.3 jars that Maven doesn't use) and
      `server/search-engine/search-engine.iml`.
- [ ] 5.2 `.gitignore`: fix the `*.imi` typo to `*.iml`.
- [ ] 5.3 Check whether the two `Beans.xml` files are loaded anywhere (`grep -r Beans.xml` and
      `@ImportResource`). If not, delete them. If they are, leave them.
- [ ] 5.4 Rename the `seacher` folder and artifactId to `searcher`. Update the README and the
      project layout table in `openspec/project.md`. Do this as its own commit.

## 6. Build and CI
- [ ] 6.1 Add `server/pom.xml` as an aggregator (`<packaging>pom</packaging>`) listing both modules
      so a single `mvn verify` from `server/` builds both. Keep `spring-boot-starter-parent` as
      each module's parent. Only move the Spotless, Checkstyle, and JaCoCo setup into a shared
      parent if that's simple; otherwise leave it duplicated.
- [ ] 6.2 Add `.github/workflows/ci.yml`: on push and pull_request, use Temurin JDK 21 with Maven
      caching, then run `mvn -B verify` in `server/`.
- [ ] 6.3 Update the README "Getting started" and "Code style" sections for the single build
      command.

## 7. Website (optional)
- [ ] 7.1 Don't format the PHP now. Add a line to the README saying the PHP follows PSR-12 via
      php-cs-fixer if it's adopted later. Never reformat the bundled Bootstrap/jQuery files.
