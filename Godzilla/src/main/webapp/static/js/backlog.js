/*! jQuery v1.11.1 | (c) 2005, 2014 jQuery Foundation, Inc. | jquery.org/license */

//load sprints for selected project
function loadProjectSprintsBacklog(projectName) {
	// get map JSON object from session
	var map = JSON
			.parse(document.getElementById("projectSprintIssuesMap").value);
	var map2 = JSON.parse(document.getElementById("projectSprintsMap").value);
	var sprints = map[projectName];
	var sprints2 = map2[projectName];

	$('#issue_info_well').empty();
	$('#project_name').empty();
	$('#project_name').append(projectName);
	$('#sprints_container').empty();
	$('#issues_container').empty();
	$('#free_issues').empty();
	$("#free_issues").append("Issues");

	var createSprintButton = $('<button></button>');
	createSprintButton.attr('id', 'create_sprint_backlog');
	createSprintButton.attr('class', 'dialog_opener');
	createSprintButton.attr('onclick', 'createSprint()');
	createSprintButton.append("Create Sprint");
	$("#create_sprint_backlog_div").empty();
	$("#create_sprint_backlog_div").append(createSprintButton);

	for (var i = 0; i < sprints2.length; i++) {
		var issuesContainerId;
		if (sprints2[i].isActive) {
			issuesContainerId = addSprintBacklog('#sprints_container',
					sprints2[i], projectName);
			var sprintIssues = sprints2[i].issues;

			for (var j = 0; j < sprintIssues.length; j++) {
				addIssueBacklog("#" + issuesContainerId, sprintIssues[j]);
			}
		}
	}

	for (var i = 0; i < sprints2.length; i++) {
		var issuesContainerId;
		if (sprints2[i].isActive) {
			continue;
		}

		issuesContainerId = addSprintBacklog('#sprints_container', sprints2[i],
				projectName);
		var sprintIssues = sprints2[i].issues;

		for (var j = 0; j < sprintIssues.length; j++) {
			addIssueBacklog("#" + issuesContainerId, sprintIssues[j]);
		}
	}

	var freeIssues = sprints["-"];
	for (var i = 0; i < freeIssues.length; i++) {
		addIssueBacklog("#issues_container", freeIssues[i]);
	}
}

// add issue in selected sprint's issues container (caller)
function addIssueBacklog(caller, issue) {
	var container = $(caller);
	var issueNumber = document.querySelectorAll('div.backlog_issue_box').length + 1;
	// issue info
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

//delete sprint
function deleteSprint(sprint) {
	var sprintObject = JSON.parse(sprint);
	if (confirm("Are you sure you want to delete sprint " + sprintObject.name + "?")) {
		var sprintId = sprintObject.id;
		
		document.getElementById("delete_sprint_id").value = sprintId;
		$("#delete_sprint_button").trigger("click");
	}
}

//edit sprint
function editSprint(sprint) {
	var sprintObject = JSON.parse(sprint);

	var sprintId = sprintObject.id;
	var sprintName = sprintObject.name;
	var sprintGoal = sprintObject.sprintGoal;
	
	$("#sprintId").val(sprintId);
	$("#edit_name").val(sprintName);
	$('#edit_goal').val(sprintGoal);
	
	$(function() {
		console.log("start")
		$("#edit_sprint_dialog").dialog({
			autoOpen : false,
			width : 535.6
		});
		
			$("#edit_sprint_dialog").dialog('open');
			console.log("after open")
	})
}

// add sprint in sprints container
function addSprintBacklog(target, sprint) {
	var userJSON = document.getElementById("currentUser").value;
	var user = JSON.parse(userJSON);
	
	var sprintString = JSON.stringify(sprint);
	var container = $(target);
	var sprintNumber = document.querySelectorAll('div.sprints').length + 1;
	var newSprint = $("<div></div>");
	newSprint.attr('id', 'sprint_' + sprintNumber);
	newSprint.attr('class', 'sprints');
	var sprintName = $('<h4></h4>');
	
	var sprintHeaderDiv = $('<div></div>');
	sprintHeaderDiv.attr('style', 'width:100%; display:inline-block;');
	
	var sprintNameDiv = $('<div></div>');
	sprintNameDiv.append(sprintName);
	sprintHeaderDiv.append(sprintNameDiv);
	
	if (sprint.isActive) {
		sprintName.append(sprint.name + "  [active]");
		if (user.permissions == "MANAGER") {
			sprintNameDiv.attr('style', 'width:70%; float:left;');
			
			var editSprint = $('<button></button>');
			editSprint.attr('id','edit_sprint_button_' + sprint.id);
			editSprint.attr('class','dialog_opener');
			
			var deleteSprint = $('<button></button>');
			var sprintString = JSON.stringify(sprint);
			
			editSprint.attr('onclick', "editSprint('" + sprintString + "')");
			editSprint.append('Edit');
			deleteSprint.attr('onclick', "deleteSprint('" + sprintString + "')");
			deleteSprint.append('Delete');
			
			var deleteSprintDiv = $('<div></div>');
			deleteSprintDiv.attr('style', 'width:15%; height:100%; float:left; text-align:right;');
			deleteSprintDiv.append(deleteSprint);

			var editSprintDiv = $('<div></div>');
			editSprintDiv.attr('style', 'width:15%; height:100%; float:left; text-align:right;');
			editSprintDiv.append(editSprint);
			
			sprintHeaderDiv.append(deleteSprintDiv);
			sprintHeaderDiv.append(editSprintDiv);
			
			var deactivateForm = $('<form></form>');
			deactivateForm.attr('action', './makeSprintInactive');
			deactivateForm.attr('method', 'POST');
			var deactivateField = $('<input/>');
			deactivateField.attr('type', 'hidden');
			deactivateField.attr('value', sprintString);
			deactivateField.attr('name', 'deactivate');
			deactivateForm.append(deactivateField);

			var deactivateButton = $("<input/>");
			console.log(deactivateForm);
			deactivateButton.attr('type', 'submit');
			deactivateButton.attr('value', 'Make inactive');
			deactivateForm.append(deactivateButton)

			sprintName.append("<br><br>");
			sprintName.append(deactivateForm);
		}
	} else {
		sprintName.append(sprint.name + "  [inactive]");
		if (user.permissions == "MANAGER") {
			sprintNameDiv.attr('style', 'width:70%; float:left;');
			
			var editSprint = $('<button></button>');
			editSprint.attr('id','edit_sprint_button_' + sprint.id);
			editSprint.attr('class','dialog_opener');
			
			var deleteSprint = $('<button></button>');
			var sprintString = JSON.stringify(sprint);
			editSprint.attr('onclick', "editSprint('" + sprintString + "')");
			editSprint.append('Edit');
			deleteSprint.attr('onclick', "deleteSprint('" + sprintString + "')");
			deleteSprint.append('Delete');
			
			var deleteSprintDiv = $('<div></div>');
			deleteSprintDiv.attr('style', 'width:15%; height:100%; float:left; text-align:right;');
			deleteSprintDiv.append(deleteSprint);

			var editSprintDiv = $('<div></div>');
			editSprintDiv.attr('style', 'width:15%; height:100%; float:left; text-align:right;');
			editSprintDiv.append(editSprint);
			
			sprintHeaderDiv.append(deleteSprintDiv);
			sprintHeaderDiv.append(editSprintDiv);
			
			
			var activateForm = $('<form></form>');
			activateForm.attr('action', './makeSprintActive');
			activateForm.attr('method', 'POST');
			var activateField = $('<input/>');
			activateField.attr('type', 'hidden');
			activateField.attr('value', sprintString);
			activateField.attr('name', 'activate');
			activateForm.append(activateField);

			var activateButton = $("<input/>");
			console.log(activateForm);
			activateButton.attr('type', 'submit');
			activateButton.attr('value', 'Make active');
			activateForm.append(activateButton)

			sprintName.append("<br><br>");
			sprintName.append(activateForm);
		}
	}

	var containerId = 'sprint_' + sprintNumber + '_issues';
	var issuesContainer = $('<div></div>');
	issuesContainer.attr('id', containerId)
	newSprint.append(sprintHeaderDiv);
	newSprint.append(issuesContainer);

	container.append(newSprint);
	container.append('<hr>');

	return containerId;
}

// view JSON map as object map (test)
function viewMap(map) {
	var string = JSON.stringify(map);
	var object = JSON.parse(string);
	console.log(object);
}