<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<H1>Weather Console</H1>
<br />
<textarea id="textBox" cols="100" rows="20">
</textarea>

<script>
	var content = "";
	$.ajax({
		url : "../realtimeweather/",
		success : function(json) {
			content = "";
			getTree(json, "");
			$("#textBox").val(content);
		}
	});

	function getTree(obj, parent) {
		for ( var i in obj) {
			var item = obj[i];

			console.log(typeof item);

			if (typeof item == 'object') {
				content += parent + "\r\n";
				getTree(item, parent.replace(/./g, " ") + "\\" + i);
			} else {
				content += parent + "\\" + i + "\\\"" + item + "\"\r\n";
			}

		}

	}
</script>

