# Tasks

Paths: `SE` = `server/search-engine`, `SR` = `server/searcher/seacher`.
Test packages mirror main: `com.samjsoares.soar...`

## 1. Make existing tests run
- [ ] 1.1 Rename `UrlUtilTester`, `RobotsHandlerTester`, `ScaledStemmerTester`, and
      `DocumentProcessorTester` in `SE` to `*Test`. Check that `mvn test` reports a nonzero test count.
- [ ] 1.2 `ScaledStemmerTest`: drop the Spring runner and `TestConfig`, and use `new ScaledStemmer()`.
- [ ] 1.3 `RobotsHandlerTest`: stop using the network. `RobotsHandler.add` opens a URL stream, so
      either test the robots.txt rule logic with `RobotsTxt.read(InputStream)` on fixture files,
      or pre-seed the handler via a test subclass. Cover these cases: allowed path, disallowed
      path, no robots.txt (allowed), and a null URL (not allowed).
- [ ] 1.4 `DocumentProcessorTest`: build documents with `Jsoup.parse(html, baseUri)` from
      fixtures and use real assertions.
- [ ] 1.5 Delete `TestConfig` once no test uses it.
- [ ] 1.6 `SR`: remove `SearcherApplicationTests.contextLoads` (reason in the proposal) and
      replace it with the unit tests in section 3.

## 2. New unit tests in `server/search-engine`
- [ ] 2.1 `LRUCacheMap`: evicts the least recently *accessed* entry, not just the oldest inserted,
      and respects the default limit of 128.
- [ ] 2.2 `LRUCacheSet`: `add` returns true for new items and false for duplicates. It also
      covers `contains`, eviction, `size`, and `isEmpty`.
- [ ] 2.3 `NodeIterable`: pre-order depth-first traversal order, and `remove` throws
      `UnsupportedOperationException`.
- [ ] 2.4 `StopWordsUtil`: common stop words are detected, real words are not, and the check is
      case-sensitive (callers lowercase first).
- [ ] 2.5 `CollectionsUtil.emptyIfNull`.
- [ ] 2.6 `JdbcUtil.getInsertedId` with Spring's `GeneratedKeyHolder`: a single key, multiple keys
      that include the id column, and multiple keys without it (returns -1).
- [ ] 2.7 `UrlUtil`: more cases for `getCleanUrl` (strips fragment and query, forces http,
      adds a missing scheme, encodes spaces), `getUrlKey`, `getUrlString`, `getRobotsTxtURL`,
      and invalid input returning null.
- [ ] 2.8 `TermProcessor`: counts words, ignores stop words and 1-character tokens, strips
      punctuation, lowercases, and merges words that share a stem under one key. Use `Jsoup.parse`
      input and `getTermInfos()`.
- [ ] 2.9 `InMemoryIndexer`: `indexPage`, `get`, `getCounts`, and `shouldIndex` (true for a new
      URL, false right after indexing, false for an invalid URL). A null document is a no-op.
- [ ] 2.10 `DatabaseIndexer` with fake `DocumentInfoDao` and `TermInfoDao` implementations:
      upserts the document, then its terms, and skips the terms when the returned id is 0 or less.
- [ ] 2.11 `Crawler` with fakes: a test subclass of `Fetcher` that overrides
      `fetchDocument`/`fetch`, a subclass of `RobotsHandler` that overrides `isAllowed`, a lambda
      for `URLServer`, and a recording `Indexer`. Cover: an empty queue with no seed returns false;
      a page is indexed and its links queued; an already-indexed URL is not re-indexed; duplicate
      links are not queued twice. Avoid the infinite-loop bug (fix-known-bugs #2).
- [ ] 2.12 Mappers (`DocumentInfoMapper`, `TermInfoMapper`): only if a `ResultSet` stub is cheap
      (a `java.lang.reflect.Proxy` works without Mockito). Otherwise skip.

## 3. New unit tests in `server/searcher/seacher`
- [ ] 3.1 `TermFrequencyRanker`: an empty ranker returns an empty list; relevance is computed as
      `sum(tf / df) * matchCount`; entries for the same doc id are grouped into one result.
      **Don't assert result order** (fix-known-bugs #1). Compare as sets or sort in the test.
- [ ] 3.2 `SearcherImpl` with a fake `SearchInfoDao`: searches for the stemmed term first, falls
      back to the raw term when that's empty, returns an empty list for a null query, and splits
      on spaces and `+` (`Regex.SPACE_OR_PLUS`).
- [ ] 3.3 `SearchController`: an empty query returns `{}`. Set the private `searcher` field via
      `ReflectionTestUtils`, or wait for the constructor-injection task in `tidy-code-quality`.
- [ ] 3.4 `SearchInfo` and `SearchResult`: skip plain getters and setters.

## 4. Coverage
- [ ] 4.1 Add `jacoco-maven-plugin` (latest release) to both poms with `prepare-agent` and
      `report` bound to `verify`. Don't set a threshold.
- [ ] 4.2 Confirm `mvn verify` passes in both projects. Note the coverage numbers in the PR body.
