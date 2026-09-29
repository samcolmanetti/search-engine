package com.samjsoares.soar.core;

import com.samjsoares.soar.util.UrlUtil;
import java.net.URL;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serves seed URLs in order from the {@code url_seed} table, storing the position of the next seed
 * in {@code url_seed_index}.
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

    String url;
    try {
      url =
          jdbcTemplate.queryForObject("select url from url_seed where id = ?", String.class, index);
    } catch (EmptyResultDataAccessException e) {
      // Past the last seed. Leave the index where it is so later calls also return null.
      return null;
    }

    jdbcTemplate.update("update url_seed_index set index = ?", index + 1);

    return UrlUtil.getCleanUrl(url);
  }
}
