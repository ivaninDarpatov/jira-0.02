<%@page import="com.godzilla.model.User"%>
<%@page import="java.util.Set"%>
<%@page import="com.godzilla.model.Project"%>
<%@page import="com.godzilla.model.Company"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Navigation bar</title>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- jQuery UI CSS -->
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

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

<!-- page-specific JS -->
<script src="js/backlog.js"></script>
<script src="js/user_panel.js"></script>
<script src="js/board.js"></script>
<script src="js/profile_page.js"></script>
</head>
<body>
	<!-- hidden input to hold session attr -->
	
	<input id='companyUsers' type="hidden"
		value='${companyUsers}' />
	<input id='user' type="hidden"
		value='${userJSON}' />
	<input id='projectSprintIssuesMap' type="hidden"
		value='${projectSprintIssuesMap}' />
	<input id='projectSprintsMap' type="hidden"
		value='${projectSprintsMap}' />
	<input id="userInformation" type="hidden" value='${userJSON}'>
	<input id="assignedIssues" type="hidden" value='${assignedIssues}'>
	<input id="reportedIssues" type="hidden" value='${reportedIssues}'>
	
	<!-- hidden delete issue form -->
	<form style="display: none;" action="./deleteissue" method="GET">
		<input name="issue_id" type="text" id="delete_issue_id"/>
		<input type="submit" id="delete_issue_button">
	</form>

	<!-- Navigation -->
	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
	<div class="container">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#bs-example-navbar-collapse-1">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="" style="font-size: 200%;"> <b>
					godzilla | <small style="font-size: 60%;"> ${company.name}
				</small>
			</b>
			</a>
		</div>
		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse"
			id="bs-example-navbar-collapse--1">
			<ul class="nav navbar-nav">
				<li><a href='#' id='projects_button' class='dropbtn'
					onclick="toggleShowDiv('#projects_menu')"> Projects </a>
					<div id='projects_menu' class='dropdown_content'>
						<ul id='projects_menu_ul' class="nav navbar-nav">
							<c:if test="${user.permissions == 'MANAGER'}">
								<li><a href='#' id="create_project" class="dialog_opener">Create
										Project</a></li>
								<br>
							</c:if>

							<c:forEach items="${companyProjects}" var="project"
								varStatus="loop">
								<!-- da si izbere funkciq za onclick ${pageContext.request.servletPath} - WEB-INF/views/jsp/HomePage.jsp -->
								<c:set var="path" value="${pageContext.request.servletPath}" />
								<li><a href='#' id="menu_project_${loop.index}"
									onclick="selectProjectNameDispatcher('${path}', '${project.name}')">${project.name}</a>
								</li>
								<br>
								<!-- open first project info upon startup -->
								<c:if test="${loop.index == 0}">
									<script>
										if ($("#menu_project_0")) {
											if ("${path}".substring("${path}".lastIndexOf('/') + 1, "${path}".lastIndexOf('.jsp')).localeCompare("HomePage") != 0) {
												$(document).ready(function(){
												    $('#menu_project_0').trigger('click');
												});
											}
											
										}
									</script>
								</c:if>
							</c:forEach>
						</ul>
					</div></li>
				<li><a href="#" id="create_issue" class="dialog_opener">Create
						Issue</a></li>
				<li><a href="./backlog">Backlog</a></li>
				<li><a href="./board">Board</a></li>
				<li><a href="./filters">Filters</a></li>
				<li><a href="./homepage">User Panel</a></li>
				<li><a href='#' id='profile_button' class='dropbtn'
					onclick="toggleShowDiv('#profile_menu')"> Profile </a>
					<div id='profile_menu' class='dropdown_content'>
						<ul id='profile_menu_ul' class="nav navbar-nav">
							<li><a href='./profilepage'> <img class="user_thumbnail"
									src="images/ninja_avatar/${user.ninjaColor}.jpg"> ${user.email}
							</a></li>
							<br>
							<li><a href="./login">Log Out</a></li>
						</ul>
					</div></li>
			</ul>
		</div>
		<!-- /.navbar-collapse -->
	</div>
	<!-- /.container --> </nav>
	
	
	<c:import url="CreateIssue.jsp" />
	<c:import url="CreateProject.jsp" />
	<c:import url="CreateSprint.jsp" />

</body>
</html>