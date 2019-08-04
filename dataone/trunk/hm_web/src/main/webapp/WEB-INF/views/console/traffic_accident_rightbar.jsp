<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<ul>
	<c:forEach var="listValue" items="${list}">
		<li><input type="button" value="show on map"
			onclick="show(${listValue.accidentKey})"><input type="button"
			value="Del" onclick="del(${listValue.accidentKey})"> <br />
			time: ${listValue.dateTime} <br />${listValue.detail}
			<hr /></li>
	</c:forEach>
</ul>
