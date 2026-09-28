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
  frequency integer not null,
  primary key (doc_id, term)
);

create index if not exists term_info_term_idx on term_info (term);

-- URLs the crawler starts from. The crawler reads them in id order.
create table if not exists url_seed (
  id  bigserial primary key,
  url text not null
);

-- A single row holding the id of the next seed to crawl.
create table if not exists url_seed_index (
  index bigint not null
);

insert into url_seed_index (index)
select 1 where not exists (select 1 from url_seed_index);
