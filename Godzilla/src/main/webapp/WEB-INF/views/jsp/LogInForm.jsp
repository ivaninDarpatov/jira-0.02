<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="css/registrationForm.css" />
<title>Godzilla Log in</title>
</head>
<body>
<div class="testbox">
		<h1>Godzilla</h1>

		<form action="./login" method="POST">
			<hr>
			<div class="accounttype">
				<h3>Log In</h3>
			</div>
			<hr>
			<label id="icon" for="name"><i class="icon-envelope "></i></label>
			 <input	type="text" name="companyName" id="name" placeholder="Company" required />
		    <label id="icon" for="name"><i class="icon-user"></i></label>
		     <input	type="text" name="email" id="name" placeholder="Email" required />
			<label id="icon" for="name"><i class="icon-shield"></i></label> 
			 <input	type="password" name="password" id="name" placeholder="Password" required />
			<button type="submit" >Log In</button>
			<p id="errors" > </p>
			<a href="./registration" class="button">Register User</a>
		</form>
	</div>
</body>
</html>