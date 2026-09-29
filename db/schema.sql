-- Soar database schema (PostgreSQL 12+).
-- Both Java services use these tables: the crawler writes to them and the searcher reads them.
-- Safe to run more than once.

-- One row per crawled page.
create table if not exists doc_info (
  id           bigserial primary key,
  url          text not null unique,
  time_indexed bigint not null,           -- epoch milliseconds
  title        text,
  description  text,
  page_rank    double precision not null default 0  -- read by the searcher, not computed yet
);

-- How many times each (stemmed) term appears on each page.
create table if not exists term_info (
  doc_id    bigint not null references doc_info (id) on delete cascade,
  term      text   not null,
  frequency integer not null check (frequency > 0),
  primary key (doc_id, term)
);

-- Serves the searcher's lookup: rows for one term, most frequent first.
create index if not exists term_info_term_frequency_idx on term_info (term, frequency desc);

-- URLs the crawler starts from. The crawler reads them in id order; gaps in ids are fine.
create table if not exists url_seed (
  id  bigserial primary key,
  url text not null unique
);

-- Exactly one row, holding the id to start from when the crawler next needs a seed.
create table if not exists url_seed_index (
  one   boolean primary key default true check (one),  -- makes a second row impossible
  index bigint  not null
);

insert into url_seed_index (index) values (1) on conflict do nothing;
