package com.samjsoares.searchengine.dao;

import com.samjsoares.searchengine.model.DocumentInfo;
import com.samjsoares.searchengine.util.DatabaseUtil;
import org.knowm.yank.Yank;

import java.util.List;

public class DocumentInfoDaoImpl implements DocumentInfoDao {
  private final static String UPSERT_KEY = "DOC_INFO_UPSERT";
  private final static String SELECT_WITH_ID = "DOC_INFO_SELECT_WITH_ID";
  private final static String SELECT_WITH_URL = "DOC_INFO_SELECT_WITH_URL";
  private final static String SELECT_ID = "DOC_INFO_SELECT_ID";
  private final static String SELECT_ALL = "DOC_INFO_SELECT_ALL";

  public DocumentInfoDaoImpl() {}

  @Override
  public long upsert (String url, long timeIndexed) {
    DatabaseUtil.setUpDatabase();

    Object[] params = new Object[] { url, timeIndexed};
    long id = Yank.insertSQLKey(UPSERT_KEY, params);

    if (id == 0) {
      System.out.println("Failed to insert: " + url);
    }

    DatabaseUtil.releaseConnection();
    return id;
  }

  @Override
  public long upsert(DocumentInfo documentInfo) {
    return upsert(documentInfo.getUrl(), documentInfo.getTimeIndexed());
  }

  @Override
  public int[] upsert(List<DocumentInfo> documentInfos) {
    DatabaseUtil.setUpDatabase();

    Object[][] params = new Object[documentInfos.size()][];
    for (int i = 0; i < documentInfos.size(); i++) {
      DocumentInfo documentInfo = documentInfos.get(i);
      params[i] = new Object[] { documentInfo.getUrl(), documentInfo.getTimeIndexed() };
    }

    int[] insertedRows = Yank.executeBatchSQLKey(UPSERT_KEY, params);

    DatabaseUtil.releaseConnection();
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
    DatabaseUtil.setUpDatabase();
    DocumentInfo documentInfo;

    if (id != null) {
      Object[] params = new Object[]{id};
      documentInfo = Yank.queryBeanSQLKey(SELECT_WITH_ID, DocumentInfo.class, params);
    } else {
      Object[] params = new Object[] {url};
      documentInfo = Yank.queryBeanSQLKey(SELECT_WITH_URL, DocumentInfo.class, params);
    }

    DatabaseUtil.releaseConnection();
    return documentInfo;
  }

  @Override
  public long getId (String url) {
    DatabaseUtil.setUpDatabase();

    Object[] params = new Object[] {url};
    Long id = Yank.queryScalarSQLKey(SELECT_ID, Long.class, params);

    DatabaseUtil.releaseConnection();
    return id;
  }

  @Override
  public List<DocumentInfo> getAll() {
    DatabaseUtil.setUpDatabase();

    List<DocumentInfo> documentInfos = Yank.queryBeanListSQLKey(SELECT_ALL, DocumentInfo.class, null);

    DatabaseUtil.releaseConnection();
    return documentInfos;
  }
}
