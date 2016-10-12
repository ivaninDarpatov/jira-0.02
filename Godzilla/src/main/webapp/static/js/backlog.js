/*! jQuery v1.11.1 | (c) 2005, 2014 jQuery Foundation, Inc. | jquery.org/license */

//load sprints for selected project
function loadProjectSprints (projectName) {
	//get map object from session JSON
	var map = JSON.parse(document.getElementById("projectSprintIssuesMap").value);
	var sprints = map[projectName];
	
	$('#issue_info_well').empty();
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

//load selected issue's information in the right-side box
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