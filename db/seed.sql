-- Starter seed URLs for the crawler. Add your own, or replace these.
-- Run after schema.sql. Running it twice adds the URLs twice, which is harmless but wasteful.
insert into url_seed (url) values
  ('https://en.wikipedia.org/wiki/Search_engine'),
  ('https://en.wikipedia.org/wiki/Web_crawler'),
  ('https://en.wikipedia.org/wiki/Inverted_index'),
  ('https://en.wikipedia.org/wiki/Tf%E2%80%93idf'),
  ('https://en.wikipedia.org/wiki/PageRank');
