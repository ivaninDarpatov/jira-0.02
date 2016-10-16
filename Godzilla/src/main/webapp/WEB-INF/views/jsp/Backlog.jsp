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


<script>
	function closeDiv(target) {
		var div = $(target);
		div.hide();
	}
</script>
<script>
	//draw charts
	function drawChart(projectName) {
		if ($('#project_chart').is(':visible')) {
			$('#project_chart').hide();
		}
		var projects = document.getElementById("companyProjects").value;
		var array = JSON.parse(projects);
		var currentProject;
		for (var i = 0; i < array.length; i++) {
			if (array[i].name == projectName) {
				currentProject = array[i];
				break;
			}
		}

		var issues = currentProject.issues;
		//get issue counts

		var toDo = 0;
		var inProgress = 0;
		var done = 0;

		for (var i = 0; i < issues.length; i++) {
			switch (issues[i].state) {
			case "TO_DO":
				toDo++;
				break;
			case "IN_PROGRESS":
				inProgress++;
				break;
			case "DONE":
				done++;
				break;
			}
		}
		//

		var data = google.visualization.arrayToDataTable([
				[ 'Issue state', 'Amount' ], [ 'TO DO', toDo ],
				[ 'IN PROGRESS', inProgress ], [ 'DONE', done ] ]);

		var options = {
			title : projectName + ' issues',
			'width' : 600,
			'height' : 300,
			'pieSliceText' : 'none'
		};

		var chart = new google.visualization.PieChart(document
				.getElementById('project_chart'));

		chart.draw(data, options);
	}
</script>

</head>
<body>
	<div class="dropdown_content" id="project_chart"
		style="width: 100%; position: relative;"></div>
		
	<c:import url="Navigation.jsp" />
	
	
	<!-- error/success message container -->
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
	<!-- /container -->

	<!-- hidden input to hold session attribute -->
	<input id='projectSprintIssuesMap' type="hidden"
		value='${projectSprintIssuesMap}' />
	<input id='projectSprintsMap' type="hidden"
		value='${projectSprintsMap}' />
	<input id='currentUser' type="hidden" value='${userJSON}' />
	<!-- /hidden -->
	
	<!-- Page Content -->
	<div class="container">
	
		<!-- hidden delete sprint form -->
		<form style="display: none;" action="./deletesprint" method="GET">
			<input name="sprint_id" type="text" id="delete_sprint_id" /> <input
				type="submit" id="delete_sprint_button">
		</form>
		<!-- /hidden -->

		<div class="row">
			<button id="project_progress_button"
				style="position: inline; top: 0px; float: right;" class="dropbtn"
				onclick="toggleShowDiv('#project_chart')">View project
				progress</button>
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