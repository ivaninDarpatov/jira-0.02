//pick a command handler when project is selected from projects menu
function selectProjectNameDispatcher(path, projectName) {
	// get opened page's name
	var pageNameStart = path.lastIndexOf('/') + 1;
	var pageNameEnd = path.lastIndexOf('.jsp');
	var pageName = path.substring(pageNameStart, pageNameEnd);

	switch (pageName) {
	case 'Board':
		loadProjectSprintsBoard(projectName);
		break;
	case 'Backlog':
		loadProjectSprintsBacklog(projectName);
		break;
	case 'HomePage':
		var s_assignedIssues = document.getElementById("assignedIssues").value;
		var s_reportedIssues = document.getElementById("reportedIssues").value;
		var assignedIssues = JSON.parse(s_assignedIssues);
		var reportedIssues = JSON.parse(s_reportedIssues);
		loadIssues(projectName, assignedIssues, reportedIssues);
	case 'ProfilePage':
		return;
	}
}

// toggle show/hide content by ID (caller)
function toggleShowDiv(caller) {
	// <!-- jquery -->
	$(caller).toggle(200);
}

// close dropdowns and dialogs on click-out
function hasParentClass(child, parent) {
	var node = child;
	while (node != document) {
		if (node.className.includes(parent)) {
			return true;
		}
		node = node.parentNode;
	}
	
	return false;
}
window.onclick = function(event) {
	if (!hasParentClass(event.target, 'dropbtn')) {

		var dropdowns = document.getElementsByClassName('dropdown_content');
		var i;
		for (i = 0; i < dropdowns.length; i++) {
			var openDropdownId = dropdowns[i].id;

			if ($('#' + openDropdownId).is(':visible')) {
				$('#' + openDropdownId).hide(200);
			}
		}
	}
	
	if (!hasParentClass(event.target, "dialog_box") && !hasParentClass(event.target, "dialog_opener")) {
		
		var dialogs = document.getElementsByClassName('dialog_box');
		var i;
		for (i = 0; i < dialogs.length; i++) {
			var openDialogId = dialogs[i].id;

			$("#" + openDialogId).dialog({
				width : 535.6
			});
			
			if ($('#' + openDialogId).dialog('isOpen')) {
				$('#' + openDialogId).dialog('close');
			}
		}
	}
}

// unused function
function addIssueToProject(caller, project) {
	var issueType = caller.substring(1, 9);
	var projectNumber = project.substring(project.lastIndexOf("_") + 1);
	var idToApplyChanges = 'project_' + projectNumber + '_' + issueType;
	var element = document.getElementById(idToApplyChanges);
	var elementHTML = element.innerHTML;
	var oldValue = elementHTML.substring(elementHTML.lastIndexOf(' ') + 1);
	var newValue = parseInt(oldValue) + 1;

	var result = elementHTML.replace(oldValue, newValue);
	element.innerHTML = result;
}

// load selected issue's information in the right-side box
function openIssueInformation(issue) {
	console.log(issue);
	var issueInformation = $('<div></div>');
	// issue values
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

	var issueHeader = $('<div></div>');
	issueHeader.attr('style', 'width:100%; height:60px;');
	var issueNameDiv = $('<div></div>');
	issueNameDiv.attr('style', 'float:left; width:70%; height:100%;');
	
	var editIssueDiv = $('<div></div>');
	editIssueDiv.attr('style',
			'float:left; width:15%; height:100%; text-align:right;');
	
	var deleteIssueDiv = $('<div></div>');
	deleteIssueDiv.attr('style',
			'float:left; width:15%; height:100%; text-align:right;');

	var issueName = $('<h4></h4>');
	issueName.append(name);
	
	var editIssue = $('<a></a>');
	editIssue.attr('href', '#');
	editIssue.attr('id','edit_issue_button')
	var deleteIssue = $('<a></a>');
	deleteIssue.attr('href', '#');
	var issueString = JSON.stringify(issue);
	deleteIssue.attr('onclick', 'deleteIssue(' + issueString + ')');
	
	editIssue.attr('onclick', 'editIssue()');
	editIssue.append('Edit');
	deleteIssue.attr('onclick', 'deleteIssue(' + issueString + ')');
	deleteIssue.append('Delete');

	issueNameDiv.append(issueName);
	deleteIssueDiv.append(deleteIssue);
	editIssueDiv.append(editIssue);

	var paragraphsDiv = $('<div></div>');
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

	issueHeader.append(issueNameDiv);
	issueHeader.append(deleteIssueDiv);
	issueHeader.append(editIssueDiv);
	issueInformation.append(issueHeader);

	paragraphsDiv.append(issueType);
	paragraphsDiv.append(issueSummary);
	paragraphsDiv.append(issueProject);
	paragraphsDiv.append(issueReporter);
	paragraphsDiv.append(issueAssignee);
	paragraphsDiv.append(issueDescription);
	paragraphsDiv.append(issueCreated);
	paragraphsDiv.append(issueLastModified);
	issueInformation.append(paragraphsDiv);

	var container = $('#issue_info_well');
	container.empty();
	container.append(issueInformation);
}

//deletes issue
function deleteIssue(issue) {
	var user = document.getElementById("user").value;
	user = JSON.parse(user);
	var reportedIssues = user.issuesReportedBy;
	var isIssueReportedByMe = false;
	if (user.permissions != "MANAGER") {
		for (var i = 0; i < reportedIssues.length; i++) {
			if (reportedIssues[i].id == issue.id) {
				isIssueReportedByMe = true;
				break;
			}
		}
	} else {
		isIssueReportedByMe = true;
	}
	
	if (!isIssueReportedByMe) {
		alert("You cannot delete issues you have not reported!");
		return;
	}
	
	if (!confirm('Are you sure you want to delete ' + issue.type + ' ' + issue.name + '')) {
		return;
	}
	
	var issueId = issue.id;
	document.getElementById("delete_issue_id").value = issueId;
	$("#delete_issue_button").trigger("click");
}

//open 'create sprint' dialog box
function createSprint() {
	$("#sprint_dialog").dialog({
		autoOpen : false,
		width : 535.6
	});
	
	$(function() {
		$("#sprint_dialog").dialog('open');
	});
}

// open 'create project' dialog box
$(function() {
	$("#project_dialog").dialog({
		autoOpen : false,
		width : 535.6
	});

	$("#create_project").click(function() {
		$("#project_dialog").dialog('open');
	});
})

//edit issue
function editIssue(){
$(function() {
	console.log("starting");
	$("#edit_dialog").dialog({
		autoOpen : false,
		width : 535.6
	});

		$("#edit_dialog").dialog('open');
		console.log("after open");
})}


// open 'create issue' dialog box
$(function() {
	$("#issue_dialog").dialog({
		autoOpen : false,
		width : 535.6
	});

	$("#create_issue").click(function() {
		$("#issue_dialog").dialog('open');
	});
})

