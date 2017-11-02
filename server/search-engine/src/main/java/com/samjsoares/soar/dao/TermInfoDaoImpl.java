package com.samjsoares.soar.dao;

import com.samjsoares.soar.constant.TermInfoSql;
import com.samjsoares.soar.mapper.TermInfoMapper;
import com.samjsoares.soar.model.TermInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Component
public class TermInfoDaoImpl implements TermInfoDao {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private JdbcTemplate jdbcTemplate;

  @Autowired
  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public long upsert(TermInfo termInfo) {
    Object[] params = new Object[] { termInfo.getDocId(), termInfo.getTerm(), termInfo.getCount() };
    return jdbcTemplate.update(TermInfoSql.UPSERT, params);
  }

  @Override
  @Transactional
  public int upsert(long docId, List<TermInfo> termInfos) {
    jdbcTemplate.update(TermInfoSql.DELETE_ALL_FROM_DOC, new Object[] { docId });

    int affectedRows = 0;
    for (int i = 0; i < termInfos.size(); i++) {
      TermInfo termInfo = termInfos.get(i);
      affectedRows += upsert(termInfo);
    }

    return affectedRows;
  }

  @Override
  public TermInfo get(long documentId, String term) {
    Object[] params = new Object[] { documentId, term };
    List<TermInfo> termInfos = jdbcTemplate.query(TermInfoSql.SELECT, params, new TermInfoMapper());

    if (termInfos.size() != 1) {
      return null;
    }
    return termInfos.get(0);
  }

  @Override
  public List<TermInfo> getAll(long documentId) {
    Object[] params = new Object[] { documentId };
    List<TermInfo> termInfos = jdbcTemplate.query(TermInfoSql.SELECT_FROM_DOC, params, new TermInfoMapper());
    return termInfos;
  }

  @Override
  public List<TermInfo> getAll() {
    Object[] params = new Object[] {};
    List<TermInfo> termInfos = jdbcTemplate.query(TermInfoSql.SELECT_ALL, params, new TermInfoMapper());
    return termInfos;
  }
}
