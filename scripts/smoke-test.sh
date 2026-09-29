#!/usr/bin/env bash
# End-to-end smoke test: builds and starts the Docker stack, crawls a small test site
# (scripts/smoke-site), and checks the search API and the website.
#
# It uses its own Compose project and host ports, so it doesn't touch a stack you already have
# running. Needs Docker with Compose v2 and curl.
#
#   scripts/smoke-test.sh           # build, test, and clean up
#   KEEP=1 scripts/smoke-test.sh    # leave the stack running afterwards to poke at it
#   NO_BUILD=1 scripts/smoke-test.sh  # reuse images from a previous run
set -euo pipefail
cd "$(dirname "$0")/.."

export COMPOSE_PROJECT_NAME=soar-smoke
export SOAR_DB_PORT="${SOAR_DB_PORT:-15432}"
export SOAR_API_PORT="${SOAR_API_PORT:-18080}"
export SOAR_WEB_PORT="${SOAR_WEB_PORT:-18000}"
api="http://localhost:$SOAR_API_PORT/api/search?query="
web="http://localhost:$SOAR_WEB_PORT/search.php?q="
build_flag="--build"
if [[ "${NO_BUILD:-}" == 1 ]]; then build_flag="--no-build"; fi

compose() { docker compose -f compose.yaml -f compose.smoke.yaml "$@"; }
sql() { compose exec -T db psql -U soar -d soar -v ON_ERROR_STOP=1 -qtA "$@"; }

cleanup() {
  if [[ "${KEEP:-}" == 1 ]]; then
    echo "Stack left running (project $COMPOSE_PROJECT_NAME). Stop it with:"
    echo "  COMPOSE_PROJECT_NAME=$COMPOSE_PROJECT_NAME docker compose -f compose.yaml -f compose.smoke.yaml down -v"
  else
    compose --profile crawl down -v --remove-orphans >/dev/null 2>&1 || true
  fi
}
trap cleanup EXIT

fail() {
  echo "FAIL: $*" >&2
  compose --profile crawl logs --tail=40 >&2 || true
  exit 1
}

# Fails unless $2 (a string) contains $3; $1 names the check.
expect_contains() { [[ "$2" == *"$3"* ]] || fail "$1: expected to find '$3' in: $2"; }
expect_missing() { [[ "$2" != *"$3"* ]] || fail "$1: did not expect '$3' in: $2"; }

echo "==> Starting the database, search API, website, and test site"
compose up -d "$build_flag" --wait db testsite website

echo "==> Seeding the crawler with the test site only"
sql -c "truncate term_info, doc_info restart identity; delete from url_seed;" \
  -c "insert into url_seed (url) values ('http://testsite/');" \
  -c "update url_seed_index set index = (select min(id) from url_seed);"

echo "==> Crawling (the crawler stops when it runs out of URLs)"
if [[ "$build_flag" == "--build" ]]; then compose --profile crawl build crawler; fi
timeout 300 docker compose -f compose.yaml -f compose.smoke.yaml --profile crawl \
  run --rm crawler >/dev/null || fail "the crawler failed or didn't stop within 5 minutes"

pages=$(sql -c "select string_agg(url, ' ' order by url) from doc_info")
echo "    indexed: $pages"
expect_contains "home page indexed" "$pages" "http://testsite/"
expect_contains "linked page indexed" "$pages" "http://testsite/crawling.html"
expect_contains "linked page indexed" "$pages" "http://testsite/ranking.html"
expect_missing "robots.txt respected" "$pages" "private"

echo "==> Waiting for the search API"
for _ in $(seq 1 60); do
  curl -fsS "$api" >/dev/null 2>&1 && break
  sleep 1
done

echo "==> Checking search results"
expect_contains "empty query" "$(curl -fsS "$api")" "{}"
expect_contains "no matches" "$(curl -fsS "${api}zebrafish")" "[]"
results=$(curl -fsS "${api}crawling")
first=$(grep -o '"url":"[^"]*"' <<<"$results" | head -n 1)
expect_contains "best match first" "$first" "http://testsite/crawling.html"
expect_contains "stemmed match" "$(curl -fsS "${api}crawled")" "crawling.html"

echo "==> Checking the website"
page=$(curl -fsS "${web}crawling")
expect_contains "website shows results" "$page" "Crawling Explained"
expect_contains "website escapes the query" "$(curl -fsS "${web}%3Cb%3Ehi%3C%2Fb%3E")" "&lt;b&gt;hi&lt;/b&gt;"

echo "PASS: crawled, indexed, and searched the test site end to end"
