<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Godzilla registration</title>
</head>
<body>
	<form action="/RegisterServlet" method="POST">
  	Company:<br>
  	<input type="text" name="company" placeholder="Enter company name"><br>
  	Email:<br>
  	<input type="text" name="email" placeholder="Enter your email"><br><br>
  	Password:<br>
  	<input type="password" name="password" placeholder="Type your password"><br><br>
  	<input type="submit" value="Submit">
</form>
</body>
</html>