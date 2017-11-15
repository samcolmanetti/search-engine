package com.samjsoares.soar.dao;

import com.samjsoares.soar.constant.DocumentInfoSql;
import com.samjsoares.soar.mapper.DocumentInfoMapper;
import com.samjsoares.soar.model.DocumentInfo;
import com.samjsoares.soar.util.JdbcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component
public class DocumentInfoDaoImpl implements DocumentInfoDao {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private JdbcTemplate jdbcTemplate;

  @Autowired
  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public DocumentInfoDaoImpl() {}

  @Override
  public long upsert (final String url, final long timeIndexed, final String title, final String description) {
    KeyHolder holder = new GeneratedKeyHolder();

    jdbcTemplate.update(new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DocumentInfoSql.UPSERT, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, url);
        ps.setLong(2, timeIndexed);
        ps.setString(3, title);
        ps.setString(4, description);
        return ps;
      }
    }, holder);

    long id = JdbcUtil.getInsertedId(holder, "id");
    if (id <= 0) {
      logger.error("Failed to insert Document Info for url: %s", url);
    }

    return id;
  }

  @Override
  public long upsert(DocumentInfo documentInfo) {
    return upsert(
        documentInfo.getUrl(),
        documentInfo.getTimeIndexed(),
        documentInfo.getTitle(),
        documentInfo.getDescription());
  }

  @Override
  public int[] upsert(List<DocumentInfo> documentInfos) {
    // TODO: implement with jdbcTemplate ~ low priority
    Object[][] params = new Object[documentInfos.size()][];
    for (int i = 0; i < documentInfos.size(); i++) {
      DocumentInfo documentInfo = documentInfos.get(i);
      params[i] = new Object[] { documentInfo.getUrl(), documentInfo.getTimeIndexed() };
    }

    int[] insertedRows = null;

    return insertedRows;
  }

  @Override
  public DocumentInfo get (long id) {
    return get(id, null);
  }

  @Override
  public DocumentInfo get (String url) {
    return get(null, url);
  }

  private DocumentInfo get (Long id, String url) {
    Object[] params;
    String sql;

    if (id != null) {
      params = new Object[]{id};
      sql = DocumentInfoSql.SELECT_WITH_ID;
    } else {
      params = new Object[]{url};
      sql = DocumentInfoSql.SELECT_WITH_URL;
    }

    List<DocumentInfo> documentInfos = (List<DocumentInfo>) jdbcTemplate.query(
        sql,
        params,
        new DocumentInfoMapper());

    if (documentInfos.size() != 1) {
      return null;
    }

    return documentInfos.get(0);
  }

  @Override
  public long getId (String url) {
    Object[] params = new Object[] {url};

    try {
      return jdbcTemplate.queryForObject(DocumentInfoSql.SELECT_ID, params, Long.class);
    } catch (Exception e) {
      return -1;
    }
  }

  @Override
  public long getTimeIndexed (String url) {
    Object[] params = new Object[] {url};

    try {
      return jdbcTemplate.queryForObject(DocumentInfoSql.SELECT_TIME_INDEXED, params, Long.class);
    } catch(Exception e) {
      return -1;
    }
  }

  @Override
  public List<DocumentInfo> getAll() {
    return (List<DocumentInfo>) jdbcTemplate.query(
        DocumentInfoSql.SELECT_ALL,
        new Object[] {},
        new DocumentInfoMapper());
  }
}
