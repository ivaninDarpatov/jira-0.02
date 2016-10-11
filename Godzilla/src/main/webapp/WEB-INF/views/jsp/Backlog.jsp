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
	<title>Backlog</title>
	
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
		
	<!-- page-specific javascript -->
	<script src="js/backlog.js"></script>

<script>
//temporary functions (to be moved to js file)
function loadProjectSprints (projectName) {
	//get map object from session JSON
	var map = JSON.parse(document.getElementById("projectSprintIssuesMap").value);
	var sprints = map[projectName];
	
	$('#project_name').empty();
	$('#project_name').append(projectName);
	$('#sprints_container').empty();
	$('#issues_container').empty();
	$('#free_issues').empty();
	for (var sprintName in sprints) {
		var issues = sprints[sprintName];
		if (sprintName.localeCompare('-') != 0) {
			var issuesContainerId = addSprintBacklog('#sprints_container', sprintName);
			for (var i = 0; i < issues.length; i++) {
				addIssueBacklog('#' + issuesContainerId + '', issues[i]);
			}
		} else {
			for (var i = 0; i < issues.length; i++) {
				$('#free_issues').empty();
				$('#free_issues').append('Issues');
				addIssueBacklog('#issues_container', issues[i]);
			}
		}
	}
}
</script>

</head>
<body>

<!-- BEGINNING OF 'ONE-FOR-ALL' PART -->

<!-- Create Issue popUp  -->
<!----------------------------------------------------------------------------------------------------------------------------------------  -->
	<div style="display: none;" id="dialog" title="Create Issue">
		<div id="container" class="ltr">
			<h1 id="logo">
				<a href="./homepage" title="Powered by Godzilla">Godzilla</a>
			</h1>

			<form id="form" name="form" class="wufoo topLabel page1"
				accept-charset="UTF-8" autocomplete="off"
				enctype="multipart/form-data" action="./homepage" method="POST">
				
				<header id="header" class="info">
					<h2>Issue Tracking</h2>
					<div></div>
				</header>
				<ul>
					<li id="fo2li1" class="notranslate      ">
						<label class="desc" id="title1" for="Field1"> Summary 
							<span id="req_1" class="req">*</span>
						</label>
							<div>
								<input id="summary" name="summary" type="text"
									class="field text medium" value="" maxlength="255" tabindex="1"
									onkeyup="handleInput(this); " onchange="handleInput(this);"
									required />
							</div>
					</li>

					<li id="fo2li105" class="leftHalf      ">
						<label class="desc notranslate" id="title105" for="Field105">
							Issue type 
						</label>
						<div>
							<select id="issue_type" name="issue_type"
								class="field select medium" onclick="handleInput(this);"
								onkeyup="handleInput(this);" tabindex="2">
								<option value="task" selected="selected">
									<span class="notranslate">Task</span>
								</option>
								<option value="bug">
									<span class="notranslate">Bug</span>
								</option>
								<option value="story">
									<span class="notranslate">Story</span>
								<option value="epic">
									<span class="notranslate">Epic</span>
								</option>
							</select>
						</div>
					</li>

					<li id="fo2li105" class="leftHalf      ">
						<label class="desc notranslate" id="title105" for="Field105">
							Project 
						</label>
						<div>
							<select id="project" name="project" class="field select medium"
								onclick="handleInput(this);" onkeyup="handleInput(this);"
								tabindex="3">
								<c:forEach items="${companyProjects}" var="project">
									<option value="${project}">${project.name}</option>
								</c:forEach>
							</select>
						</div>
					</li>

					<li id="fo2li115" class="leftHalf      ">
						<label class="desc notranslate" id="title115" for="Field115">
							Priority
						</label>
						<div>
							<select id="priority" name="priority" class="field select medium"
								onclick="handleInput(this);" onkeyup="handleInput(this);"
								tabindex="4">
								<option value="verry_low" selected="selected">
									<span class="notranslate">Very Low</span>
								</option>
								<option value="low">
									<span class="notranslate">Low</span>
								</option>
								<option value="medium">
									<span class="notranslate">Medium</span>
								</option>
								<option value="high">
									<span class="notranslate">High</span>
								</option>
								<option value="verry high">
									<span class="notranslate">Very High</span>
								</option>
							</select>
						</div>
					</li>

					<li id="fo2li106" class="rightHalf      ">
						<label class="desc notranslate" id="title106" for="Field106">
							Status 
						</label>
						<div>
							<select id="status" name="status" class="field select medium"
								onclick="handleInput(this);" onkeyup="handleInput(this);"
								tabindex="5">
								<option value="to do" selected="selected">
									<span class="notranslate">TO DO</span>
								</option>
								<option value="in progress">
									<span class="notranslate">IN PROGRESS</span>
								</option>
								<option value="done">
									<span class="notranslate">DONE</span>
								</option>
							</select>
						</div>
					</li>

					<li id="fo2li112" class="notranslate      ">
						<label class="desc" id="title112" for="Field112"> 
							Description 
						</label>
						<div>
							<textarea id="description" name="description"
								class="field textarea medium" spellcheck="true" rows="10"
								cols="50" tabindex="6" onkeyup="handleInput(this); "
								onchange="handleInput(this);">
							</textarea>
						</div>
					</li>


					<li id="fo2li114" class="      ">
						<label class="desc notranslate" id="title114" for="Field114">
							Linked Issues 
						</label>
						<div>
							<select id="linked_issues" name="linked_issues"
								class="field select medium" onclick="handleInput(this);"
								onkeyup="handleInput(this);" tabindex="7">
								<c:forEach items="${companyProjects}" var="project">
									<c:forEach items="${project.issues}" var="issue">
										<option value="${issue}">${issue.summary}</option>
									</c:forEach>
								</c:forEach>
							</select>
						</div>
					</li>

					<li id="fo2li114" class="      ">
						<label class="desc notranslate" id="title114" for="Field114">
							Link Type 
						</label>
						<div>
							<select id="link_type" name="link_type"
								class="field select medium" onclick="handleInput(this);"
								onkeyup="handleInput(this);" tabindex="8">

								<option value="IS BLOCKED BY">IS BLOCKED BY</option>
								<option value="BLOCKS">BLOCKS</option>
								<option value="IS CAUSED BY">IS CAUSED BY</option>
								<option value="CAUSES">CAUSES</option>
								<option value="IS CLONED BY">IS CLONED BY</option>
								<option value="CLONES">CLONES</option>
								<option value="IS DUPLICATED BY">IS DUPLICATED BY</option>
								<option value="DUPLICATES">DUPLICATES</option>
								<option value="RELATES TO">RELATES TO</option>
							</select>
						</div>
					</li>


					<li id="fo2li102" class="      ">
						<label class="desc notranslate" id="title102" for="Field102">
							Assign to 
						</label>
						<div>
							<select id="assignee" name="assignee" class="field select medium"
								onclick="handleInput(this);" onkeyup="handleInput(this);"
								tabindex="9">
								<c:forEach items="${companyUsers}" var="user">
									<option value="${user}">${user.email}</option>
								</c:forEach>
							</select>
						</div>
					</li>

					<li class="buttons ">
						<div>
							<input type="hidden" name="currentPage" id="currentPage" /> 
							<input id="saveForm" name="saveForm" class="btTxt submit" type="submit" value="Submit" />
						</div>
					</li>

				</ul>
			</form>
		</div>
	</div>


<!----------------------------------------------------------------------------------------------------------------------------------------  -->

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
								<li>
									<a href='#'>Create Project</a>
								</li>

								<c:forEach items="${companyProjects}" var="project">
									<br>
									<li>
										<a href='#' onclick="loadProjectSprints('${project.name}')">${project.name}</a>
									</li>
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
						<a href="#">Board</a>
					</li>
					<li>
						<a href="#">User Panel</a>
					</li>
					<li>
						<a href='#' id='profile_button' class='dropbtn'
							onclick="toggleShowDiv('#profile_menu')">
							Profile
						</a>
						<div id='profile_menu' class='dropdown_content'>
							<ul id='profile_menu_ul' class="nav navbar-nav">
								<li>
									<a href='#'> 
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
<!-- END OF 'ONE-FOR-ALL' PART -->
<!-- hidden input to hold session attribute -->
<input id='projectSprintIssuesMap' type="hidden" value='${projectSprintIssuesMap}'/>
	<!-- Page Content -->
	<div class="container">

		<div class="row">
			<h2 id="project_name"></h2>
			<!-- project's issues column -->
<!-- LEFT SIDE -->
			<div class="col-md-8">
				<hr>
				<div id='sprints_container'>

				</div>

				<hr>

				<h4 id="free_issues"></h4>
				<div id='issues_container'>

				</div>
			</div>
<!-- /LEFT SIDE -->

<!-- RIGHT SIDE -->
			<!-- User Sidebar Column -->
			<div class="col-md-4">

				<!-- Issue Information Well -->
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