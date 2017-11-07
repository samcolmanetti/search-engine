package com.samjsoares.soar.searcher.searcher.mapper;

import com.samjsoares.soar.searcher.searcher.model.SearchInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchInfoMapper implements RowMapper{

  @Override
  public Object mapRow(ResultSet resultSet, int i) throws SQLException {
    SearchInfo searchInfo = new SearchInfo();
    searchInfo.setUrl(resultSet.getString("url"));
    searchInfo.setPageRank(resultSet.getDouble("page_rank"));
    searchInfo.setTerm(resultSet.getString("term"));
    searchInfo.setTermCount(resultSet.getInt("count"));
    return searchInfo;
  }
}
