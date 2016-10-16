/*! jQuery v1.11.1 | (c) 2005, 2014 jQuery Foundation, Inc. | jquery.org/license */

//deletes project
function deleteProject(projectId) {
	if (confirm("Are you sure you want to delete this project?")) {

		document.getElementById("delete_project_id").value = projectId;
		$("#delete_project_button").trigger("click");
	}
}

// load assigned and reported issues for current user in selected project
function loadIssues(projectName, s_assignedIssues, s_reportedIssues) {
	var assignedB = false;
	var reportedB = false;
	// load project name
	$("h2#project_name").empty();
	$("h2#project_name").append(projectName);

	$("div#assigned_issues").empty();
	$("div#reported_issues").empty();

	// load assigned issues
	var jsonStringA = JSON.stringify(s_assignedIssues);
	var assignedIssues = JSON.parse(jsonStringA);
	if (assignedIssues[projectName]) {
		var assignedCount = assignedIssues[projectName].length;
		for (var i = 0; i < assignedCount; i++) {
			addIssue("#assigned_issues", assignedIssues[projectName][i]);
		}
		
		assignedB = true;
	}

	// load reported issues
	var jsonStringR = JSON.stringify(s_reportedIssues);
	var reportedIssues = JSON.parse(jsonStringR);
	if (reportedIssues[projectName]) {
		var reportedCount = reportedIssues[projectName].length;
		for (var i = 0; i < reportedCount; i++) {
			addIssue("#reported_issues", reportedIssues[projectName][i]);
		}
		reportedB = true;
	}

	if (assignedB && reportedB) {
		drawChart(projectName, s_assignedIssues, s_reportedIssues);
	} else {
		$("#assigned_chart").empty();
		$("#reported_chart").empty();
	}
}

// get number of assigned/reported issues for current user in selected project
function showDigit(projectName, issues) {
	var issuesObject = JSON.parse(issues);
	var issuesCount = issuesObject[projectName].length;

	return issuesCount;
}

// toggle show/hide issue information in 'assigned and reported issues for
// current user in selected project'
function toggleShowParagraph(caller) {
	var callerId = caller.id;
	var idNumberBeginIndex = callerId.lastIndexOf("_") + 1;
	var paragraphId;

	if (callerId.includes("assigned")) {
		paragraphId = "assigned_issue_";
	}

	if (callerId.includes("reported")) {
		paragraphId = "reported_issue_";
	}

	paragraphId = paragraphId + callerId.substring(idNumberBeginIndex);
	var jqueryParagraphId = "#" + paragraphId;
	var paragraph = document.getElementById(paragraphId);

	// <!-- jquery -->
	$(jqueryParagraphId).toggle(500);
}

// add issue to 'assigned and reported issues for current user in selected
// project'
function addIssue(caller, issue) {
	var container = $(caller);
	var issueType = caller.substring(1, 9);
	var issueNumber = document.querySelectorAll('div.' + issueType
			+ '_issue_box').length + 1;

	// issue values
	var name = issue.name;
	var type = issue.type;
	var summary = issue.summary;
	var state = issue.state;
	state = state.replace("_", " ")
	var priority = issue.priority;
	priority = priority.replace("_", " ");

	var companyProjects = document.getElementById('companyProjects').value;
	var projects = JSON.parse(companyProjects);
	var project;

	for (var i = 0; i < projects.length; i++) {

		var projectIssues = projects[i].issues;

		for (var j = 0; j < projectIssues.length; j++) {

			if (projectIssues[j].id == issue.id) {
				project = projects[i].name;
				break;
			}
		}
	}

	var companyUsers = document.getElementById('companyUsersJSON').value;
	var users = JSON.parse(companyUsers);
	var reporter;
	var assignee;

	for (var userCount = 0; userCount < users.length; userCount++) {

		var userIssuesR = users[userCount].issuesReportedBy;

		for (var issueCount = 0; issueCount < userIssuesR.length; issueCount++) {

			if (userIssuesR[issueCount].id == issue.id) {
				reporter = users[userCount].email;
				break;
			}
		}

		var userIssuesA = users[userCount].issuesAssignedTo;

		for (var issueCount = 0; issueCount < userIssuesA.length; issueCount++) {

			if (userIssuesA[issueCount].id == issue.id) {
				assignee = users[userCount].email;
				break;
			}
		}
	}

	var description = issue.description;
	var dateCreated = issue.dateCreated.time.hour + ':'
			+ issue.dateCreated.time.minute + ' ' + issue.dateCreated.date.day
			+ '/' + issue.dateCreated.date.month + '/'
			+ issue.dateCreated.date.year;
	var dateLastModified = issue.dateLastModified.time.hour + ':'
			+ issue.dateLastModified.time.minute + ' '
			+ issue.dateLastModified.date.day + '/'
			+ issue.dateLastModified.date.month + '/'
			+ issue.dateLastModified.date.year;

	var issueBox = document.createElement("div");
	issueBox.setAttribute("class", issueType + "_issue_box");
	issueBox.innerHTML = "<h4 class='issue_name'>"
			+ "<a class='dropbtn' id='"
			+ issueType
			+ "_issue_name_"
			+ issueNumber
			+ "' onclick='toggleShowParagraph(this)'>"
			+ name
			+ "</a>"
			+ "</h4>"
			+ "<p id='"
			+ issueType
			+ "_issue_"
			+ issueNumber
			+ "' class='issue_information'>"
			+ "Issue type: "
			+ type
			+ " <br> "
			+ "Summary: "
			+ summary
			+ " <br> "
			+ "Project: "
			+ project
			+ " <br>"
			+ "Reporter: "
			+ reporter
			+ " <br> "
			+ "Assignee: "
			+ assignee
			+ " <br> "
			+ "State: "
			+ state
			+ " <br> "
			+ "Priority: "
			+ priority
			+ " <br> "
			+ "Description: "
			+ description
			+ " <br>"
			+ "Created: "
			+ dateCreated
			+ " <br> "
			+ "Last modified: "
			+ dateLastModified + " <br>" + "</p>";
	container.append(issueBox);
}

