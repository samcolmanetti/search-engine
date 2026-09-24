package com.samjsoares.soar.mapper;

import com.samjsoares.soar.model.DocumentInfo;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class DocumentInfoMapper implements RowMapper {
  private static final String COLUMN_ID = "id";
  private static final String COLUMN_URL = "url";
  private static final String COLUMN_TIME_INDEXED = "time_indexed";
  private static final String COLUMN_TIME_PAGE_RANK = "page_rank";

  @Override
  public Object mapRow(ResultSet resultSet, int i) throws SQLException {
    DocumentInfo documentInfo = new DocumentInfo();
    documentInfo.setId(resultSet.getLong(COLUMN_ID));
    documentInfo.setUrl(resultSet.getString(COLUMN_URL));
    documentInfo.setTimeIndexed(resultSet.getLong(COLUMN_TIME_INDEXED));
    return documentInfo;
  }
}
