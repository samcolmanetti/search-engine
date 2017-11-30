package com.samjsoares.soar.searcher.mapper;

import com.samjsoares.soar.searcher.model.SearchInfo;
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
    searchInfo.setTermFrequency(resultSet.getInt("frequency"));
    searchInfo.setDocId(resultSet.getLong("doc_id"));
    searchInfo.setTitle(resultSet.getString("title"));
    searchInfo.setDescription(resultSet.getString("description"));
    searchInfo.setDocumentTermFrequency(resultSet.getLong("doc_term_freq"));

    return searchInfo;
  }
}
