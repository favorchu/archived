<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<form id="fileuploadForm" action="fileupload" method="POST"
		enctype="multipart/form-data" class="cleanform">
		<div class="header">
			<h2>Form</h2>

		</div>
		<label for="file">File</label> <input id="file" type="file"
			name="file" />
		<p>
			<button type="submit">Upload</button>
		</p>
	</form>
</body>
</html>