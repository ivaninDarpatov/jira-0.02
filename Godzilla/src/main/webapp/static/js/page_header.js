//open 'create issue' dialog box
$(function() {
	console.log("asfsda");
	$("#dialog").dialog({
		autoOpen : false,
		width : 535.6
	});

	$("#opener").click(function() {
		$("#dialog").dialog('open');
	});
})

//toggle show/hide content by ID (caller)
function toggleShowDiv(caller) {
	// <!-- jquery -->
	$(caller).toggle(200);
}

//close dropdowns on click-out
window.onclick = function(event) {
	if (!event.target.matches('.dropbtn')) {

		var dropdowns = document.getElementsByClassName('dropdown_content');
		var i;
		for (i = 0; i < dropdowns.length; i++) {
			var openDropdownId = dropdowns[i].id;

			if ($('#' + openDropdownId).is(':visible')) {
				$('#' + openDropdownId).hide(200);
			}
		}
	}
}

//unused function
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

//load selected issue's information in the right-side box
//TO DO: edit issue pop-up
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
	
	var issueHeader = $('<div></div>');
	issueHeader.attr('style', 'width:100%; height:60px;');
	var issueNameDiv = $('<div></div>');
	issueNameDiv.attr('style', 'float:left; width:50%; height:100%;')
	var editIssueDiv = $('<div></div>');
	editIssueDiv.attr('style', 'float:left; width:50%; height:100%; text-align:right;')
	
	var issueName = $('<h4></h4>');
	issueName.append(name);
	var editIssue = $('<a></a>');
	editIssue.attr('href', '#');
	editIssue.append('Edit');

	issueNameDiv.append(issueName);
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
