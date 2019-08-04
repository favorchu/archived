<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<head>

<!-- Basics -->

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

<title>Login</title>

<!-- CSS -->

<link rel="stylesheet" href="css/login_reset.css">
<link rel="stylesheet" href="css/login_styles.css">

</head>

<!-- Main HTML -->

<body>

	<!-- Begin Page Content -->

	<div id="container">
		<p style="font-size: 26; color: #aaa;">Time Sheet</p>
		<form method="post">
			<label for="name">Username:</label> <input type="name" name="username" value="${username}"> <label
				for="username">Password:</label> <input type="password" name="password">


			<div id="lower">

				<input type="submit" value="Login">

			</div>
		</form>

	</div>


	<!-- End Page Content -->

</body>

</html>






