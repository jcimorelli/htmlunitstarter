<!doctype html>
<html lang="en">
<head>
<title>Html Unit Shell</title>
<link rel="shortcut icon" type="image/png" href="resources/images/favicon.ico" />
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="resources/css/htmlunitstarter.css" type="text/css" />
<script src="resources/js/htmlunitstarter.js"></script>
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jStorage/0.4.12/jstorage.js"></script>
<script>
	$(function() {
		$("#tabs").tabs();
	});
</script>
</head>
<body>
	<div id="tabs">
		<ul>
			<li><a href="#tab1">Tab 1</a></li>
			<li><a href="#tab2">Tab 2</a></li>
		</ul>
		<div id="tab1">
		</div>
		<div id="tab2">
		</div>
	</div>
</body>
</html>