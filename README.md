# 🦅 Soar — an educational search engine

Soar is a small, open-source search engine built to show students how search works under the
hood: **crawl → index → search → rank**.

> 🎓 Started as a college project. The goal was to keep it simple enough to read, run, and modify.

## ✨ Features

- 🕷️ **Crawler:** follows links from a seed list, respects `robots.txt`, and rate-limits requests per host
- 📚 **Indexer:** extracts titles, descriptions, and term frequencies, and drops stop words and stems terms
- 🔎 **Searcher:** a REST API that ranks matching pages by term frequency
- 🌐 **Website:** a PHP front end with a "Learn" page that explains each component

## 🗂️ Project layout

| Path | What it is | Stack |
| --- | --- | --- |
| `server/search-engine` | Crawler and indexer | Java, Spring Boot, jsoup, OpenNLP |
| `server/searcher/seacher` | Search API (`GET /api/search?query=…`) | Java, Spring Boot |
| `website` | Search UI | PHP, Bootstrap |

Both Java services store data in **PostgreSQL**. Database settings go in each service's
`application.properties` file, which is git-ignored.

## 🚀 Getting started

**Requirements:** JDK 21 to build (the services run on Java 17+), Maven 3.9+, and PostgreSQL

```bash
cd server/search-engine        # or server/searcher/seacher
mvn spring-boot:run            # run the service
mvn test                       # run the tests
```

## 🧹 Code style

Java code follows the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).

| Command | What it does |
| --- | --- |
| `mvn spotless:apply` | ✍️ Formats code with google-java-format |
| `mvn checkstyle:check` | 🔍 Lints with Google's Checkstyle rules |
| `mvn verify` | ✅ Builds, tests, and fails on unformatted code |

To hide formatting-only commits from `git blame`:

```bash
git config blame.ignoreRevsFile .git-blame-ignore-revs
```

## 🧭 Original vision

The original proposal was a search engine for students and researchers. Users would choose the
ranking method (boolean, TF, TF-IDF, or PageRank) and see how results were computed, including
term frequencies, processing time, and page counts. Today Soar ranks by term frequency only; the
other ranking methods and the explanation views are ideas for future work.
