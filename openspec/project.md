# Project: Soar search engine (tidy-up)

Soar is an old college project: a crawler/indexer, a search API, and a PHP website. This folder
holds the plan for tidying it up, written as OpenSpec-style change proposals.

## Start here

Do the changes in this order. Each one is its own branch and PR.

| # | Change | Changes behavior? |
| --- | --- | --- |
| 1 | [`add-working-test-suite`](changes/add-working-test-suite/proposal.md) | No |
| 2 | [`tidy-code-quality`](changes/tidy-code-quality/proposal.md) | No (only log output) |
| 3 | [`fix-known-bugs`](changes/fix-known-bugs/proposal.md) | **Yes**: one small PR per bug |

Already done (PR #1, merged): Spotless (google-java-format) and Checkstyle (`google_checks.xml`)
run in both Maven projects, the code is reformatted, and the README is rewritten.

## Layout

| Path | What | Notes |
| --- | --- | --- |
| `server/search-engine` | Crawler + indexer | Spring Boot 4.1, jsoup, OpenNLP, galimatias, panforge robots |
| `server/searcher/seacher` | Search REST API (`GET /api/search?query=`) | Spring Boot 4.1, Gson. The folder is misspelled. |
| `website` | PHP front end | Out of scope for Java style |

Both projects are standalone Maven builds with no shared parent. Base package: `com.samjsoares.soar`.
Both depend on PostgreSQL, configured through git-ignored `application.properties` files.

## Tooling

- JDK 21 for the build, Maven 3.9. Code targets Java 17 (`java.version` in each pom).
- `mvn spotless:apply` formats. `mvn verify` runs tests, the Spotless check, and Checkstyle
  (warnings only).
- Test libraries come from `spring-boot-starter-test`: JUnit Jupiter, AssertJ, and Mockito.
  The existing tests use hand-written fakes rather than Mockito; follow that style.

## Conventions (must follow)

- **No AI-assistant attribution anywhere.** No AI tool names, co-author trailers, or session links
  in branch names, commit messages, PR titles or bodies, or files.
- **Git identity:** `Sam Colmanetti <samcolmanetti@gmail.com>`.
- **Merge PRs with a merge commit**, never squash or rebase. `.git-blame-ignore-revs` depends on
  commit hashes staying the same.
- Code follows Google Java Style. Run `mvn spotless:apply` before committing, and make sure
  `mvn verify` passes in both projects before pushing.
- Changes 1 and 2 must not change core logic. Change 3 is where behavior changes.
