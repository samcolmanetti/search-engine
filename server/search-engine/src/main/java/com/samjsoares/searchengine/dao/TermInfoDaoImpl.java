package com.samjsoares.searchengine.dao;

import com.samjsoares.searchengine.model.TermInfo;
import com.samjsoares.searchengine.util.DatabaseUtil;
import org.knowm.yank.Yank;

import java.util.List;

public class TermInfoDaoImpl implements TermInfoDao {
  private final static String UPSERT_KEY = "TERM_INFO_UPSERT";
  private final static String SELECT_KEY = "TERM_INFO_SELECT";
  private final static String SELECT_FROM_DOC_KEY = "TERM_INFO_SELECT_FROM_DOC";

  @Override
  public long upsert(TermInfo termInfo) {
    DatabaseUtil.setUpDatabase();

    Object[] params = new Object[] { termInfo.getDocId(), termInfo.getTerm(), termInfo.getCount() };
    long affectedRows = Yank.executeSQLKey(UPSERT_KEY, params);

    DatabaseUtil.releaseConnection();
    return affectedRows;
  }

  @Override
  public int[] upsert(List<TermInfo> termInfos) {
    DatabaseUtil.setUpDatabase();

    Object[][] params = new Object[termInfos.size()][];
    for (int i = 0; i < termInfos.size(); i++) {
      TermInfo termInfo = termInfos.get(i);
      params[i] = new Object[] { termInfo.getDocId(), termInfo.getTerm(), termInfo.getCount() };
    }

    int[] insertedRows = Yank.executeBatchSQLKey(UPSERT_KEY, params);

    DatabaseUtil.releaseConnection();
    return insertedRows;
  }

  @Override
  public TermInfo get(long documentId, String term) {
    DatabaseUtil.setUpDatabase();

    Object[] params = new Object[] { documentId, term };
    TermInfo termInfo = Yank.queryBeanSQLKey(SELECT_KEY, TermInfo.class, params);

    DatabaseUtil.releaseConnection();
    return termInfo;
  }

  @Override
  public List<TermInfo> getAll(long documentId) {
    DatabaseUtil.setUpDatabase();

    Object[] params = new Object[] { documentId };
    List<TermInfo> termInfos = Yank.queryBeanListSQLKey(SELECT_FROM_DOC_KEY, TermInfo.class, params);

    DatabaseUtil.releaseConnection();
    return termInfos;
  }

  @Override
  public List<TermInfo> getAll() {
    DatabaseUtil.setUpDatabase();

    List<TermInfo> termInfos = Yank.queryBeanListSQLKey(SELECT_FROM_DOC_KEY, TermInfo.class, null);

    DatabaseUtil.releaseConnection();
    return termInfos;
  }
}
