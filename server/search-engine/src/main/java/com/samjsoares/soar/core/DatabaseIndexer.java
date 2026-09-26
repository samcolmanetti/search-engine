package com.samjsoares.soar.core;

import com.samjsoares.soar.constant.TimeConstants;
import com.samjsoares.soar.dao.DocumentInfoDao;
import com.samjsoares.soar.dao.TermInfoDao;
import com.samjsoares.soar.util.UrlUtil;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    long docId =
        documentInfoDao.upsert(
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
  public void printIndex() {}

  @Override
  public boolean shouldIndex(String url) {
    List<String> urls = UrlUtil.getHttpAndHttpsUrlStrings(url);
    if (urls.isEmpty()) {
      return false;
    }

    // A page indexed over either http or https counts as indexed.
    long lastIndexedTime = -1;
    for (String candidate : urls) {
      lastIndexedTime = Math.max(lastIndexedTime, documentInfoDao.getTimeIndexed(candidate));
    }

    long nextIndexTime = lastIndexedTime + TimeConstants.MS_PER_WEEK;
    return System.currentTimeMillis() > nextIndexTime;
  }

  @Override
  public Map<String, Integer> getCounts(String term) {
    return null;
  }
}
