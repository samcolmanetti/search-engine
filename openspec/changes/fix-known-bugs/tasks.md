# Tasks

For each bug: a failing test first, the minimal fix, and then `mvn verify` in both projects.
Bugs 1–5 were fixed together on branch `fix/known-bugs`, with one commit per bug.

- [x] 1 `fix/ranker-ordering`: TermFrequencyRanker returns results sorted by relevance, highest first
- [x] 2 `fix/crawler-empty-queue`: Crawler stops instead of looping when the queue runs out
- [x] 3 `fix/document-description`: DocumentProcessor handles no `<p>`, sentence regex, meta name, ellipsis, null title
- [x] 4 `fix/url-seed-exhausted`: URLServerImpl returns null past the last seed
- [x] 5 `fix/urlutil-thread-safety`: UrlUtil creates a matcher per call
- [ ] 6 Ask the owner about storing the stem in `TermInfo.term`; only implement if approved
- [ ] 7 Ask the owner about the https→http rewrite and the Spring Boot upgrade; don't implement here
