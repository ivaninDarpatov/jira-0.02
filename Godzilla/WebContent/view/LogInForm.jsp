<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Godzilla Log in</title>
</head>
<body>
<form action="./Login" method="POST">
  	Company Name:<br>
  	<input type="text" name="companyName" placeholder="Type your company name"><br>
  	Email:<br>
  	<input type="text" name="email" placeholder="Enter your email"><br>
  	Password:<br>
  	<input type="password" name="password" placeholder="Type your password"><br>
  	<input type="submit" value="Log in">
</form>
</body>
</html>