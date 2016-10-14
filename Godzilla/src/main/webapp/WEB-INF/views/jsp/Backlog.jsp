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
<title>Backlog</title>

<script>
		function closeDiv(target){
			var div = $(target);
			div.hide();
		}
</script>

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

<!-- page-specific javascript -->
<script src="js/backlog.js"></script>


</head>
<body>

	<c:import url="Navigation.jsp" />
	
	<c:set var="errorMessage" value="${sessionScope.issueError}" />
	<c:set var="errorLengh" value="${fn:length(errorMessage)}" />
	<c:remove var="issueError" scope="session" />

	<c:set var="succeed" value="${sessionScope.succeed}"></c:set>
	<c:set var="succeedLengh" value="${fn:length(succeed)}" />
	<c:remove var="succeed" scope="session" />
	<div id="errors_container">
		<c:if test="${(errorLengh) gt 0}">
			<div id="tag1" class="tag1"
				style="text-align: center; color: red; font-family: 'Arial Black';"
				id="errors">
				<a class="closeButton" onclick="closeDiv('#tag1')"></a> <b>${(errorMessage)}
				</b>
			</div>
		</c:if>
		<c:if test="${(succeedLengh) gt 0}">
			<div id="tag1" class="tag2"
				style="text-align: center; color: green; font-family: 'Arial Black';"
				id="errors">
				<a class="closeButton" onclick="closeDiv('#tag1')"></a> <b>${(succeed)}</b>
			</div>
		</c:if>
	</div>	

	
	<!-- hidden input to hold session attribute -->
	<input id='projectSprintIssuesMap' type="hidden"
		value='${projectSprintIssuesMap}' />
	<input id='projectSprintsMap' type="hidden"
		value='${projectSprintsMap}' />
	<!-- Page Content -->
	<div class="container">

		<div class="row">
			<h2 id="project_name"></h2>
			<!-- project's issues column -->
			<!-- LEFT SIDE -->
			<div class="col-md-8">
				<div id="create_sprint_backlog_div"></div>
				<hr>
				<div id='sprints_container'></div>

				<hr>

				<h4 id="free_issues"></h4>
				<div id='issues_container'></div>
			</div>
			<!-- /LEFT SIDE -->

			<!-- RIGHT SIDE -->
			<!-- User Sidebar Column -->
			<div class="col-md-4">

				<!-- Issue Information Well -->
				<div id='issue_info_well' class="well"></div>

			</div>
			<!-- /RIGHT SIDE -->

		</div>
		<!-- /.row -->

		<hr>

		<!-- Footer -->
		<footer>
		<div class="row">
			<div class="col-lg-12">
				<p>Copyright &copy; Godzilla 2016</p>
			</div>
			<!-- /.col-lg-12 -->
		</div>
		<!-- /.row --> </footer>

	</div>
	<!-- /.container -->


</body>
</html>