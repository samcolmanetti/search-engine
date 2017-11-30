package com.samjsoares.soar.searcher.dao;

import com.samjsoares.soar.searcher.constant.SearcherSQL;
import com.samjsoares.soar.searcher.mapper.SearchInfoMapper;
import com.samjsoares.soar.searcher.model.SearchInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class SearchInfoDaoImpl implements SearchInfoDao{

  private JdbcTemplate jdbcTemplate;

  @Autowired
  public SearchInfoDaoImpl(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public List<SearchInfo> getSearchInfo(String term) {
    Object[] params = new Object[] { term };
    return jdbcTemplate.query(SearcherSQL.SELECT_BY_TERM, params, new SearchInfoMapper());
  }
}
