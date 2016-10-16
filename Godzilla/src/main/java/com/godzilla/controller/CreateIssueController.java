package com.godzilla.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.Epic;
import com.godzilla.model.Issue;
import com.godzilla.model.Project;
import com.godzilla.model.Sprint;
import com.godzilla.model.User;
import com.godzilla.model.DAO.IssueDAO;
import com.godzilla.model.DAO.ProjectDAO;
import com.godzilla.model.DAO.SprintDAO;
import com.godzilla.model.DAO.UserDAO;
import com.godzilla.model.enums.IssuePriority;
import com.godzilla.model.enums.IssueState;
import com.godzilla.model.exceptions.EpicException;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.IssueException;
import com.godzilla.model.exceptions.ProjectDAOException;
import com.godzilla.model.exceptions.SprintDAOException;
import com.godzilla.model.exceptions.UserDAOException;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/createIssue")
public class CreateIssueController {

	@RequestMapping(method = RequestMethod.POST)
	public String createIssue(HttpServletRequest request) {
		// issue_type , project , priority , status , description ,
		// linked_issues , link_type , assignee
		System.err.println("REQUEST: " + request);
		HttpSession session = request.getSession(false);
		if (session == null) {
			return "redirect:login";
		} else {
			if (session.getAttribute("user") == null) {
				session.invalidate();
				return "redirect:login";
			}
		}

		String summary = request.getParameter("summary");
		String issueType = request.getParameter("issue_type");
		String projectName = request.getParameter("project");
		String sprintName = request.getParameter("sprint");
		String priority = request.getParameter("priority");
		String status = request.getParameter("status");
		String description = request.getParameter("description");
//		String linkedIssueName = request.getParameter("linked_issues");
//		String linkType = request.getParameter("link_type");
		String assigneeEmail = request.getParameter("assignee");
		User currentUser = (User) session.getAttribute("user");

		String currentUserEmail = currentUser.getEmail();

		int projectId;
		Project project = null;

		int currentUserId;
		User user = null;

		int assigneeId;
		User assignee = null;

//		int linkedIssueId;
//		Issue linkedIssue = null;

		IssuePriority issuePriority = IssuePriority.getPriorityFromString(priority);
		IssueState issueState = IssueState.getIssueStateFromString(status);

		Issue issue = null;

		try {
			projectId = ProjectDAO.getProjectIdByName(projectName);
			project = ProjectDAO.getProjectById(projectId);

			currentUserId = UserDAO.getUserIdByEmail(currentUserEmail);
			user = UserDAO.getUserById(currentUserId);

			assigneeId = UserDAO.getUserIdByEmail(assigneeEmail);
			assignee = UserDAO.getUserById(assigneeId);

//			linkedIssueId = IssueDAO.getIssueIdByName(linkedIssueName);
//			linkedIssue = IssueDAO.getIssueById(linkedIssueId);

			if (issueType.equalsIgnoreCase("Epic")) {
				issue = new Epic(summary, issueType);

			} else {
				issue = new Issue(summary, issueType);
			}

			issue.setDescription(description);
			issue.setPriority(issuePriority);
			issue.setState(issueState);

			IssueDAO.createIssue(issue, project, user, assignee);
			if (!sprintName.equals("NONE")) {
				Sprint sprint = SprintDAO.getSprintById(SprintDAO.getSprintIdByName(sprintName));
				IssueDAO.addIssueToSprint(issue, sprint);
			}

			//update current user with the new issue
			user = UserDAO.getUserById(currentUserId);
			String userJSON = new Gson().toJson(user);
			session.setAttribute("succeed", "Succeed: Issue created");
			session.setAttribute("user", user);
			session.setAttribute("userJSON", userJSON);
			
		} catch (ProjectDAOException e) {
			session.setAttribute("issueError", e.getMessage());
		} catch (UserDAOException e) {
			session.setAttribute("issueError", e.getMessage());
		} catch (IssueDAOException e) {
			session.setAttribute("issueError", e.getMessage());
		} catch (IssueException e) {
			session.setAttribute("issueError", e.getMessage());
		} catch (EpicException e) {
			session.setAttribute("issueError", e.getMessage());
		} catch (SprintDAOException e) {
			session.setAttribute("issueError", e.getMessage());
		}

		return "redirect:homepage";
	}
}
