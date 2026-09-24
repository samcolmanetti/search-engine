# Change: Fix known bugs

## Why
Reading the code turned up real bugs. They were left alone on purpose because the earlier changes
don't touch core logic. **Each fix changes behavior**, so each gets its own small PR with a test
that fails before the fix and passes after.

## Prerequisite
`add-working-test-suite` is merged. When a fix lands, update or remove the matching
`// Known bug` comment in the tests.

## Bugs (in priority order)
Line numbers were taken right after PR #1 and may drift.

1. **Search results aren't ranked.** `server/searcher/.../core/TermFrequencyRanker.java`
   - Line 45: `new ArrayList<>(heap)` copies the `PriorityQueue` in internal heap order, not
     sorted order.
   - Line 93: `RelevanceComparator` never returns 0, which breaks the `Comparator` contract.
   - Fix: sort results by relevance, highest first (`Comparator.comparingDouble(...).reversed()`,
     or drain the heap with `poll()`).
2. **The crawler can loop forever.** `server/search-engine/.../core/Crawler.java:107-109`.
   `getNextUrlFromQueue` keeps polling. When the queue runs out, `poll()` returns null,
   `isAllowed(null)` is false, and the loop never ends.
   - Fix: stop when `poll()` returns null, and let `crawl()` return false or pull a new seed URL.
3. **Description extraction is broken.** `server/search-engine/.../core/DocumentProcessor.java`
   - Line 73: `select("p").first()` is null on pages with no `<p>`, so it crashes with a
     NullPointerException. The null check before it is on `select()`, which is never null.
   - Lines 78-79: `StringUtils.indexOf(body, SENTENCE_REGEX)` treats the regex as plain text, so
     it never matches and the whole paragraph is always used. Use `Pattern`/`Matcher`, and pin the
     intended behavior (first one or two sentences) in tests.
   - Line 94: `meta[description]` doesn't match the usual `<meta name="description">`; it should
     be `meta[name=description]`.
   - Line 109: the ellipsis is added even when the text is shorter than `MAX_DESCRIPTION`. Only
     truncate and add it when the text is too long.
   - `getTitle()` crashes when the document is null. The one-argument constructor allows that.
4. **The seed server throws past the end.** `server/search-engine/.../core/URLServerImpl.java:28`.
   `queryForObject` throws `EmptyResultDataAccessException` when the index goes past the last
   seed. Catch it and return null; `Crawler` already handles a null seed URL.
5. **`UrlUtil` isn't thread-safe.** `server/search-engine/.../util/UrlUtil.java:12`. The static
   shared `Matcher` gives wrong results under concurrent use. Keep a static `Pattern` and create a
   matcher per call.
6. **The term's text depends on which word form came first (check intent first).**
   `TermProcessor` (around line 141) groups words under their stem but stores the **first raw
   word** in `TermInfo.term`, so the indexed term for the same stem varies by page. The searcher
   looks up the stemmed query, then the raw one. Storing the stem is probably intended. **Ask the
   owner before changing it**, since it affects data already in the database.

## Not bugs, but decisions to raise with the owner
- `UrlUtil.cleanUpUrl` rewrites every `https://` URL to `http://` (line 68). This was likely done
  for deduplication, but many sites now require https.
- Spring Boot 1.5.8 is end-of-life. Upgrading to 3.x (Java 17+, `javax` → `jakarta`, JUnit 5) is a
  separate, larger project.
