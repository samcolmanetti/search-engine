package com.samjsoares.soar.core;

import com.samjsoares.soar.util.UrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.net.URL;

@Component
public class URLServerImpl implements URLServer {

  private JdbcTemplate jdbcTemplate;

  @Autowired
  public URLServerImpl (DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  @Transactional
  public URL getNextUrl() {
    long index = jdbcTemplate.queryForObject("select index from url_seed_index", Long.class);

    String url =
        jdbcTemplate.queryForObject("select url from url_seed where id = ?", new Object[] {index}, String.class);

    jdbcTemplate.update("update url_seed_index set index = ?", new Object[] {index + 1});

    return UrlUtil.getCleanUrl(url);
  }
}
