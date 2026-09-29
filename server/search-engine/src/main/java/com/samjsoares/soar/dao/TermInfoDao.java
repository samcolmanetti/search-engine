package com.samjsoares.soar.dao;

import com.samjsoares.soar.model.TermInfo;
import java.util.List;

/** Reads and writes per-document term counts in the {@code term_info} table. */
public interface TermInfoDao {
  /**
   * Inserts the term count, or replaces the count for the same document and term. Returns the
   * number of rows changed, or 0 if the write failed.
   */
  long upsert(TermInfo termInfo);

  /**
   * Replaces all of a document's term counts with {@code termInfos} in one transaction. Returns the
   * number of rows written.
   */
  int upsert(long docId, List<TermInfo> termInfos);

  /** Returns the count of a term in a document, or null if there is none. */
  TermInfo get(long documentId, String term);

  /** Returns all term counts for a document. */
  List<TermInfo> getAll(long documentId);

  /** Returns up to 1000 term counts across all documents. */
  List<TermInfo> getAll();
}
