package com.samjsoares.soar.dao;

import com.samjsoares.soar.model.DocumentInfo;
import java.util.List;

/** Reads and writes indexed pages in the {@code doc_info} table. */
public interface DocumentInfoDao {
  /**
   * Inserts a page, or updates the index time, title, and description of the row with the same URL.
   * Returns the row's id, or -1 if no id came back.
   */
  long upsert(String url, long timeIndexed, String title, String description);

  /**
   * Upserts the URL, index time, title, and description of {@code documentInfo}; its id is ignored.
   * Returns the row's id, or -1 if no id came back.
   */
  long upsert(DocumentInfo documentInfo);

  /** Not implemented yet: writes nothing and returns null. */
  int[] upsert(List<DocumentInfo> documentInfos);

  /**
   * Returns the page with the given id, or null if there is none. Only the id, URL, and index time
   * are filled in.
   */
  DocumentInfo get(long id);

  /**
   * Returns the page with the given URL, or null if there is none. Only the id, URL, and index time
   * are filled in.
   */
  DocumentInfo get(String url);

  /** Returns the id of the page with the given URL, or -1 if there is none. */
  long getId(String url);

  /** Returns when the page with the given URL was indexed, or -1 if there is none. */
  long getTimeIndexed(String url);

  /** Returns up to 1000 pages, with only the id, URL, and index time filled in. */
  List<DocumentInfo> getAll();
}
