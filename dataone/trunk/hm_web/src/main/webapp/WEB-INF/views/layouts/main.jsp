<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib
	uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
            "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="//ajax.googleapis.com/ajax/libs/dojo/1.8.3/dojo/resources/dojo.css">
<link rel="stylesheet" type="text/css"
	href="../js/jquery.datetimepicker.css" />
<link rel="stylesheet"
	href="//ajax.googleapis.com/ajax/libs/dojo/1.8.3/dijit/themes/claro/claro.css"
	media="screen">
<link rel="stylesheet" media="screen,projection" type="text/css"
	href="../css/reset.css" />
<!-- RESET -->
<link rel="stylesheet" media="screen,projection" type="text/css"
	href="../css/main.css" />
<!-- MAIN STYLE SHEET -->
<link rel="stylesheet" media="screen,projection" type="text/css"
	href="../css/2col.css" title="2col" />
<!-- DEFAULT: 2 COLUMNS -->
<link rel="alternate stylesheet" media="screen,projection"
	type="text/css" href="../css/1col.css" title="1col" />
<!-- ALTERNATE: 1 COLUMN -->
<!--[if lte IE 6]><link rel="stylesheet" media="screen,projection" type="text/css" href="../css/main-ie6.css" /><![endif]-->
<!-- MSIE6 -->
<link rel="stylesheet" media="screen,projection" type="text/css"
	href="../css/style.css" />
<!-- GRAPHIC THEME -->
<link rel="stylesheet" media="screen,projection" type="text/css"
	href="../css/mystyle.css" />
<!-- WRITE YOUR CSS CODE HERE -->
<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/jquery.datetimepicker.js"></script>
<script type="text/javascript" src="../js/switcher.js"></script>
<script type="text/javascript" src="../js/toggle.js"></script>
<script type="text/javascript" src="../js/ui.core.js"></script>
<script type="text/javascript" src="../js/ui.tabs.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(".tabs > ul").tabs();
	});
</script>
<title>Console</title>
<script>
	dojoConfig = {
		parseOnLoad : true
	}
</script>
<script src="//ajax.googleapis.com/ajax/libs/dojo/1.8.3/dojo/dojo.js"></script>


</head>
<body class="claro">
	<div id="main">

		<!-- Tray -->
		<div id="tray" class="box">

			<p class="f-left box">

				<!-- Switcher -->
				<!--<span class="f-left" id="switcher"> <a href="#" rel="1col" class="styleswitch ico-col1"
					title="Display one column"><img src="../img/switcher-1col.gif" alt="1 Column" /></a> <a href="#" rel="2col"
					class="styleswitch ico-col2" title="Display two columns"><img src="../img/switcher-2col.gif" alt="2 Columns" /></a>
				</span> Project: <strong>Your Project</strong>
-->
			</p>

			<p class="f-right">
				User: <strong><%=session.getAttribute("username")%>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <strong><a href="../logout"
						id="logout">Log out</a></strong>
			</p>

		</div>
		<!--  /tray -->

		<hr class="noscreen" />


		<!-- /header -->

		<hr class="noscreen" />

		<!-- Columns -->
		<div id="cols" class="box">

			<!-- Aside (Left Column) -->
			<div id="aside" class="box">

				<!-- /padding -->
				<div class="padding box">

					<!-- Logo (Max. width = 200px) -->
					<p id="logo"></p>
				</div>

				<ul class="box">
					<li><a href="../trafficconsole/">Traffic Monitor</a></li>
					<li><a href="../addtrafficaccidentconsole/">Add Traffic
							Accident Console</a></li>
					<li><a href="../trafficaccidentconsole/">Traffic Accident
							Console</a></li>
					<li><a href="../weatherconsole/">Weather Console</a></li>
					<li><a href="../suspendclassconsole/">Suspend Class
							Console</a></li>

				</ul>
			</div>
			<!-- /aside -->

			<hr class="noscreen" />

			<!-- Content (Right Column) -->
			<div id="content" class="box">
				<tiles:insertAttribute name="primary-content" />

				<!-- /content -->

			</div>
			<!-- /cols -->

			<hr class="noscreen" />

			<!-- Footer -->
			<div id="footer" class="box">

				<p class="f-left"></p>

				<p class="f-right"></p>

			</div>
			<!-- /footer -->

		</div>
		<!-- /main -->
	</div>
</body>
</html>
