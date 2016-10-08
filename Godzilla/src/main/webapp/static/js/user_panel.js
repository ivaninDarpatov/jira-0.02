/*! jQuery v1.11.1 | (c) 2005, 2014 jQuery Foundation, Inc. | jquery.org/license */


function toggleShowParagraph (caller) {
	var callerId = caller.id;
	var idNumberBeginIndex = callerId.lastIndexOf("_") + 1;
	var paragraphId;
	
	if (callerId.includes("assigned")) {
		paragraphId = "assigned_issue_";
	}
	
	if (callerId.includes("reported")) {
		paragraphId = "reported_issue_";
	}
	
	
	paragraphId = paragraphId + callerId.substring(idNumberBeginIndex);
	var jqueryParagraphId = "#" + paragraphId;
	var paragraph = document.getElementById(paragraphId);

	<!-- jquery -->
	$(jqueryParagraphId).toggle(200);
}

function toggleShowDiv (caller) {
	<!-- jquery -->
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

//add issue
function addIssue (caller) {
	var container = $(caller);
	var issueType = caller.substring(1, 9);
	var issueNumber = document.querySelectorAll('div.' + issueType + '_issue_box').length + 1;
	
	//issue values
		var name = 'ISSUE-' + issueNumber;
		var type = 'task';
		var summary = 'task ' + issueNumber;
		var project = 'project 1';
		var reporter = 'user 1';
		var assignee = 'user 1';
		var description = 'description ' + issueNumber;
		var dateCreated = '07/10/2016';
		var dateLastModified = '07/10/2016';
	//
	
	var issueBox = document.createElement("div");
	issueBox.setAttribute("class", issueType + "_issue_box");
	issueBox.innerHTML = "<h4 class='issue_name'>" + 
						"<a href='#' class='dropbtn' id='" + issueType + "_issue_name_" + issueNumber + "' onclick='toggleShowParagraph(this)'>" + name +
						"</a>" +
						"</h4>" +
						"<p id='" + issueType + "_issue_" + issueNumber + "' class='issue_information'>" +
						"Issue type: " + type +
						" <br> " +
						"Summary: " + summary +
						" <br> " +
						"Project: " + project +
						" <br>" +
						"Reporter: " + reporter +
						" <br> " +
						"Assignee: " + assignee +
						" <br> " +
						"Description: " + description +
						" <br>" +
						"Created: " + dateCreated +
						" <br> " +
						"Last modified: " + dateLastModified +
						" <br>" +
						"</p>";
	container.append(issueBox);
}

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

//add project
function addProject(caller, project) {
	alert($(project.name));
	var container = $(caller);
	var projectNumber = document.querySelectorAll('div.project_box').length + 1;
	var projectName = "PROJECT-" + projectNumber;
	var projectId = "project_" + projectNumber;
	var projectBox = document.createElement("div");
	projectBox.setAttribute('id', projectId);
	projectBox.setAttribute('class', 'project_box');
	projectBox.innerHTML = 	"<h4>" +
							"<a href='#'>" + projectName + "</a>" +
							"</h4>" +
							"<h5 id='project_" + projectNumber + "_assigned' class='assigned_issues_count'>Assigned Issues 0</h5>" +
							"<h5 id='project_" + projectNumber + "_reported' class='reported_issues_count'>Reported	Issues 0</h5>";
	container.append(projectBox);
	
	var menu = $('#projects_menu_ul');
	var projectIdLI = projectId + "_li";
	var menuProject = document.createElement("li");
	menuProject.setAttribute('id', projectIdLI);
	menuProject.innerHTML = "<a href='#'>" + projectName +
							"</a>";
	
	menu.append(menuProject);
	menu.append(document.createElement("br"));
}

