package com.samjsoares.soar.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class UrlServerImplTest {

  /** Serves the seed table from a map of id to URL instead of from Postgres. */
  private static class FakeJdbcTemplate extends JdbcTemplate {
    final TreeMap<Long, String> seeds = new TreeMap<>();
    long index = 1;

    void addSeed(String url) {
      seeds.put(seeds.isEmpty() ? 1 : seeds.lastKey() + 1, url);
    }

    @Override
    public <T> T queryForObject(String sql, Class<T> requiredType) {
      return requiredType.cast(index);
    }

    // Stands in for "select id, url from url_seed where id >= ? order by id limit 1".
    @Override
    public List<Map<String, Object>> queryForList(String sql, Object... args) {
      Map.Entry<Long, String> seed = seeds.ceilingEntry((Long) args[0]);
      if (seed == null) {
        return Collections.emptyList();
      }
      Map<String, Object> row = new HashMap<>();
      row.put("id", seed.getKey());
      row.put("url", seed.getValue());
      return Collections.singletonList(row);
    }

    @Override
    public int update(String sql, Object... args) {
      index = (Long) args[0];
      return 1;
    }
  }

  private final FakeJdbcTemplate jdbcTemplate = new FakeJdbcTemplate();
  private final UrlServer urlServer = new UrlServerImpl(jdbcTemplate);

  @Test
  public void testServesSeedsInOrder() {
    jdbcTemplate.addSeed("http://example.com/one");
    jdbcTemplate.addSeed("http://example.com/two");

    assertThat(urlServer.getNextUrl()).hasToString("http://example.com/one");
    assertThat(urlServer.getNextUrl()).hasToString("http://example.com/two");
    assertThat(jdbcTemplate.index).isEqualTo(3);
  }

  @Test
  public void testReturnsNullPastTheLastSeed() {
    jdbcTemplate.addSeed("http://example.com/one");

    assertThat(urlServer.getNextUrl()).isNotNull();
    assertThat(urlServer.getNextUrl()).isNull();
    assertThat(urlServer.getNextUrl()).isNull();
    // The index stays put instead of running further past the end.
    assertThat(jdbcTemplate.index).isEqualTo(2);
  }

  @Test
  public void testSkipsGapsInSeedIds() {
    // Ids 2 and 3 were deleted.
    jdbcTemplate.seeds.put(1L, "http://example.com/one");
    jdbcTemplate.seeds.put(4L, "http://example.com/four");

    assertThat(urlServer.getNextUrl()).hasToString("http://example.com/one");
    assertThat(urlServer.getNextUrl()).hasToString("http://example.com/four");
    assertThat(jdbcTemplate.index).isEqualTo(5);
    assertThat(urlServer.getNextUrl()).isNull();
  }
}
