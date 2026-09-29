package com.samjsoares.soar.core;

import com.samjsoares.soar.util.UrlUtil;
import java.net.URL;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serves seed URLs in id order from the {@code url_seed} table, storing the position of the next
 * seed in {@code url_seed_index}.
 */
@Component
public class UrlServerImpl implements UrlServer {

  private final JdbcTemplate jdbcTemplate;

  /** Creates a server that reads seeds from {@code dataSource}. */
  @Autowired
  public UrlServerImpl(DataSource dataSource) {
    this(new JdbcTemplate(dataSource));
  }

  UrlServerImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  @Transactional
  public URL getNextUrl() {
    long index = jdbcTemplate.queryForObject("select index from url_seed_index", Long.class);

    // The next seed at or after the index, so deleted seeds leave no gap that ends the crawl.
    List<Map<String, Object>> rows =
        jdbcTemplate.queryForList(
            "select id, url from url_seed where id >= ? order by id limit 1", index);
    if (rows.isEmpty()) {
      // Past the last seed. Leave the index where it is so later calls also return null.
      return null;
    }

    long id = ((Number) rows.get(0).get("id")).longValue();
    jdbcTemplate.update("update url_seed_index set index = ?", id + 1);

    return UrlUtil.getCleanUrl((String) rows.get(0).get("url"));
  }
}
