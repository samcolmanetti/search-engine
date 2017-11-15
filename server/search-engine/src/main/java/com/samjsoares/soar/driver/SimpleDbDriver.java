package com.samjsoares.soar.driver;

import com.samjsoares.soar.dao.DocumentInfoDao;
import com.samjsoares.soar.model.DocumentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleDbDriver {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private DocumentInfoDao documentInfoDao;

  public void runSimpleRead() {
    logger.info("runSimpleRead results: " + documentInfoDao.get("youtube.com"));
  }

  public  void runSimpleInsert() {
    long time1 = System.currentTimeMillis();

    documentInfoDao.upsert(
        "sam1.com",
        System.currentTimeMillis(),
        "sam1",
        "this is the description for sam1");

    documentInfoDao.upsert(
        "sam2.com",
        System.currentTimeMillis(),
        "sam2",
        "this is the description for sam2");

    documentInfoDao.upsert(
        "sam3.com",
        System.currentTimeMillis(),
        "sam3",
        "this is the description for sam3");

    long time2 = System.currentTimeMillis();

    logger.info("Time to insert: " + (time2 - time1));
  }

  public void printDocuments() {
    logger.info("Printing all documents in Document_Info");
    for (DocumentInfo info : documentInfoDao.getAll()) {
      logger.info(info.toString());
    }
  }

}
