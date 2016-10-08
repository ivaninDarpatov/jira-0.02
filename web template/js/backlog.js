/*! jQuery v1.11.1 | (c) 2005, 2014 jQuery Foundation, Inc. | jquery.org/license */

function addIssueBacklog (caller) {
	var container = $(caller);
	var issueNumber = document.querySelectorAll('div.backlog_issue_box').length + 1;
	//issue info
	var name = 'ISSUE-' + issueNumber;
	var summary = '\tSUMMARY-' + issueNumber;
	//
	var issueBox = document.createElement('div');
	issueBox.setAttribute('class', 'backlog_issue_box');
	issueBox.setAttribute('id', 'issue_' + issueNumber);
	var bold = document.createElement('b');
	var addIssue = document.createElement('a');
	addIssue.setAttribute('href', '#');
	addIssue.setAttribute('onclick', 'openIssueInformation(\'' + name + '\')');
	addIssue.innerHTML = name;
	bold.append(addIssue);
	issueBox.append(bold);
	issueBox.append(summary);
	container.append(issueBox);
}

function openIssueInformation(issueName) {
	var issueInformation = document.createElement('div');
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
	issueInformation.setAttribute('id', issueName.toLowerCase());
	var issueName = document.createElement('h4');
	issueName.innerHTML = name;
	
	var issueType = document.createElement('p');
	issueType.innerHTML = 'Type: ' + type;
	var issueSummary = document.createElement('p');
	issueSummary.innerHTML = 'Summary: ' + summary;
	var issueProject = document.createElement('p');
	issueProject.innerHTML = 'Project: ' + project;
	var issueReporter = document.createElement('p');
	issueReporter.innerHTML = 'Reporter: ' + reporter;
	var issueAssignee = document.createElement('p');
	issueAssignee.innerHTML = 'Assignee: ' + assignee;
	var issueDescription = document.createElement('p');
	issueDescription.innerHTML = 'Description: ' + description;
	var issueCreated = document.createElement('p');
	issueCreated.innerHTML = 'Created: ' + dateCreated;
	var issueLastModified = document.createElement('p');
	issueLastModified.innerHTML = 'Last modified: ' + dateLastModified;

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
	var newSprint = document.createElement('div');
	newSprint.setAttribute('id', 'sprint_' + sprintNumber);
	newSprint.setAttribute('class', 'sprints');
	//sprint info
	var name = 'SPRINT-' + sprintNumber;
	//
	var sprintName = document.createElement('h4');
	sprintName.innerHTML = name;
	var issuesContainer = document.createElement('div');
	issuesContainer.setAttribute('id', 'sprint_' + sprintNumber + '_issues');
	var addIssue = document.createElement('a');
	addIssue.setAttribute('href', '#');
	addIssue.setAttribute('onclick', 'addIssueBacklog(\'#' + issuesContainer.id + '\')');
	addIssue.innerHTML = 'add issue';
	
	issuesContainer.append(addIssue);
	newSprint.append(sprintName);
	newSprint.append(issuesContainer);
	
	container.append(newSprint);
	
}