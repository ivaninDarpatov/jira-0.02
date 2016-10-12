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

<script>
	function loadProjectSprints(projectName) {
		//get map object from session JSON
		var map = JSON.parse(document.getElementById("projectSprintIssuesMap").value);
		var sprints = map[projectName];
		
		$('#project_name').empty();
		$('#project_name').append(projectName);
		
		$('#project_sprints').empty();
		$('#to_do').empty();
		$('#in_progress').empty();
		$('#done').empty();
		$('#issue_info_well').empty();

		var sprintsContainer = $('#project_sprints');
		for (var sprintName in sprints) {
			var issues = sprints[sprintName];
			var issuesString = JSON.stringify(issues);
			if (sprintName.localeCompare('-') != 0) {
				var sprintNameA = $('<a></a>');
				sprintNameA.attr('href', '#');
				sprintNameA.attr('onClick', 'addSprintBoard("#opened_sprint_container", "' + sprintName + '", ' + issuesString + ')');
				sprintNameA.append(sprintName);
				sprintsContainer.append(sprintNameA);
			} 
		}
	}
	
	function addIssueBoard(caller, issue) {
		var container = $(caller);
		var issueNumber = document.querySelectorAll('div.board_issue_box').length + 1;
		var issueId = caller.substring(1) + '_' + issueNumber;

		//issue info
		var name = issue.name;
		var summary = '\t' + issue.summary;
		//

		var issueBox = $('<div></div>');
		issueBox.attr('class', 'board_issue_box');
		issueBox.attr('id', issueId);
		var bold = $('<b></b>');
		var addIssue = $('<a></a>');
		addIssue.attr('href', '#');
		var issueString = JSON.stringify(issue);
		addIssue.attr('onclick', 'openIssueInformation(' + issueString + ')');
		addIssue.append(name);
		bold.append(addIssue);
		issueBox.append(bold);
		issueBox.append(summary);
		container.append(issueBox);
	}

	function addSprintBoard(target, sprintName, issues) {
		var nameContainer = $('#sprint_name');
		nameContainer.empty();
		nameContainer.append(sprintName);
		
		$('#issue_info_well').empty();
		
		//add sprint issues in table
		for (var i = 0; i < issues.length; i++) {
			console.log(issues[i]);
			if (issues[i].state.localeCompare("TO_DO") == 0) {
				addIssueBoard("#to_do", issues[i]);
			} else {
				if (issues[i].state.localeCompare("IN_PROGRESS") == 0) {
					addIssueBoard("#in_progress", issues[i]);
				} else {
					if (issues[i].state.localeCompare("DONE") == 0) {
						addIssueBoard("#done", issues[i]);
					}
				}
			}
		}
	}
	
	function openIssueInformation(issue) {
		var issueInformation = $('<div></div>');
		//issue values
		var name = issue.name;
		var type = issue.type;
		var summary = issue.summary;
		var project = 'PROJECT-1';
		var reporter = 'USER-1';
		var assignee = 'USER-1';
		var description = issue.description;
		var dateCreated = issue.dateCreated;
		var dateLastModified = issue.dateLastModified;
		//
		issueInformation.attr('id', name.toLowerCase());
		var issueName = $('<h4></h4>');
		issueName.append(name);
		
		var issueType = $('<p></p>');
		issueType.append('Type: ' + type);
		var issueSummary = $('<p></p>');
		issueSummary.append('Summary: ' + summary);
		var issueProject = $('<p></p>');
		issueProject.append('Project: ' + project);
		var issueReporter = $('<p></p>');
		issueReporter.append('Reporter: ' + reporter);
		var issueAssignee = $('<p></p>');
		issueAssignee.append('Assignee: ' + assignee);
		var issueDescription = $('<p></p>');
		issueDescription.append('Description: ' + description);
		var issueCreated = $('<p></p>');
		issueCreated.append('Created: ' + dateCreated);
		var issueLastModified = $('<p></p>');
		issueLastModified.append('Last modified: ' + dateLastModified);

		issueInformation.append(issueName);
		
		issueInformation.append(issueType);
		issueInformation.append(issueSummary);
		issueInformation.append(issueProject);
		issueInformation.append(issueReporter);
		issueInformation.append(issueAssignee);
		issueInformation.append(issueDescription);
		issueInformation.append(issueCreated);
		issueInformation.append(issueLastModified);
		
		var container = $('#issue_info_well');
		container.empty();
		container.append(issueInformation);
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

			<form action="./homepage" method="POST">
				
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
									<option value="${project.name}">${project.name}</option>
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
										<option value="${issue.name}">${issue.name}</option>
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
									<option value="${user.email}">${user.email}</option>
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
<input id='projectSprintIssuesMap' type="hidden" value='${projectSprintIssuesMap}'/>

	<!-- Page Content -->
	<div class="container">
		<div class="row">
			
<!-- LEFT SIDE -->
			<div class="col-md-8">
				<h2 id="project_name"></h2>
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
							<td id="to_do" class="issues_container" class="sprint_table_element"></td>
							<td id="in_progress" class="issudes_container" class="sprint_table_element"></td>
							<td id="done" class="issues_container" class="sprint_table_element"></td>
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