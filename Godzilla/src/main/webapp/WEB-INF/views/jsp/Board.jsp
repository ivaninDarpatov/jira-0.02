<%@page import="com.godzilla.model.User"%>
<%@page import="java.util.Set"%>
<%@page import="com.godzilla.model.Project"%>
<%@page import="com.godzilla.model.Company"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<title></title>
	
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
	
	<!-- board javascript -->
	<script src="js/board.js"></script>

<script>
	
</script>
	
</head>

<body>

<c:import url="Navigation.jsp"/>

<!-- hidden input to hold session attribute -->
<input id='projectSprintIssuesMap' type="hidden" value='${projectSprintIssuesMap}'/>

	<!-- Page Content -->
	<div class="container">
		<div class="row">
			
<!-- LEFT SIDE -->
			<div class="col-md-8">
				<h2 id="project_name"></h2>
				<div id="create_sprint_board_div">
				</div>
				<br>
				<div id="project_sprints">
				
				</div>
				<hr>
				<div id="opened_sprint_container">
					<table id='opened_sprint'>
						<h4 id="sprint_name"></h4>
						<tr class="sprint_table_element">
							<th class="sprint_table_element">TO DO</th>
							<th class="sprint_table_element">IN PROGRESS</th>
							<th class="sprint_table_element">DONE</th>
						</tr>
						<tr class="sprint_table_element">
							<td id="to_do" class="issues_container, sprint_table_element"></td>
							<td id="in_progress" class="issudes_container, sprint_table_element"></td>
							<td id="done" class="issues_container, sprint_table_element"></td>
						</tr>
					</table>
				</div>
				
			</div>
<!-- /LEFT SIDE -->

<!-- RIGHT SIDE -->
			<!-- User projects Sidebar -->
			<div class="col-md-4">
			
				<!-- Sidebar Well -->
				<div id='issue_info_well' class="well">
					
				</div>
			</div>
<!-- /RIGHT SIDE -->

		</div>
		<!-- /.row -->

		<hr>

		<!-- Footer -->
		<footer>
			<div class="row">
				<div class="col-lg-12">
					<p>
						Copyright &copy; Godzilla 2016
					</p>
				</div>
				<!-- /.col-lg-12 -->
			</div>
			<!-- /.row -->
		</footer>

	</div>
	<!-- /.container -->


</body>
</html>