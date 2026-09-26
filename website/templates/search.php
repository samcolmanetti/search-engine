<?php
  $query = isset($_GET["q"]) ? $_GET["q"] : "";
?>
<div id="search_bar" style="display: flex; align-items: center;">
  <a href="/index.php"><img alt="Soar" src="/img/small_soar.png"/></a>
  <span>
    <form action="/search.php" method="get">
      <input autocomplete="false" type="text" name="q" value="<?= escape($query) ?>"/>
      <input type="submit" value="Search">
    </form>
  </span>
</div>

<?php
  $url = "localhost:8080/api/search?query=" . urlencode($query);

  $results = json_decode(curl($url));
  if (!empty($results)) {
    foreach ($results as $result) {
      // Results come from crawled pages, so escape everything and only link to web URLs.
      $href = preg_match("/^https?:\/\//i", $result->url) ? $result->url : "#";
      echo '<div class="result">';
      echo '<a class="title" href="' . escape($href) . '">' . escape($result->title) . '</a>';
      echo '<p class="url">' . escape($result->url) . '</p>';
      echo '<p class="description">' . escape($result->description) . '</p>';
      echo '</div>';
    }
  } else {
    echo "<p> Your search - <strong>" . escape($query) . "</strong> - did not match any documents.";
  }

?>
