function loadProjectSprintsBoard(projectName) {
		//get map object from session JSON
		var map = JSON.parse(document.getElementById("projectSprintIssuesMap").value);
		var sprints = map[projectName];
		
		var map2 = JSON.parse(document.getElementById("projectSprintsMap").value);
		var sprints2 = map2[projectName];
		
		$('#project_name').empty();
		$('#project_name').append(projectName);
		
		$('#to_do').empty();
		$('#in_progress').empty();
		$('#done').empty();
		$('#issue_info_well').empty();
		
		
		var active = false;
		
		for (var i = 0; i < sprints2.length; i++) {
			if (sprints2[i].isActive) {
				$("#sprint_name").append(sprints2[i].name);
				var issues = sprints2[i].issues;
				var issuesString = JSON.stringify(issues);
				addSprintBoard('#opened_sprint_container', sprints2[i].name, issues);
				active = true;
				break;
			}
		}
		
		if (!active) {
			var container = $('#active_sprint_name');
			container.empty();
			container.append("This project has no active sprints");
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
		var nameContainer = $('#active_sprint_name');
		nameContainer.empty();
		nameContainer.append(sprintName);
		
		$('#issue_info_well').empty();
		
		//add sprint issues in table
		for (var i = 0; i < issues.length; i++) {
			var issueContainerId = '#' + issues[i].state.toLowerCase();
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
	
