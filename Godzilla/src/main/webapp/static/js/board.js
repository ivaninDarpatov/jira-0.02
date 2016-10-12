function loadProjectSprintsBoard(projectName) {
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
	
