//load profile information on startup
$(function () {
	var userJSON = document.getElementById("userInformation").value;
	var user = JSON.parse(userJSON);
	var profileInfoContainer = $("#user_profile_information");
	profileInfoContainer.empty();
	//user information
	var paragraph = $("<p></p>")
	var company = user.company;
	var permissions = user.permissions;
	var assignedIssues = user.issuesAssignedTo;
	var reportedIssues = user.issuesReportedBy;
	//
	
	paragraph.append("Company: " + company + "<hr>");
	paragraph.append("Permissions: " + permissions + "<hr>");

	var assignedLink = "Assigned issues";
	var assignedContainer = $("<div></div>");
	assignedContainer.attr("id", "#assigned_container");
	
	for (var i = 0; i < assignedIssues.length; i++) {
		var issueBox = $("<div></div>");
		issueBox.attr("class", "profile_issue_box");
		var issueName = $("<a></a>");
		var issueString = JSON.stringify(assignedIssues[i]);
		issueName.attr("onclick", "openIssueInformation(" + issueString + ")");
		issueName.append(assignedIssues[i].name);
		issueBox.append(issueName);
		issueBox.append("\t" + assignedIssues[i].summary);
		assignedContainer.append(issueBox);
	}
	
	paragraph.append(assignedLink);
	paragraph.append(assignedContainer);
	paragraph.append("<hr>");
	
	var reportedLink = "Reported issues";
	var reportedContainer = $("<div></div>");
	reportedContainer.attr("id", "#reported_container");
	
	for (var i = 0; i < reportedIssues.length; i++) {
		var issueBox = $("<div></div>");
		issueBox.attr("class", "profile_issue_box");
		var issueName = $("<a></a>");
		var issueString = JSON.stringify(reportedIssues[i]);
		issueName.attr("onclick", "openIssueInformation(" + issueString + ")");
		issueName.append(reportedIssues[i].name);
		issueBox.append(issueName);
		issueBox.append("\t" + reportedIssues[i].summary);
		reportedContainer.append(issueBox);
	}
	
	paragraph.append(reportedLink);
	paragraph.append(reportedContainer);
	paragraph.append("<hr>");
	
	profileInfoContainer.append(paragraph);
})
