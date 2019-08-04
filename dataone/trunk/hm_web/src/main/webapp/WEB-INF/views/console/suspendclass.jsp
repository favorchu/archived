<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<H1>Suspend Class Console</H1>
<b>List:</b>
<ul>
	<c:forEach var="listValue" items="${list}">
		<li><input type="button" value="Del"
			onclick="del(${listValue.key})"> <br /> time:
			${listValue.date} <br />${listValue.detail}
			<hr /></li>
	</c:forEach>
</ul>
<br />
<hr />
<br />
<input value="Submit" type="button" onclick="submit()" />
Time:
<input type="text" id="dateTime" />
<br />
<textarea rows="10" cols="100" id="issueDetail">[示範資料]教育局宣佈 ...</textarea>
<script>
$(function() {
				$("#dateTime").datetimepicker();
});
function submit() {
	if (isBlank($("#dateTime").val())) {
		alert("Please fill in the datetime box");
		return;
	}
	if (isBlank($("#issueDetail").val())) {
		alert("Please fill in the content");
		return;
	}

	var issueDetail = $("#issueDetail").val();
	var time = $("#dateTime").val();

	$.post("add", {
		"content" : issueDetail,
		"time" : time
	}, function() {
		alert("Added!");
		location.reload();

	}).error(function() {
		alert("can not apply the content");
	});

}	
function isBlank(str) {
	return (!str || /^\s*$/.test(str));
}	

function del(key) {
	var metaKey = key;

	if (!confirm("Confirm to del?"))
		return;

	$.ajax({
		url : "del/" + metaKey,
		success : function() {
			location.reload();
		}
	});
}
</script>