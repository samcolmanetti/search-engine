# Change: Tidy code quality

## Why
The code is formatted now but not yet clean. It still has `System.out` logging, dead code, about
150 Checkstyle warnings in the indexer and about 35 in the searcher (mostly missing Javadoc),
checked-in jars, typos, and plugin setup duplicated across two poms.

## What changes
- Replace `System.out` and `System.err` with SLF4J.
- Remove dead code and commented-out code.
- Fix the Checkstyle warnings, then make warnings fail the build.
- Small modernizations: `final` fields, constructor injection, generics.
- Fix typos and names, and remove junk files.
- Add a parent pom under `server/`, a GitHub Actions workflow, and a PHP style note.

## Impact
- Core logic doesn't change. The only visible difference is that log output goes through SLF4J.
- Depends on `add-working-test-suite` being merged first, so the tests act as a safety net.
- Do the renames as separate commits so the diffs stay readable.
