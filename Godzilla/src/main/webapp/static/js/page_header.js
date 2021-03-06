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

//place select options (sprints in project)
function loadSprintsInSelect(target,projectName) {
	//target must be select element
	var map = document.getElementById("projectSprintsMap").value;
	var mapObj = JSON.parse(map);
	var sprints = mapObj[projectName];
	$(target).empty();
	
	var defaultSprint = $('<option></option>');
	defaultSprint.attr('value', 'NONE');
	defaultSprint.append('NONE');
	
	$(target).append(defaultSprint);
	for (var i = 0; i < sprints.length; i++) {
		var option = $("<option></option>");
		option.append(sprints[i].name);
		$(target).append(option);
	}
}

// toggle show/hide content by ID (caller)
function toggleShowDiv(caller) {
	// <!-- jquery -->
	if (caller.charAt(0) == '#') {
		$(caller).toggle(800);
	}
	
	if (caller.charAt(0) == '.') {
		var charts = document.getElementsByClassName(caller.substring(1));
		for (var i = 0; i < charts.length; i++) {
			var chartId = '#' + charts[i].id;
			$(chartId).toggle(800);
		}
	}
}

// close dropdowns and dialogs on click-out
function hasParentClass(child, parent) {
	var node = child;
	while (node != document) {
		if (JSON.stringify(node.className) != "{}"){
	 		if (node.className.includes(parent)) {
				return true;
			}
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
				$('#' + openDropdownId).hide(800);
			}
		}
	}
	
	if (!hasParentClass(event.target, "dialog_box") 
			&& !hasParentClass(event.target, "dialog_opener")
			&& !$(event.target).is('select')) {
		
		var dialogs = document.getElementsByClassName('dialog_box');
		var i;
		for (i = 0; i < dialogs.length; i++) {
			var openDialogId = dialogs[i].id;

			$("#" + openDialogId).dialog({
				width : 535.6,
			});
			
			if ($('#' + openDialogId).dialog('isOpen')) {
				$('#' + openDialogId).dialog('close');
			}
		}
	}
}

// load selected issue's information in the right-side box
function openIssueInformation(issue) {
	console.log(issue);
	var issueInformation = $('<div></div>');
	// issue values
	var name = issue.name;
	var type = issue.type;
	var summary = issue.summary;
	var state = issue.state;
	state = state.replace("_"," ")
	var priority = issue.priority;
	priority = priority.replace("_"," ");
	var companyProjects = document.getElementById('companyProjects').value;
	var projects = JSON.parse(companyProjects);
	var project;
	
	//get issue project
	for (var i = 0; i < projects.length; i++) {
		
		var projectIssues = projects[i].issues;
		
		for (var j = 0; j < projectIssues.length; j++) {
			
			if (projectIssues[j].id == issue.id) {
				project = projects[i].name;
				break;
			}
		}
	}
	//get issue reporter and assignee
	var companyUsers = document.getElementById('companyUsersJSON').value;
	var users = JSON.parse(companyUsers);
	var reporter;
	var assignee;
	
	for(var userCount = 0; userCount < users.length; userCount++){
		
		var userIssuesR = users[userCount].issuesReportedBy;
		
		for(var issueCount = 0; issueCount < userIssuesR.length; issueCount++){
			
			if(userIssuesR[issueCount].id == issue.id){
				reporter = users[userCount].email;
				break;
			}
		}
		
		var userIssuesA = users[userCount].issuesAssignedTo;
		
		for(var issueCount = 0; issueCount < userIssuesA.length; issueCount++){
			
			if(userIssuesA[issueCount].id == issue.id){
				assignee = users[userCount].email;
				break;
			}
		}
	}
	//

	

	
	var description = issue.description;
	var dateCreated = issue.dateCreated.time.hour + ':' + issue.dateCreated.time.minute + ' '
						+ issue.dateCreated.date.day + '/' + issue.dateCreated.date.month + '/'
						+ issue.dateCreated.date.year;
	var dateLastModified = issue.dateLastModified.time.hour + ':' + issue.dateLastModified.time.minute + ' '
							+ issue.dateLastModified.date.day + '/' + issue.dateLastModified.date.month + '/'
							+ issue.dateLastModified.date.year;
	

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
	
	var editIssue = $('<button></button>');
	editIssue.attr('id','edit_issue_button');
	editIssue.attr('class','dialog_opener');
	var deleteIssue = $('<button></button>');
	var issueString = JSON.stringify(issue);
	deleteIssue.attr('onclick', 'deleteIssue(' + issueString + ')');
	
	editIssue.attr('onclick', 'editIssue(' + issueString + ')');
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
	var issueState = $('<p></p>');
	issueState.append('State: ' + state);
	var issuePriority = $('<p></p>');
	issuePriority.append('Priority: ' + priority);
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
	paragraphsDiv.append(issueState);
	paragraphsDiv.append(issuePriority);
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
		width : 535.6,
	});
	
	$(function() {
		$("#sprint_dialog").dialog('open');
	});
}

// open 'create project' dialog box
$(function() {
	$("#project_dialog").dialog({
		autoOpen : false,
		width : 535.6,
	});

	$("#create_project").click(function() {
		$("#project_dialog").dialog('open');
	});
})

//edit issue
function editIssue(issue){
	var companyProjects = document.getElementById('companyProjects').value;
	var projects = JSON.parse(companyProjects);
	var projectName;
	
	
	
	for (var i = 0; i < projects.length; i++) {
		var projectIssues = projects[i].issues;
		for (var j = 0; j < projectIssues.length; j++) {
			if (projectIssues[j].id == issue.id) {
				projectName = projects[i].name;
				break;
			}
		}
	}
	
	loadSprintsInSelect('#edit_issue_sprints', projectName);
	
	var issueId = issue.id;
	var projectSummary = issue.summary;
	var issuePriority = issue.priority;
	var issueStatus = issue.state;
	var issueDescription = issue.description;
	
	
	$("#hiddenId").val(issueId);
	$("#edit_summary").val(projectSummary);
	$('#edit_priority').val(issuePriority);
	$("#edit_status").val(issueStatus);
	$("#edit_description").val(issueDescription);
$(function() {
	console.log("starting");
	$("#edit_dialog").dialog({
		autoOpen : false,
		width : 535.6,
	});
	

		$("#edit_dialog").dialog('open');
		console.log("after open");
})}


// open 'create issue' dialog box
$(function() {
	$("#issue_dialog").dialog({
		autoOpen : false,
		width : 535.6,
	});

	$("#create_issue").click(function() {
		$("#issue_dialog").dialog('open');
	});
})

