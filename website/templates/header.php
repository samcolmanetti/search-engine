<!DOCTYPE html>

<html>

    <head>
        <link href="/css/styles.css" rel="stylesheet"/>

        <?php if (isset($title)): ?>
            <title>Soar: <?= htmlspecialchars($title) ?></title>
        <?php else: ?>
            <title>Soar</title>
        <?php endif ?>

    </head>

    <body>

        <div class="container">
            <div id="middle">
