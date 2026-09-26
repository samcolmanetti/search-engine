package com.samjsoares.soar.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class URLServerImplTest {

  /** Serves the seed table from a list, with ids starting at 1, instead of from Postgres. */
  private static class FakeJdbcTemplate extends JdbcTemplate {
    final List<String> seeds = new ArrayList<>();
    long index = 1;

    @Override
    public <T> T queryForObject(String sql, Class<T> requiredType) {
      return requiredType.cast(index);
    }

    @Override
    public <T> T queryForObject(String sql, Class<T> requiredType, Object... args) {
      int id = ((Long) args[0]).intValue();
      if (id < 1 || id > seeds.size()) {
        throw new EmptyResultDataAccessException(1);
      }
      return requiredType.cast(seeds.get(id - 1));
    }

    @Override
    public int update(String sql, Object... args) {
      index = (Long) args[0];
      return 1;
    }
  }

  private final FakeJdbcTemplate jdbcTemplate = new FakeJdbcTemplate();
  private final URLServer urlServer = new URLServerImpl(jdbcTemplate);

  @Test
  public void testServesSeedsInOrder() {
    jdbcTemplate.seeds.add("http://example.com/one");
    jdbcTemplate.seeds.add("http://example.com/two");

    assertThat(urlServer.getNextUrl()).hasToString("http://example.com/one");
    assertThat(urlServer.getNextUrl()).hasToString("http://example.com/two");
    assertThat(jdbcTemplate.index).isEqualTo(3);
  }

  @Test
  public void testReturnsNullPastTheLastSeed() {
    jdbcTemplate.seeds.add("http://example.com/one");

    assertThat(urlServer.getNextUrl()).isNotNull();
    assertThat(urlServer.getNextUrl()).isNull();
    assertThat(urlServer.getNextUrl()).isNull();
    // The index stays put instead of running further past the end.
    assertThat(jdbcTemplate.index).isEqualTo(2);
  }
}
