-- Starter seed URLs for the crawler. Add your own, or replace these.
-- Run after schema.sql. Safe to run more than once: URLs that are already seeds are skipped.
insert into url_seed (url) values
  ('https://en.wikipedia.org/wiki/Search_engine'),
  ('https://en.wikipedia.org/wiki/Web_crawler'),
  ('https://en.wikipedia.org/wiki/Inverted_index'),
  ('https://en.wikipedia.org/wiki/Tf%E2%80%93idf'),
  ('https://en.wikipedia.org/wiki/PageRank')
on conflict (url) do nothing;
