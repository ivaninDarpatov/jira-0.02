<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="css/registrationForm.css" />
<title>Godzilla registration</title>

<!-- jQuery JavaScript -->
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<!-- jQuery UI CSS -->
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
	
	<script>
	//company name
	function validateCompanyName() {
		var form = document.getElementById("registration_form");
		var companyName = document.getElementById("company").value;
		//min 5 max 20 characters, only letters, digits and spaces
		var companyNameReg = new RegExp(/^[a-zA-Z0-9 ]{5,20}$/);

		if (!(companyNameReg.test(companyName))) {
			form.company.style.borderColor = 'red';
			console.log("2");

		} else {
			form.company.style.borderColor = 'green';
			console.log("3");

		}
		updateForm();
	}
	//password
	function validatePassword() {
		var form = document.getElementById("registration_form");
		var password = document.getElementById("password").value;
		//min 8 max 20 characters, at least one letter, at least one digit, no spaces
		var passwordReg = new RegExp(/^(?=.*\d)(?=.*[a-zA-Z])(?!.*\s).{8,20}/);

		if (!(passwordReg.test(password))) {
			form.password.style.borderColor = 'red';
			console.log("2");

		} else {
			form.password.style.borderColor = 'green';
			var confirmPassword = document.getElementById("conf_password");
			if (password == confirmPassword.value) {
				confirmPassword.style.borderColor = 'green';
				console.log("3");
			} else {
				confirmPassword.style.borderColor = 'red';
			}

		}
		updateForm();
	}
	
	//email 
	function validateEmail() {
		var form = document.getElementById("registration_form");
		
		//example@domain.com
		var emailReg = new RegExp(
				/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-z]{2,4}$/);
		var email = document.getElementById('email').value;

		console.log("1");

		if (!(emailReg.test(email))) {
			form.email.style.borderColor = 'red';
			console.log("2");

		} else {
			form.email.style.borderColor = 'green';
			console.log("3");

		}
		updateForm();
	}

	//deactivate login button if not all fields are the correct format
	function updateForm() {
		var company = document.getElementById("company");
		var email = document.getElementById("email");
		var password = document.getElementById("password");
		var confirmPassword = document.getElementById("conf_password");
		
		var button = document.getElementById("register");
		
		if (company.style.borderColor == 'green' &&
			email.style.borderColor == 'green' && 
			password.style.borderColor == 'green' &&
			confirmPassword.style.borderColor == 'green') {
			
			button.disabled = false;
		} else {
			button.disabled = true;
		}
		
		if (button.disabled == true) {
			console.log("not now");
		} else {
			console.log("ok now");
		}
	}
	</script>
</head>
<body>
	<div class="testbox">
		<h1>Godzilla</h1>

		<form id="registration_form" action="./registration" method="POST">
			<hr>
			<div class="accounttype">
				<h3>Registration</h3>
			</div>
			<hr>
			<label id="icon" for="name"><i class="icon-envelope "></i></label>
			 <input onkeyup="validateCompanyName()"	type="text" name="company" id="company" placeholder="Company" required />
		    <label id="icon" for="name"><i class="icon-user"></i></label>
		     <input	onkeyup="validateEmail()" type="text" name="email" id="email" placeholder="Email" required />
			<label id="icon" for="name"><i class="icon-shield"></i></label> 
			 <input onkeyup="validatePassword()"	type="password" name="password" id="password" placeholder="Password" required />
			<label id="icon" for="name"><i class="icon-shield"></i></label> 
			 <input onkeyup="validatePassword()"	type="password" name="conf_password" id="conf_password" placeholder="Confirm password" required />
			
			<div style="text-align:center; color:red; font-family:'Arial Black';" id="errors"><b>${error} </b></div>
			
			<button type="submit" id="register" disabled>Register</button>
			<a href="./login" class="button">Back to Log In</a>
			
			<c:remove var="error"/>
		</form>
	</div>
</body>
</html>