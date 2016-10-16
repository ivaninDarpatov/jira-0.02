

	$(function filterIssues() {
		$("#search_result").empty();
		var result = document.getElementById("filterResult").value;
		var issues = JSON.parse(result);
		
		for (var i = 0; i < issues.length; i++) {
			var issueBox = $('<div></div>');
			issueBox.attr('class', 'filter_issue_box');
			var bold = $('<b></b>');
			bold.attr('class', 'issue_name');
			var anchor = $('<a></a>');
			anchor.attr('id', 'issue_' + i);
			var issueString = JSON.stringify(issues[i]);
			anchor.attr('onclick', "openIssueInformation(" + issueString + ")");
			anchor.append(issues[i].name);
			bold.append(anchor);
			issueBox.append(bold);
			$('#search_result').append(issueBox);
		}

	});