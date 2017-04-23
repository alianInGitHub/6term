<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Graph3D</title>
    <link rel="stylesheet" type="text/css" href="resources/styles.css">
</head>
<body onload="InitConnection()">
<div class="container" >
    <canvas id = "graph" width="1900" height="920"></canvas>
    <div id="overlay">
        <div>Source: <span id="source"></span></div>
        <div>Sink: <span id="sink"></span> </div>
        <div>Weight: <span id="weight"></span></div>
    </div>
</div>

<script src="resources/gl-matrix.js"></script>
<script src="resources/index.js"></script>
<script src="resources/GraphDataReader.js"></script>
</body>
</html>