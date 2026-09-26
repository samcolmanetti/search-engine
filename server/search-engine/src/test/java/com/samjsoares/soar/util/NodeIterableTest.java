package com.samjsoares.soar.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.jupiter.api.Test;

public class NodeIterableTest {

  private static Element root() {
    return Jsoup.parseBodyFragment("<div id=\"root\"><p>one<b>two</b></p><span>three</span></div>")
        .getElementById("root");
  }

  private static String label(Node node) {
    return node instanceof TextNode ? ((TextNode) node).text() : node.nodeName();
  }

  @Test
  public void testPreOrderDepthFirst() {
    List<String> visited = new ArrayList<>();
    for (Node node : new NodeIterable(root())) {
      visited.add(label(node));
    }

    assertThat(visited).containsExactly("div", "p", "one", "b", "two", "span", "three");
  }

  @Test
  public void testLeafNodeYieldsOnlyItself() {
    Iterator<Node> iterator = new NodeIterable(new TextNode("leaf", "")).iterator();

    assertThat(label(iterator.next())).isEqualTo("leaf");
    assertThat(iterator.hasNext()).isFalse();
  }

  @Test
  public void testNextPastEndThrows() {
    Iterator<Node> iterator = new NodeIterable(new TextNode("leaf", "")).iterator();
    iterator.next();
    assertThrows(NoSuchElementException.class, () -> iterator.next());
  }

  @Test
  public void testRemoveThrows() {
    Iterator<Node> iterator = new NodeIterable(root()).iterator();
    iterator.next();
    assertThrows(UnsupportedOperationException.class, () -> iterator.remove());
  }
}
