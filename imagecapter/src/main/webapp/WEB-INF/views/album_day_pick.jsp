<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Ablum</title>
<style>
body {
	font-family: 'Helvetica Neue', Helvetica, Arial, 'lucida grande', tahoma,
		verdana, arial, sans-serif;
}

.list_item:hover {
	background-color: #efefef;
}

.list_item.selected {
	background-color: #FFCC99;
}

.list_item.selected .addToCartBtn {
	display: none;
}

.list_item .addToCartBtn {
	cursor: pointer;
}

.list_item.selected .removeItemBx {
	display: block;
}

.list_item .removeItemBx {
	cursor: pointer;
	font-size: 8pt;
	display: none;
	margin: 10px;
}

.list_item {
	margin: 5px;
	padding: 5px;
	border: 1px solid rgb(209, 209, 209);
	width: 200px;
	height: 240px;
	float: left;
}

.list_item .image_action .left {
	float: left;
	width: auto;
}

.list_item .image_action .right {
	float: right;
	width: auto;
}

.list_item .image_action {
	margin: 5px;
	color: #999;
}

.list_item .icon  .infobox {
	background: linear-gradient(to bottom, rgba(0, 0, 0, 1) 0%,
		rgba(0, 0, 0, 0) 100% );
	color: #FFF;
	position: relative;
	width: 100%;
	height: 53px;
}

.list_item .icon  .infobox  .info {
	margin: 0 3px 3px 3px;
	padding-top: 3px;
	font-size: 10pt;
}

.list_item .icon {
	cursor: pointer;
	float: left;
	background-color: #efefef;
	width: 200px;
	height: 200px;
	background-repeat: no-repeat;
	background-position: center;
	float: left;
}
</style>
</head>
<body>
	<div>
		<br /> <a href="../../upload/">upload my photos</a><br /> <a href="../../cart/download/thai2013s/">View my
			download cart</a>
		<br /> <a href="../../../logout">Logout</a><br /> 
	</div>
	<div>
		<c:forEach items="${days}" var="day">
			<div class="list_item">
				<a href="${day.dayToken}/">
					<div class="icon" style="background-image: url(../../image/${day.thumbKey});">
						<div class="infobox">
							<div class="info">${day.day}</div>
						</div>
					</div>
				</a>

			</div>
		</c:forEach>
	</div>
</body>
</html>