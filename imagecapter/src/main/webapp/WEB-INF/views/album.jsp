<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="../../../../resources/js/jquery-1.10.1.min.js"></script>
<script type="text/javascript" src="../../../../resources/fancybox/jquery.fancybox.pack.js"></script>
<link rel="stylesheet" href="../../../../resources/fancybox/jquery.fancybox.css" type="text/css" media="screen" />

<title>Album</title>
<style>
body {
	font-family: 'Helvetica Neue', Helvetica, Arial, 'lucida grande', tahoma,
		verdana, arial, sans-serif;
}

.list_item .deleteLink:link {
	color: #CCC;
} /* unvisited link */
.list_item .deleteLink:visited {
	color: #CCC;
} /* visited link */
.list_item .deleteLink:hover {
	color: #CCC;
} /* mouse over link */
.list_item .deleteLink:active {
	color: #CCC;
} /* selected link */
.list_item .deleteLink {
	font-size: 7pt;
	cursor: pointer;
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
<script>
	/* This is basic - uses default settings */
	$(document).ready(function() {
		//init fancybox
		$(".list_item a.preview").fancybox({
			preload : 0
		});

		//Init the add to cart button

		$(".list_item  .addToCartBtn").click(function() {
			var id = $(this).attr("hidden_img_id");
			add2Cart([ id ]);
		});
		$(".list_item  .removeItemBx").click(function() {
			var id = $(this).attr("hidden_img_id");
			deleteFromCart([ id ]);
		});

	});

	function add2Cart(ids) {
		$.ajax({
			type : "POST",
			url : "add2cart",
			data : {
				keys : ids
			},
			success : function(data) {
				setItemAdded(data);
			},
		});

	}
	function deleteFromCart(ids) {
		$.ajax({
			type : "POST",
			url : "removefromcart",
			data : {
				keys : ids
			},
			success : function(data) {
				setItemRemoved(data);
			},
		});
	}

	function setItemAdded(ids) {
		if (typeof ids == "string")
			ids = eval("(" + ids + ")");

		for ( var i in ids) {
			$(".list_item  .addToCartBtn[hidden_img_id=" + ids[i] + "]")
					.parents(".list_item").addClass('selected');
		}
	}
	function setItemRemoved(ids) {
		if (typeof ids == "string")
			ids = eval("(" + ids + ")");

		for ( var i in ids) {
			$(".list_item  .addToCartBtn[hidden_img_id=" + ids[i] + "]")
					.parents(".list_item").removeClass('selected');
		}
	}

	function addAll2Cart() {
		var list = [];
		$(".list_item:not(.selected) .addToCartBtn").each(function() {
			list.push($(this).attr("hidden_img_id"));
		});
		add2Cart(list);
	}
	function removeAllFromCart() {
		var list = [];
		$(".list_item.selected .addToCartBtn").each(function() {
			list.push($(this).attr("hidden_img_id"));
		});
		deleteFromCart(list);
	}

	function deleteImage(id) {
		var answer = confirm("Confirm to delete this image?");
		if (answer) {
			$.ajax({
				type : "POST",
				url : "../../../image/" + id + "/delete",
				data : {
					keys : id
				},
				success : function(data) {
					$(".list_item  .addToCartBtn[hidden_img_id=" + data + "]")
							.parents(".list_item").remove();
				},
			});
		}
	}
</script>
</head>
<body>
	<div>
		<a href="javascript:addAll2Cart()">Add all to cart</a> <br /> <a href="javascript:removeAllFromCart()">Remove all
			from cart</a> <br /> <a href="../../../album/thai2013s/">Back to pick day list</a> <br /> <a href="../../../upload/">upload
			my photos</a> <br /> <a href="../../../cart/download/thai2013s/">View my download cart</a> <br />${donwloadurl}
	</div>
	<div>
		<c:forEach items="${images}" var="image">
			<div class="list_item ${image.selectedClass} }">
				<a class="preview" href="../../../image/${image.imImageKey}/preview.jpg" rel="gallery">
					<div class="icon" style="background-image: url(../../../image/${image.imImageKey});">
						<div class="infobox">
							<div class="info">${image.imCreatedBy}${image.cameraMake}@${image.imCaptureTime}</div>
						</div>
					</div>
				</a>
				<div class="image_action">
					<div class="left">
						<a href="../../../image/${image.imImageKey}/large.jpg" target="_blank"> <img
							src="../../../../resources/img/enlarge_btn.png"></a> <a class="deleteLink"
							href="javascript:deleteImage(${image.imImageKey})">Delete</a>
					</div>
					<div class="right">
						<div class="removeItemBx" hidden_img_id="${image.imImageKey}">
							Remove<br />from cart
						</div>
						<img src="../../../../resources/img/add_to_shopping_cart.png" hidden_img_id="${image.imImageKey}"
							class="addToCartBtn" />
					</div>
				</div>

			</div>
	</div>
	</c:forEach>
	</div>
</body>
</html>