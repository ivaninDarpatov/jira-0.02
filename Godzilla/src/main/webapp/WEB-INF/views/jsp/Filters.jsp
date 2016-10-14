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
<title>Filters</title>

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

<script>
function loadSprintsFilters(projectName) {
	var map = document.getElementById("projectSprintsMap").value;
	var mapObj = JSON.parse(map);
	var sprints = mapObj[projectName];
	$("#sprints_select").empty();
	for (var i = 0; i < sprints.length; i++) {
		var option = $("<option></option>");
		option.append(sprints[i].name);
		$("#sprints_select").append(option);
	}
}
</script>
</head>
<body>
	<input id='projectSprintsMap' type="hidden"
		value='${projectSprintsMap}' />
	<c:import url="Navigation.jsp" />
	<!-- Page Content -->
	<div class="container">

		<div class="row">
			<h2 id="project_name"></h2>
			<!-- search results column -->
			<!-- LEFT SIDE -->
			<div class="col-md-8">
				<div id="search_result">
					<div class="issue_box">
					<!-- TODO with javascript -->
						<c:forEach items="${filterResultIssues}" var="issue" varStatus="loop">
							<div class="filter_issue_box">
								<b class="issue_name">
									<a href="#" id="issue_${issue.id}" onclick='openIssueInformation()'>
										${issue.name}
									</a>
								</b>
								&nbsp
								${issue.summary}
							</div>
						</c:forEach>
					</div>
				</div>
			</div>
			<!-- /LEFT SIDE -->

			<!-- RIGHT SIDE -->
			<!-- Search Well Column -->
			<div class="col-md-4">

				<!-- Issue Information Well -->
				<div id='filter_search' class="well">
					<form action="./filters" method="POST">
						<table>
							<tr>
								<th><label>Issue state</label></th>
								<th><select name="issue_state">
										<option selected="selected">TO DO</option>
										<option>TO DO</option>
										<option>IN PROGRESS</option>
										<option>DONE</option>
								</select></th>
							</tr>
							<tr>
								<th><label>In Project</label></th>
								<th><select name="project_name">
										<option selected="selected">Project 1</option>
										<c:forEach items="${companyProjects}" var="project" varStatus="loop">
											<option onclick="loadSprintsFilters('${project.name}')" id="project_name_${loop.index}">${project.name}</option>
										</c:forEach>
								</select></th>
							</tr>
							<tr>
								<th><label>In sprint</label></th>
								<th><select id="sprints_select" name="sprint_name">
										<option selected="selected">Sprint 1</option>
								</select></th>
							</tr>
							<tr>
								<th><label>Reported by</label></th>
								<th><select name="reporter">
										<option selected="selected">user_1@abv.bg</option>
									<c:forEach items="${companyUsers}" var="user">
										<option>${user.email}</option>
									</c:forEach>
								</select></th>
							</tr>
							<tr>
								<th><label>Assigned to</label></th>
								<th><select name="assignee">
										<option selected="selected">user_1@abv.bg</option>
									<c:forEach items="${companyUsers}" var="user">
										<option>${user.email}</option>
									</c:forEach>
								</select></th>
							</tr>
						</table>
						<input type="submit" value="Search" />
					</form>
				</div>

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