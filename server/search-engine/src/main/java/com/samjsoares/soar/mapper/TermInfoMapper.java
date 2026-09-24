package com.samjsoares.soar.mapper;

import com.samjsoares.soar.model.TermInfo;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class TermInfoMapper implements RowMapper {

  private static final String COLUMN_DOC_ID = "doc_id";
  private static final String COLUMN_TERM = "term";
  private static final String COLUMN_COUNT = "frequency";

  @Override
  public Object mapRow(ResultSet resultSet, int i) throws SQLException {
    TermInfo termInfo = new TermInfo();
    termInfo.setDocId(resultSet.getLong(COLUMN_DOC_ID));
    termInfo.setCount(resultSet.getInt(COLUMN_COUNT));
    termInfo.setTerm(resultSet.getString(COLUMN_TERM));
    return termInfo;
  }
}
