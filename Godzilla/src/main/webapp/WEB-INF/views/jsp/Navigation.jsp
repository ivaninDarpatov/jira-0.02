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
<title>Insert title here</title>
	
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
	
	<!-- page-specific JS -->
	<script src="js/backlog.js"></script>
	<script src="js/user_panel.js"></script>
	<script src="js/board.js"></script>
	<script src="js/profile_page.js"></script>
	
	<script>
	function selectProjectNameDispatcher(path, projectName) {
		//get opened page's name
		var pageNameStart = path.lastIndexOf('/') + 1;
		var pageNameEnd = path.lastIndexOf('.jsp');
		var pageName = path.substring(pageNameStart, pageNameEnd);
		
		switch (pageName) {
		case 'Board':
			loadProjectSprintsBoard(projectName);
			break;
		case 'Backlog':
			loadProjectSprintsBacklog(projectName);
			break;
		case 'HomePage':
			var s_assignedIssues = document.getElementById("assignedIssues").value;
			var s_reportedIssues = document.getElementById("reportedIssues").value;
			var assignedIssues = JSON.parse(s_assignedIssues);
			var reportedIssues = JSON.parse(s_reportedIssues);
			loadIssues(projectName, assignedIssues, reportedIssues);
		case 'ProfilePage':
			return;
		}
	}
	</script>
</head>
<body>
<!-- hidden input to hold session attr -->
<input id='projectSprintIssuesMap' type="hidden" value='${projectSprintIssuesMap}'/>
<input id="userInformation" type="hidden" value='${userJSON}'>
<input id="assignedIssues" type="hidden" value='${assignedIssues}'>
<input id="reportedIssues" type="hidden" value='${reportedIssues}'>

	<!-- Navigation -->
	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target="#bs-example-navbar-collapse-1">
					<span class="sr-only">Toggle navigation</span> 
					<span class="icon-bar"></span> 
					<span class="icon-bar"></span> 
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="" style="font-size: 200%;">
					<b>
						godzilla |
						<small style="font-size: 60%;"> 
							${company.name}
						</small>
					</b>
				</a>
			</div>
			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse--1">
				<ul class="nav navbar-nav">
					<li>
						<a href='#' id='projects_button' class='dropbtn'
						onclick="toggleShowDiv('#projects_menu')">
							Projects
						</a>
						<div id='projects_menu' class='dropdown_content'>
							<ul id='projects_menu_ul' class="nav navbar-nav">
								<c:if test="${user.permissions == 'MANAGER'}">
									<li>
										<a href='#'>Create Project</a>
									</li>
									<br>
								</c:if>

								<c:forEach items="${companyProjects}" var="project">
									<li>
										<!-- da si izbere funkciq za onclick ${pageContext.request.servletPath} - WEB-INF/views/jsp/HomePage.jsp -->
										<c:set var="path" value="${pageContext.request.servletPath}"/>
										<a href='#' onclick="selectProjectNameDispatcher('${path}', '${project.name}')">${project.name}</a>
										
									</li>
									<br>
								</c:forEach>
							</ul>
						</div>
					</li>
					<li>
						<a href="#" id="opener">Create Issue</a>
					</li>
					<li>
						<a href="./backlog">Backlog</a>
					</li>
					<li>
						<a href="./board">Board</a>
					</li>
					<li>
						<a href="./homepage">User Panel</a>
					</li>
					<li>
						<a href='#' id='profile_button' class='dropbtn'
							onclick="toggleShowDiv('#profile_menu')">
							Profile
						</a>
						<div id='profile_menu' class='dropdown_content'>
							<ul id='profile_menu_ul' class="nav navbar-nav">
								<li>
									<a href='./profilepage'> 
										<img class="user_thumbnail" src="images/profile_photo.png">
											${user.email}
									</a>
								</li>
								<br>
								<li>
									<a href="./login">Log Out</a>
								</li>
							</ul>
						</div>
					</li>
				</ul>
			</div>
			<!-- /.navbar-collapse -->
		</div>
		<!-- /.container -->
	</nav>
</body>
</html>