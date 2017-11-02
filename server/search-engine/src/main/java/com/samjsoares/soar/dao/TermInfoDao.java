package com.samjsoares.soar.dao;

import com.samjsoares.soar.model.TermInfo;

import java.util.List;

public interface TermInfoDao {
  long upsert (TermInfo termInfo);
  int upsert (long docId, List<TermInfo> termInfos);
  TermInfo get (long documentId, String term);
  List<TermInfo> getAll (long documentId);
  List<TermInfo> getAll ();
}
