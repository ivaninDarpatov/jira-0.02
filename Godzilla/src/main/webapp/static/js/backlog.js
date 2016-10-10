/*! jQuery v1.11.1 | (c) 2005, 2014 jQuery Foundation, Inc. | jquery.org/license */

function addIssueBacklog (caller) {
	var container = $(caller);
	var issueNumber = document.querySelectorAll('div.backlog_issue_box').length + 1;
	//issue info
	var name = 'ISSUE-' + issueNumber;
	var summary = '\tSUMMARY-' + issueNumber;
	//
	var issueBox = $('<div></div>');
	issueBox.attr('class', 'backlog_issue_box');
	issueBox.attr('id', 'issue_' + issueNumber);
	var bold = $('<b></b>');
	var addIssue = $('<a></a>');
	addIssue.attr('href', '#');
	addIssue.attr('onclick', 'openIssueInformation(\'' + name + '\')');
	addIssue.append(name);
	bold.append(addIssue);
	issueBox.append(bold);
	issueBox.append(summary);
	container.append(issueBox);
}

function openIssueInformation(issueName) {
	var issueInformation =$('<div></div>');
	var issueNumber = issueName.substring(issueName.lastIndexOf('-') + 1);
	//issue values
	var name = 'ISSUE-' + issueNumber;
	var type = 'task';
	var summary = 'SUMMARY-' + issueNumber;
	var project = 'PROJECT-1';
	var reporter = 'USER-1';
	var assignee = 'USER-1';
	var description = 'DESCRIPTION-' + issueNumber;
	var dateCreated = '07/10/2016';
	var dateLastModified = '07/10/2016';
	//
	issueInformation.attr('id', issueName.toLowerCase());
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

function addSprintBacklog (target) {
	var container = $(target);
	var sprintNumber = document.querySelectorAll('div.sprints').length + 1;
	var newSprint = $("<div></div>");
	newSprint.attr('id', 'sprint_' + sprintNumber);
	newSprint.attr('class', 'sprints');
	//sprint info
	var name = 'SPRINT-' + sprintNumber;
	//
	var sprintName = $('<h4></h4>');
	sprintName.append(name);
	var containerId = 'sprint_' + sprintNumber + '_issues';
	var issuesContainer = $('<div></div>');
	issuesContainer.attr('id', containerId);
	var addIssue = $('<a></a>');
	addIssue.attr('href', '#');
	addIssue.attr('onclick', 'addIssueBacklog("#' + containerId + '")');
	addIssue.append('add issue');
	
	issuesContainer.append(addIssue);
	newSprint.append(sprintName);
	newSprint.append(issuesContainer);
	
	container.append(newSprint);
	
}