package com.samjsoares.searchengine;

import com.samjsoares.searchengine.core.Fetcher;
import com.samjsoares.searchengine.core.FetcherModule;
import dagger.Module;

import javax.inject.Inject;

@Module(includes = FetcherModule.class)
public class FetcherDITester {

  private Fetcher fetcher;

  @Inject
  public FetcherDITester(Fetcher fetcher) {
    this.fetcher = fetcher;
  }

  public static void main(String args[]) {
    FetcherDITester diTester;
  }

}
