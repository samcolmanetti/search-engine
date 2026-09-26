<?php
// configuration
require("../includes/config.php");

render("search.php", ["title" => isset($_GET["q"]) ? $_GET["q"] : ""]);

 ?>
