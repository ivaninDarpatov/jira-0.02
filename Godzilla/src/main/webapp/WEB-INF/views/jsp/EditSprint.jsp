<%@page import="com.godzilla.model.User"%>
<%@page import="java.util.Set"%>
<%@page import="com.godzilla.model.Project"%>
<%@page import="com.godzilla.model.Company"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Edit Sprint </title>
	
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	
	<!-- jQuery UI CSS -->
	<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css"> 
	
	<!-- Bootstrap Core CSS -->
	<link href="css/bootstrap.min.css" rel="stylesheet">
	
	<!-- Custom CSS -->
	<link href="css/user_panel.css" rel="stylesheet">
		
	
<!-- javascript files initialization -->
	
	<!-- jQuery JavaScript -->
	<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	
	<!-- Bootstrap Core JavaScript -->
	<script src="js/bootstrap.min.js"></script>
		
	<!-- header functionality javascript -->
	<script src="js/page_header.js"></script>	
	<script src="js/backlog.js"></script>	

	
<script>
//remove textarea preset first row
$(document).ready(function(){
    $('textarea').val("");
});
</script>


</head>
<body>
<div style="display: none;" id="edit_sprint_dialog" class="dialog_box"
		title="Edit Sprint">
		<div id="container" class="ltr">
			<h1 id="logo">
				<a href="./homepage" title="Powered by Godzilla">Godzilla</a>
			</h1>

			<form action="./editSprint" method="POST">
				<input id="sprintId" name="sprintId" type="hidden" value="" >
				<header id="header" class="info">
					<h2>Issue Tracking</h2>
					<div></div>
				</header>
				<ul>
					<li id="fo2li1" class="notranslate      ">
						<label class="desc" id="title1" for="Field1"> Sprint name 
							<span id="req_1" class="req">*</span>
						</label>
							<div>
								<input id="edit_name" name="name" type="text"
									class="field text medium" value="" maxlength="255" tabindex="1"
									required />
							</div>
					</li>

					<li id="fo2li112" class="notranslate      ">
						<label class="desc" id="title112" for="Field112"> 
							Sprint Goal 
						</label>
						<div>
							<textarea id="edit_goal" name="goal"
								class="field textarea medium" spellcheck="true" rows="10">
							</textarea>
						</div>
						</br>
						<button type="submit">Edit Sprint</button>
					</li>
					
					
					<!-- <li class="buttons ">
						<div>
							<input type="hidden" name="currentPage" id="currentPage" /> 
							<input id="saveForm" name="saveForm" class="btTxt submit" type="submit" value="Submit" />
						</div>
					</li> -->
				</ul>
			</form>
		</div>
	</div>
</body>
</html>