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

## 🚀 Running it locally

Soar has three moving parts that share one PostgreSQL database:

```
seed URLs ──▶ crawler (server/search-engine) ──▶ PostgreSQL ──▶ searcher API :8080 ──▶ website :8000
```

The crawler is a batch job: it reads seed URLs from the `url_seed` table, follows links, and writes
pages and term counts to the database. It stops when it runs out of URLs. Press Ctrl+C to stop it
sooner. The searcher and website only read.

### Option A: Docker (easiest)

Needs only [Docker](https://docs.docker.com/get-docker/).

```bash
docker compose up --build -d          # db, searcher on :8080, website on :8000
docker compose run --rm crawler       # crawl the seed URLs in db/seed.sql
```

Then open <http://localhost:8000> and search for something the crawler has indexed (e.g. `crawler`).

- The schema (`db/schema.sql`) and starter seeds (`db/seed.sql`) load the first time the database
  volume is created. To start over with an empty database, run `docker compose down -v`.
- Follow logs with `docker compose logs -f searcher`.
- Open a SQL shell with `docker compose exec db psql -U soar soar`.
- Port already in use (for example, a local PostgreSQL on 5432)? Pick other host ports with
  `SOAR_DB_PORT`, `SOAR_API_PORT`, or `SOAR_WEB_PORT`, e.g. `SOAR_DB_PORT=5433 docker compose up -d`.

### Option B: Run each piece yourself

**Requirements:** JDK 21 to build (the services run on Java 17+), Maven 3.9+, PostgreSQL 12+, and PHP 8
with the `curl` extension (only needed for the website).

1. **Create the database:**

   ```bash
   createuser soar --pwprompt            # use password "soar", or change it below
   createdb soar --owner soar
   psql -U soar -h localhost soar -f db/schema.sql -f db/seed.sql
   ```

2. **Point the services at it.** Spring Boot reads these environment variables. You can also put the
   same settings (`spring.datasource.url=…`) in a git-ignored `src/main/resources/application.properties`.

   ```bash
   export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/soar
   export SPRING_DATASOURCE_USERNAME=soar
   export SPRING_DATASOURCE_PASSWORD=soar
   ```

3. **Crawl** (in one terminal):

   ```bash
   cd server/search-engine && mvn spring-boot:run
   ```

4. **Serve the search API** (in another terminal):

   ```bash
   cd server/searcher/seacher && mvn spring-boot:run
   curl 'http://localhost:8080/api/search?query=crawler'
   ```

5. **Serve the website** (in a third terminal):

   ```bash
   cd website/public && php -S localhost:8000
   ```

   The site calls the API at `http://localhost:8080` unless you set `SOAR_API_URL`.

### Choosing what to crawl

Add rows to `url_seed`. The crawler reads them in `id` order, starting from the id in
`url_seed_index`. Seed URLs are unique, and `db/seed.sql` is safe to run again.

```sql
insert into url_seed (url) values ('https://example.com/') on conflict (url) do nothing;
update url_seed_index set index = (select min(id) from url_seed);   -- re-read seeds from the start
```

The crawler respects `robots.txt` and waits at least one second between requests to the same host.

## 🧪 Testing and debugging

```bash
cd server && mvn verify       # unit tests + style checks for both services (no database needed)
scripts/smoke-test.sh         # end to end in Docker: crawl a test site, then search it
```

The smoke test builds the Docker images, crawls the small site in `scripts/smoke-site/`, and checks
the search API and website. It uses its own ports and Compose project, so it won't disturb a stack
you already have running. Set `KEEP=1` to leave its stack up afterwards. CI runs both commands on
every pull request.

To run one service's unit tests: `cd server/search-engine && mvn test` (or `server/searcher/seacher`).

- **Debug in an IDE:** open either Maven project, set the three `SPRING_DATASOURCE_*` variables in the
  run configuration, and debug `SoarApplication` (crawler) or `SearcherApplication` (searcher).
- **Debug from the command line:** add
  `-Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005"`
  to `mvn spring-boot:run`, then attach your IDE's remote debugger to port 5005.
- **More logging:** `export LOGGING_LEVEL_COM_SAMJSOARES=DEBUG`.
- **Check what was indexed:** `select url, title from doc_info;` and
  `select term, frequency from term_info where doc_id = 1 order by frequency desc;`.
- **Empty search results** usually mean the crawler hasn't indexed a page with that term yet. Queries
  are stemmed, so `crawling` matches `crawl`.
- **`Failed to configure a DataSource`** means the `SPRING_DATASOURCE_*` variables aren't set in that shell.

## ☁️ Deploying for free

Soar doesn't need a dedicated server. The cheapest setup has three parts:

| Piece | Free option | Notes |
| --- | --- | --- |
| PostgreSQL | [Neon](https://neon.tech) or [Supabase](https://supabase.com) free tier | Run `db/schema.sql` and `db/seed.sql` in their SQL editor. Use the JDBC URL they give you, with `?sslmode=require`. |
| Searcher + website | [Render](https://render.com) free web services (Docker) | Create one service from `server/searcher/seacher/Dockerfile` and one from `website/Dockerfile`. Set `SPRING_DATASOURCE_*` on the searcher and `SOAR_API_URL` (the searcher's public URL) on the website. Free services sleep when idle, so the first request after a while is slow. |
| Crawler | Your own machine | Run it locally, or run `docker compose run --rm crawler`, with `SPRING_DATASOURCE_*` pointing at the cloud database. It's a batch job, so it doesn't need to stay online. |

**Alternative:** An always-free VM (e.g. [Oracle Cloud Always Free](https://www.oracle.com/cloud/free/))
can run the whole `docker compose` stack as it is, so it replaces the old server most directly.
Free-tier limits change often, so check each provider's current terms.

## 🧹 Code style

Java code follows the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).

| Command | What it does |
| --- | --- |
| `mvn spotless:apply` | ✍️ Formats code with google-java-format |
| `mvn checkstyle:check` | 🔍 Lints with Google's Checkstyle rules |
| `mvn verify` | ✅ Builds, tests, and fails on unformatted code or Checkstyle warnings |

Run these in `server/search-engine` or `server/searcher/seacher`. Run `mvn verify` in `server/`
to build and check both at once. CI runs that same command on every push and pull request.

The PHP website isn't auto-formatted. If a formatter is added, use PSR-12 with php-cs-fixer, and
leave the bundled Bootstrap and jQuery files alone.

To hide formatting-only commits from `git blame`:

```bash
git config blame.ignoreRevsFile .git-blame-ignore-revs
```

## 🧭 Original vision

The original proposal was a search engine for students and researchers. Users would choose the
ranking method (boolean, TF, TF-IDF, or PageRank) and see how results were computed, including
term frequencies, processing time, and page counts. Today Soar ranks by term frequency only; the
other ranking methods and the explanation views are ideas for future work.
