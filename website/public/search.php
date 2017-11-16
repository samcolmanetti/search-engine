<?php
// configuration
require("../includes/config.php");

render("search.php", ["title" => $_GET["q"]]);

 ?>
