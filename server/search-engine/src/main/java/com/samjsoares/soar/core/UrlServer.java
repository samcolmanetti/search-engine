package com.samjsoares.soar.core;

import java.net.URL;

/** Supplies seed URLs for the crawler to start from. */
public interface UrlServer {
  /**
   * Returns the next seed URL, or null when no seeds remain or the next seed is not a valid URL.
   */
  URL getNextUrl();
}
