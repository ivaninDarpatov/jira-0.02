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
	<title>User panel</title>
	
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
	
	<!-- page specific javascript -->
	<script src="js/user_panel.js"></script>
		
	<script>
		function closeDiv(target){
			var div = $(target);
			div.hide();
		}
	</script>
</head>

<body>

<c:import url="Navigation.jsp"/>

<!-- hidden inputs to hold session attributes -->
<input id="assignedIssues" type="hidden" value="${assignedIssues}">
<input id="reportedIssues" type="hidden" value="${reportedIssues}">

	<!-- Page Content -->
	<div class="container">
		<div class="row">
 		<div id="errors_container">
 		
 		<c:set var="errorMessage" value="${issueError}"/>
 		<c:set var="errorLengh" value="${fn:length(errorMessage)}" />
		
		<c:set var="succeed" value="${succeed}"></c:set>
 		<c:set var="succeedLengh" value="${fn:length(succeed)}" />
 		
 	 			<c:if test="${(errorLengh) gt 0}">
 					<div id="tag1" class="tag1" style="text-align:center; color:red; font-family:'Arial Black';" id="errors">
 						<a class="closeButton" onclick="closeDiv('#tag1')"></a>
 						<b>${issueError} </b>
 					</div>
 				 </c:if>
 				 <c:if test="${(succeedLengh) gt 0}">
 				 	<div id="tag1" class="tag2" style="text-align:center; color:green; font-family:'Arial Black';" id="errors">
 						<a class="closeButton" onclick="closeDiv('#tag1')"></a>
 						<b>${succeed}</b>
 					</div>
 				 </c:if>
 		</div>
			<!-- selected project name -->
			<h2 id="project_name"></h2>
			
<!-- LEFT SIDE -->
			<div class="col-md-8">
				<hr>

				<!-- Assigned Issues for user in selected project -->
				<div id="assigned_issues_well">
					<h4>Assigned Issues</h4>
					<div id="assigned_issues"></div>
				</div>

				<hr>

				<!-- Reported Issues for user in selected project -->
				<div id="reported_issues_well">
					<h4>Reported Issues</h4>
					<div id="reported_issues"></div>
				</div>
				<hr>
			</div>
<!-- /LEFT SIDE -->

<!-- RIGHT SIDE -->
			<!-- User projects Sidebar -->
			<div class="col-md-4">
			
				<!-- Projects Well -->
				<div class="well">
					<h4 align=center>Projects you participate in</h4>
					<div id="projects">
					
						<!-- all projects where current user has assigned or reported issues -->
						<c:forEach items="${userProjects}" var="project" varStatus="loop">
							<div class='project_box' id='project_${loop.index}'>
								<c:set var="stringa" value="${assignedIssues}" />
								<h4>
									<a href='#'
										onclick='loadIssues("${project.name}", <c:out value="${assignedIssues}"/>, <c:out value="${reportedIssues}"/>)'
										id='${project.name}'>
										${project.name}
									</a>
								</h4>
								
								<h5 id='project_${loop.index}_assigned'
									class='assigned_issues_count'>
									Assigned Issues
									
									<!-- show number of assigned issues for current user in this project -->
									<script>document.write(showDigit('${project.name}', '${assignedIssues}'))</script>
								</h5>
								
								<h5 id='project_${loop.index}_reported'
									class='reported_issues_count'>
									Reported Issues
									
									<!-- show number of reported issues for current user in this project -->
									<script>document.write(showDigit('${project.name}', '${reportedIssues}'))</script>
								</h5>
							</div>
						</c:forEach>
					</div>
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