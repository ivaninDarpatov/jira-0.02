/*! jQuery v1.11.1 | (c) 2005, 2014 jQuery Foundation, Inc. | jquery.org/license */

//load sprints for selected project
function loadProjectSprintsBacklog(projectName) {
	//get map object from session JSON
	var map = JSON.parse(document.getElementById("projectSprintIssuesMap").value);
	var sprints = map[projectName];
	
	$('#issue_info_well').empty();
	$('#project_name').empty();
	$('#project_name').append(projectName);
	$('#sprints_container').empty();
	$('#issues_container').empty();
	$('#free_issues').empty();
	
	var createSprintButton = $('<button></button>');
	createSprintButton.attr('id', 'create_sprint_backlog');
	createSprintButton.attr('class', 'dialog_opener');
	createSprintButton.attr('onclick', 'createSprint()');
	createSprintButton.append("Create Sprint");
	$("#create_sprint_backlog_div").empty();
	$("#create_sprint_backlog_div").append(createSprintButton);
	
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

//add issue in selected sprint's issues container (caller)
function addIssueBacklog (caller, issue) {
	var container = $(caller);
	var issueNumber = document.querySelectorAll('div.backlog_issue_box').length + 1;
	//issue info
	var name = issue.name;
	var summary = '\t' + issue.summary;
	//
	var issueBox = $('<div></div>');
	issueBox.attr('class', 'backlog_issue_box');
	issueBox.attr('id', 'issue_' + issueNumber);
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

//add sprint in the sprints container
function addSprintBacklog (target, name) {
	var container = $(target);
	var sprintNumber = document.querySelectorAll('div.sprints').length + 1;
	var newSprint = $("<div></div>");
	newSprint.attr('id', 'sprint_' + sprintNumber);
	newSprint.attr('class', 'sprints');
	var sprintName = $('<h4></h4>');
	sprintName.append(name);
	var containerId = 'sprint_' + sprintNumber + '_issues';
	var issuesContainer = $('<div></div>');
	issuesContainer.attr('id', containerId)
	newSprint.append(sprintName);
	newSprint.append(issuesContainer);
	
	container.append(newSprint);
	
	return containerId;
}

//view JSON map as object map (test)
function viewMap (map) {
	var string = JSON.stringify(map);
	var object = JSON.parse(string);
	console.log(object);
}