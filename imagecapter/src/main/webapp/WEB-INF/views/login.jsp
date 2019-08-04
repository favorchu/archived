<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<form method="post">
		<table class="login_table">
			<tr>
				<td>
					<div class="login_title">LOGIN</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="username_style">USERNAME</div> <select name="username">
						<option value="">Please select yourself</option>
						<c:forEach items="${users}" var="user">
							<option value="${user}">${user}</option>
						</c:forEach>
				</select>
				</td>
			</tr>
			<tr class="row_space">
				<td></td>
			</tr>
			<tr>
				<td>
					<div class="password_style">PASSWORD</div> <input id="password" class="" type="password" value="" name="password">
				</td>
			</tr>
			<tr class="row_space">
				<td></td>
			</tr>
			<tr class="row_space">
				<td></td>
			</tr>
			<tr class="row_space">
				<td></td>
			</tr>
			<tr class="row_space">
				<td></td>
			</tr>
			<tr>
				<td class="checkbox_style"></td>
			</tr>
			<tr>
				<td class="loginbtn_style"><input id="loginbtn" type="submit" value="Submit"></td>
			</tr>
			<tr>
				<td class="forgot"></td>
			</tr>
			<tr class="row_space">
				<td></td>
			</tr>
			<tr class="row_space">
				<td></td>
			</tr>
		</table>
	</form>
</body>
</html>