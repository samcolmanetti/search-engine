package com.samjsoares.searchengine.dao;

import com.samjsoares.searchengine.model.DocumentInfo;

import java.util.List;

public interface DocumentInfoDao {
  long upsert (String url, long timeIndexed);
  long upsert (DocumentInfo documentInfo);
  int[] upsert (List<DocumentInfo> documentInfos);
  DocumentInfo get (long id);
  DocumentInfo get (String url);
  long getId (String url);
  List<DocumentInfo> getAll();
}
