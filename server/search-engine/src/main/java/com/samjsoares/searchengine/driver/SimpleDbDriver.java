package com.samjsoares.searchengine.driver;

import com.samjsoares.searchengine.dao.DocumentInfoDao;
import com.samjsoares.searchengine.dao.DocumentInfoDaoImpl;
import com.samjsoares.searchengine.model.DocumentInfo;
import org.knowm.yank.PropertiesUtils;
import org.knowm.yank.Yank;

import java.util.Properties;

public class SimpleDbDriver {

  public static void runSimpleRead() {
    DocumentInfoDao documentInfoDao = new DocumentInfoDaoImpl();
    System.out.println(documentInfoDao.get("google.com"));
  }

  private static void runSimpleInsert() {
    DocumentInfoDao documentInfoDao = new DocumentInfoDaoImpl();

    long time1 = System.currentTimeMillis();
    documentInfoDao.upsert("sam1.com", System.currentTimeMillis());
    documentInfoDao.upsert("sam2.com", System.currentTimeMillis());
    documentInfoDao.upsert("sam3.com", System.currentTimeMillis());
    long time2 = System.currentTimeMillis();

    System.out.println("Time to insert: " + (time2 - time1) + "ms");
  }

  private static void printDocuments() {
    DocumentInfoDao documentInfoDao = new DocumentInfoDaoImpl();
    for (DocumentInfo info : documentInfoDao.getAll()) {
      System.out.println(info);
    }
  }

  public static void main (String[] args) {
    runSimpleInsert();
    printDocuments();
  }
}
