<?php

    require_once("constants.php");

    function apologize($message){
        render("apology.php", ["message" => $message]);
        exit;
    }

    function dump($variable){
        require("../templates/dump.php");
        exit;
    }

    /**
     * Redirects user to destination, which can be
     * a URL or a relative path on the local host.
     *
     * Because this function outputs an HTTP header, it
     * must be called before caller outputs any HTML.
     */
    function redirect($destination) {
        // handle URL
        if (preg_match("/^https?:\/\//", $destination)) {
            header("Location: " . $destination);
        }

        // handle absolute path
        else if (preg_match("/^\//", $destination))
        {
            $protocol = (isset($_SERVER["HTTPS"])) ? "https" : "http";
            $host = $_SERVER["HTTP_HOST"];
            header("Location: $protocol://$host$destination");
        }

        // handle relative path
        else
        {
            // adapted from http://www.php.net/header
            $protocol = (isset($_SERVER["HTTPS"])) ? "https" : "http";
            $host = $_SERVER["HTTP_HOST"];
            $path = rtrim(dirname($_SERVER["PHP_SELF"]), "/\\");
            header("Location: $protocol://$host$path/$destination");
        }

        // exit immediately since we're redirecting anyway
        exit;
    }

    function render($template, $values = []) {
        // if template exists, render it
        if (file_exists("../templates/$template")){
            // extract variables into local scope
            extract($values);

            require("../templates/header.php");
            require("../templates/$template");
            require("../templates/footer.php");
        }

        else {
            trigger_error("Invalid template: $template", E_USER_ERROR);
        }
    }

    function curl ($url) {
      $ch = curl_init();

       // set url
       curl_setopt($ch, CURLOPT_URL, $url);

       //return the transfer as a string
       curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);

       // $output contains the output string
       $output = curl_exec($ch);

       // close curl resource to free up system resources
       curl_close($ch);

       return $output;
    }

?>
