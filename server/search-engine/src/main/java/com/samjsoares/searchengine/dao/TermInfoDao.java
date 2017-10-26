package com.samjsoares.searchengine.dao;

import com.samjsoares.searchengine.model.TermInfo;

import java.util.List;

public interface TermInfoDao {
  long upsert (TermInfo termInfo);
  int[] upsert (List<TermInfo> termInfos);
  TermInfo get (long documentId, String term);
  List<TermInfo> getAll (long documentId);
  List<TermInfo> getAll ();
}
