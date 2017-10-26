package com.samjsoares.searchengine.core;

import com.samjsoares.searchengine.constant.TimeConstants;
import com.samjsoares.searchengine.core.Index;
import com.samjsoares.searchengine.core.TermProcessor;
import com.samjsoares.searchengine.dao.DocumentInfoDao;
import com.samjsoares.searchengine.dao.DocumentInfoDaoImpl;
import com.samjsoares.searchengine.dao.TermInfoDao;
import com.samjsoares.searchengine.dao.TermInfoDaoImpl;
import com.samjsoares.searchengine.model.DocumentInfo;
import com.samjsoares.searchengine.model.TermInfo;
import com.samjsoares.searchengine.util.UrlUtil;
import org.jsoup.select.Elements;

import java.util.Map;
import java.util.Set;

public class DatabaseIndex implements Index {
  private DocumentInfoDao documentInfoDao = new DocumentInfoDaoImpl();
  private TermInfoDao termInfoDao = new TermInfoDaoImpl();

  @Override
  public void indexPage(String url, Elements paragraphs) {
    if (paragraphs == null) {
      return;
    }

    url = UrlUtil.getUrlString(url);

    if (url == null) {
      return;
    }

    long docId = documentInfoDao.upsert(url, System.currentTimeMillis());
    TermProcessor termProcessor = new TermProcessor(docId);
    termProcessor.processElements(paragraphs);

    termInfoDao.upsert(termProcessor.getTermInfos());
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

    DocumentInfo documentInfo = documentInfoDao.get(url);
    if (documentInfo == null) {
      return true;
    }

    long nextIndexTime = documentInfo.getTimeIndexed() + TimeConstants.MS_PER_WEEK;
    return System.currentTimeMillis() > nextIndexTime;
  }

  @Override
  public Map<String, Integer> getCounts(String term) {
    return null;
  }
}
