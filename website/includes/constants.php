<?php

    // Base URL of the searcher service. Override with the SOAR_API_URL environment variable.
    define("SOAR_API_URL", getenv("SOAR_API_URL") ?: "http://localhost:8080");

?>
