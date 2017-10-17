package com.samjsoares.searchengine.core;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class FetcherModule {

  @Provides
  @Singleton
  Fetcher provideFetcher() {
    return new Fetcher();
  }
}
