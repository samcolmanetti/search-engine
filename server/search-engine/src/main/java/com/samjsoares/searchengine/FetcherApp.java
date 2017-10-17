package com.samjsoares.searchengine;

import com.samjsoares.searchengine.core.Fetcher;
import com.samjsoares.searchengine.core.FetcherModule;
import dagger.Component;
import org.jsoup.select.Elements;

import javax.inject.Singleton;

public class FetcherApp {

  @Singleton
  @Component(modules = FetcherModule.class)
  interface FetcherInterface {
    Fetcher maker();
  }

  public static void main(String[] args) {
    //FetcherInterface fetcherInterface = DaggerFetcherApp_FetcherInterface.create();
    Fetcher fetcher = DaggerFetcherApp_FetcherInterface.create().maker();

    Elements elements = fetcher.fetch("https://en.wikipedia.org/wiki/Education");

    System.out.println(elements.toString());
  }
}
