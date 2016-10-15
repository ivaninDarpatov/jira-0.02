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

<!-- page specific javascript -->
<script src="js/filters.js"></script>
</head>
<body>
	<input id="filterResult" type="hidden" value='${filterResultIssues}' />
	<input id='projectSprintsMap' type="hidden"
		value='${projectSprintsMap}' />
	<c:import url="Navigation.jsp" />
	<!-- Page Content -->
	<div class="container">

		<div class="row">

			<c:set var="succeed" value="${sessionScope.succeed}"></c:set>
			<c:set var="succeedLengh" value="${fn:length(succeed)}" />
			<c:remove var="succeed" scope="session" />
			<div id="errors_container">
				<c:if test="${(succeedLengh) gt 0}">
					<div id="tag1" class="tag2"
						style="text-align: center; color: green; font-family: 'Arial Black';"
						id="errors">
						<a class="closeButton" onclick="closeDiv('#tag1')"></a> <b>${(succeed)}</b>
					</div>
				</c:if>
			</div>


			<h2 id="project_name"></h2>
			<!-- search results column -->
			<!-- LEFT SIDE -->
			<div class="col-md-8">
				<div id="search_result">
					<div class="issue_box">
						<!-- TODO with javascript -->
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
						<input type="hidden" value="${company.name}" name="company_name">
						<table>
							<tr>
								<th><label>Issue state</label></th>
								<th><select name="issue_state">
										<option selected>NONE</option>
										<option>TO DO</option>
										<option>IN PROGRESS</option>
										<option>DONE</option>
								</select></th>
							</tr>
							<tr>
								<th><label>In Project</label></th>
								<th><select name="project_name"
									onChange='loadSprintsInSelect("#sprints_select", value)'>
										<option selected>NONE</option>
										<c:forEach items="${companyProjects}" var="project"
											varStatus="loop">
											<option value='${project.name}'
												id="project_name_${loop.index}">${project.name}</option>
										</c:forEach>
								</select></th>
							</tr>
							<tr>
								<th><label>In sprint</label></th>
								<th><select id="sprints_select" name="sprint_name">

								</select></th>
							</tr>
							<tr>
								<th><label>Reported by</label></th>
								<th><select name="reporter">
										<option selected>NONE</option>
										<c:forEach items="${companyUsers}" var="user">
											<option>${user.email}</option>
										</c:forEach>
								</select></th>
							</tr>
							<tr>
								<th><label>Assigned to</label></th>
								<th><select name="assignee">
										<option selected>NONE</option>
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