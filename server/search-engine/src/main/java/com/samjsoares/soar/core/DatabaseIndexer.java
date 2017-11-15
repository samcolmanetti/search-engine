package com.samjsoares.soar.core;

import com.samjsoares.soar.constant.TimeConstants;
import com.samjsoares.soar.dao.DocumentInfoDao;
import com.samjsoares.soar.dao.TermInfoDao;
import com.samjsoares.soar.model.DocumentInfo;
import com.samjsoares.soar.util.UrlUtil;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class DatabaseIndexer implements Indexer {

  private DocumentInfoDao documentInfoDao;
  private TermInfoDao termInfoDao;

  @Autowired
  public DatabaseIndexer(DocumentInfoDao documentInfoDao, TermInfoDao termInfoDao) {
    this.documentInfoDao = documentInfoDao;
    this.termInfoDao = termInfoDao;
  }

  @Override
  public void indexPage(String url, Document document) {
    if (document == null) {
      return;
    }

    url = UrlUtil.getUrlString(url);
    if (url == null) {
      return;
    }

    DocumentProcessor documentProcessor = new DocumentProcessor(url, document);
    long docId = documentInfoDao.upsert(
        url,
        System.currentTimeMillis(),
        documentProcessor.getTitle(),
        documentProcessor.getDescription());

    if (docId <= 0) {
      return;
    }

    TermProcessor termProcessor = new TermProcessor(docId);
    termProcessor.processElements(documentProcessor.getElements());
    termInfoDao.upsert(docId, termProcessor.getTermInfos());
  }

  @Override
  public Set<TermProcessor> get(String term) {

    return null;
  }

  @Override
  public void printIndex() {

  }

  @Override
  public boolean shouldIndex(String url) {
    url = UrlUtil.getUrlString(url);
    if (url == null) {
      return false;
    }

    long nextIndexTime = documentInfoDao.getTimeIndexed(url) + TimeConstants.MS_PER_WEEK;
    return System.currentTimeMillis() > nextIndexTime;
  }

  @Override
  public Map<String, Integer> getCounts(String term) {
    return null;
  }
}
