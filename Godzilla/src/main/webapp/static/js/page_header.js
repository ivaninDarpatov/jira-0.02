//open 'create issue' dialog box
$(function() {
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