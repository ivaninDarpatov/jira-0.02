<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="css/registrationForm.css" />
<title>Godzilla registration</title>
</head>
<body>
	<div class="testbox">
		<h1>Godzilla</h1>

		<form action="./registration" method="POST">
			<hr>
			<div class="accounttype">
				<h3>Registration</h3>
			</div>
			<hr>
			<label id="icon" for="name"><i class="icon-envelope "></i></label>
			 <input	type="text" name="company" id="name" placeholder="Company" required />
		    <label id="icon" for="name"><i class="icon-user"></i></label>
		     <input	type="text" name="email" id="name" placeholder="Email" required />
			<label id="icon" for="name"><i class="icon-shield"></i></label> 
			 <input	type="password" name="password" id="name" placeholder="Password" required />
			<label id="icon" for="name"><i class="icon-shield"></i></label> 
			 <input	type="password" name="conf_password" id="name" placeholder="Confirm password" required />
			
			<div style="text-align:center; color:red; font-family:'Arial Black';" id="errors"><b>${error} </b></div>
			
			<button type="submit">Register</button>
			<a href="./login" class="button">Back to Log In</a>
			
			<c:remove var="error"/>
		</form>
	</div>

	<!--
	<form action="./registration" method="POST">
  	Company:<br>
  	<input type="text" name="company" placeholder="Enter company name"><br>
  	Email:<br>
  	<input type="text" name="email" placeholder="Enter your email"><br>
  	Password:<br>
  	<input type="password" name="password" placeholder="Type your password"><br>
  	*min 8 symbols, at least one letter and at least one digit*<br>
  	Confirm Password:<br>
  	<input type="password" name="conf_password" placeholder="Confirm your password"><br><br>
  	<input type="submit" value="Register">
	</form> -->
</body>
</html>