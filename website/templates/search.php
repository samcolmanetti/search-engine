<form action="/search.php" method="get">
  <input autocomplete="false" type="text" name="q" value="<? echo $_GET["q"] ?>"/>
  <input type="submit" value="Search">
</form>

<?php
  $query = $_GET["q"];
  $url = "localhost:8080/api/search?query=" . urlencode($_GET["q"]);

  $results = json_decode(curl($url));
  if (!empty($results)) {
    foreach ($results as $result) {
      echo '<div class="result">';
      echo '<a class="title" href="'. $result->url . '">' .$result->title .'</a>';
      echo '<p class="url">' . $result->url . '</p>';
      echo '<p class="description">' . $result->description . '</p>';
      echo '</div>';
    }
  } else {
    echo "<p> Your search - <strong>{$query}</strong> - did not match any documents.";
  }

?>
