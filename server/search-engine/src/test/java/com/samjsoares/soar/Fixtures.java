package com.samjsoares.soar;

import com.panforge.robotstxt.RobotsTxt;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/** Loads test fixtures from {@code src/test/resources/fixtures}. */
public final class Fixtures {

  private Fixtures() {}

  /** Parses {@code fixtures/html/<name>} as if it had been fetched from {@code baseUri}. */
  public static Document html(String name, String baseUri) {
    try (InputStream in = open("html/" + name)) {
      return Jsoup.parse(in, "UTF-8", baseUri);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /** Parses {@code fixtures/robots/<name>} as a robots.txt file. */
  public static RobotsTxt robots(String name) {
    try (InputStream in = open("robots/" + name)) {
      return RobotsTxt.read(in);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static InputStream open(String path) {
    InputStream in = Fixtures.class.getResourceAsStream("/fixtures/" + path);
    if (in == null) {
      throw new IllegalArgumentException("Missing fixture: " + path);
    }
    return in;
  }
}
