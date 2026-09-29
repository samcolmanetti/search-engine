package com.samjsoares.soar.searcher.dao;

import com.samjsoares.soar.searcher.constant.SearcherSql;
import com.samjsoares.soar.searcher.mapper.SearchInfoMapper;
import com.samjsoares.soar.searcher.model.SearchInfo;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * {@link SearchInfoDao} backed by JDBC; returns at most 150 documents per term, ordered by the
 * term's frequency in each document.
 */
@Component
public class SearchInfoDaoImpl implements SearchInfoDao {

  private final JdbcTemplate jdbcTemplate;

  /**
   * Creates a DAO that queries the given database.
   *
   * @param dataSource the database holding the {@code doc_info} and {@code term_info} tables
   */
  @Autowired
  public SearchInfoDaoImpl(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public List<SearchInfo> getSearchInfo(String term) {
    Object[] params = new Object[] {term, term};
    return jdbcTemplate.query(SearcherSql.SELECT_BY_TERM, new SearchInfoMapper(), params);
  }
}
