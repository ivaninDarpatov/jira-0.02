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
<title>Create Issue dialog</title>

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
	//remove textarea preset first row
	$(document).ready(function() {
		$('textarea').val("");
	});
</script>
</head>
<body>
	<div style="display: none;" id="issue_dialog" class="dialog_box"
		title="Create Issue">
		<div id="container" class="ltr">
			<h1 id="logo">
				<a href="./homepage" title="Powered by Godzilla">Godzilla</a>
			</h1>

			<form action="./createIssue" method="POST">

				<header id="header" class="info">
				<h2>Create Issue</h2>
				<div></div>
				</header>
				<ul>
					<li id="fo2li1" class="notranslate      "><label class="desc"
						id="title1" for="Field1"> Summary <span id="req_1"
							class="req">*</span>
					</label>
						<div>
							<input id="summary" name="summary" type="text"
								class="field text medium" value="" maxlength="255" tabindex="1"
								required />
						</div></li>

					<li id="fo2li105" class="leftHalf      "><label
						class="desc notranslate" id="title105" for="Field105">
							Issue type </label>
						<div>
							<select id="issue_type" name="issue_type"
								class="field select medium" tabindex="2">
								<option value="task" selected="selected"><span
										class="notranslate">Task</span>
								</option>
								<option value="bug"><span class="notranslate">Bug</span>
								</option>
								<option value="story"><span class="notranslate">Story</span>
								<option value="epic"><span class="notranslate">Epic</span>
								</option>
							</select>
						</div></li>

						<!-- project -->
					<li id="fo2li105" class="leftHalf      "><label
						class="desc notranslate" id="title105" for="Field105">
							Project </label>
						<div>
							<select onChange="loadSprintsInSelect('#project_sprint', value)" id="project" name="project" class="field select medium"
								tabindex="3">
								<c:forEach items="${companyProjects}" var="project">
									<option value="${project.name}">${project.name}</option>
								</c:forEach>
							</select>
						</div></li>
						
						
						
						<!-- sprint -->
						<li id="fo2li105" class="leftHalf      "><label
						class="desc notranslate" id="title105" for="Field105">
							Sprint </label>
						<div>
							<select id="project_sprint" name="sprint" class="field select medium"
								tabindex="3">
								<option value="NONE" selected>NONE</option>
							</select>
						</div></li>

					<li id="fo2li115" class="leftHalf      "><label
						class="desc notranslate" id="title115" for="Field115">
							Priority </label>
						<div>
							<select id="priority" name="priority" class="field select medium"
								tabindex="4">
								<option value="VERY_LOW" selected="selected">VERY LOW
								</option>
								<option value="LOW">LOW
								</option>
								<option value="MEDIUM">MEDIUM
								</option>
								<option value="HIGH">HIGH
								</option>
								<option value="VERY_HIGH">VERY HIGH
								</option>
							</select>
						</div></li>

					<li id="fo2li106" class="rightHalf      "><label
						class="desc notranslate" id="title106" for="Field106">
							Status </label>
						<div>
							<select id="status" name="status" class="field select medium"
								tabindex="5">
								<option value="TO_DO" selected="selected">TO DO
								</option>
								<option value="IN_PROGRESS">IN PROGRESS
								</option>
								<option value="DONE">DONE
								</option>
							</select>
						</div></li>

					<li id="fo2li112" class="notranslate      "><label
						class="desc" id="title112" for="Field112"> Description </label>
						<div>
							<textarea id="description" name="description"
								class="field textarea medium" spellcheck="true" rows="10">
							</textarea>
						</div></li>

					<li id="fo2li102" class="      "><label
						class="desc notranslate" id="title102" for="Field102">
							Assign to </label>
						<div>
							<select id="assignee" name="assignee" class="field select medium"
								tabindex="9">
								<c:forEach items="${companyUsers}" var="user">
									<c:if test="${user.isTester == false}">
										<option value="${user.email}">${user.email}</option>
									</c:if>
								</c:forEach>
							</select>
						</div></li>

					<li class="buttons ">
						<div>
							<input type="hidden" name="currentPage" id="currentPage" /> <input
								id="saveForm" name="saveForm" class="btTxt submit" type="submit"
								value="Submit" />
						</div>
					</li>

				</ul>
			</form>
		</div>
	</div>
</body>
</html>
