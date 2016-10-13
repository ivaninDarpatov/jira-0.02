<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="css/registrationForm.css" />
<title>Login</title>
<script>
function validateForm(){
	var form = document.getElementById("login_form");
	//company name
    var companyNameRegex = /^[a-zA-Z0-9 ]+$/;
    if (form.companyName.value == "") {
    	form.companyName.style.borderColor = "red";
    	return;
    }
    var validCompanyName = form.companyName.value.match(companyNameRegex);
    if(validCompanyName == null){
       	form.companyName.style.borderColor = "red";
        return false;
    } else {
    	form.companyName.style.borderColor = "green";
    	return;
    }    
}
</script>

</head>
<body>
<div class="testbox">
		<h1>Godzilla</h1>

		<form action="./login" method="POST" id="login_form">
			<hr>
			<div class="accounttype">
				<h3>Log In</h3>
			</div>
			<hr>
			<label id="icon" for="name"><i class="icon-envelope "></i></label>
			 <input type="text" name="companyName" id="name" placeholder="Company" onkeyup="validateForm()" required />
		    <label id="icon" for="name"><i class="icon-user"></i></label>
		     <input	type="text" name="email" id="name" placeholder="Email" onkeyup="validateForm()" required />
			<label id="icon" for="name"><i class="icon-shield"></i></label> 
			 <input	type="password" name="password" id="name" placeholder="Password" onkeyup="validateForm()" required />
			<div style="text-align:center; color:red; font-family:'Arial Black';" id="errors"><b>${error} </b></div>
			<button type="submit" >Log In</button>
			<a href="./registration" class="button">Register User</a>
		</form>
		 <c:remove var="error"/>
	</div>
</body>
</html>