package com.samjsoares.soar.dao;

import com.samjsoares.soar.model.DocumentInfo;

import java.util.List;

public interface DocumentInfoDao {
  long upsert (String url, long timeIndexed, String title, String description);
  long upsert (DocumentInfo documentInfo);
  int[] upsert (List<DocumentInfo> documentInfos);
  DocumentInfo get (long id);
  DocumentInfo get (String url);
  long getId (String url);
  long getTimeIndexed (String url);
  List<DocumentInfo> getAll();
}
