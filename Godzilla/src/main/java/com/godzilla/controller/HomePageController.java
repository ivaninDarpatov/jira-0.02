package com.godzilla.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.Epic;
import com.godzilla.model.Issue;
import com.godzilla.model.Project;
import com.godzilla.model.User;
import com.godzilla.model.DAO.IssueDAO;
import com.godzilla.model.DAO.ProjectDAO;
import com.godzilla.model.DAO.UserDAO;
import com.godzilla.model.enums.IssuePriority;
import com.godzilla.model.enums.IssueState;
import com.godzilla.model.exceptions.EpicException;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.IssueException;
import com.godzilla.model.exceptions.ProjectDAOException;
import com.godzilla.model.exceptions.UserDAOException;

@Controller
@RequestMapping(value = "/homepage")
public class HomePageController {

	@RequestMapping(method = RequestMethod.GET)
	public String homePage(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if(session == null){
			return "redirect:login";
		} else {
			if (session.getAttribute("user") == null) {
				session.invalidate();
				return "redirect:login";
			}
		}
		
		return "HomePage";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String createIssue(HttpServletRequest request){
		//issue_type , project , priority , status , description , 
		//linked_issues , link_type , assignee
		System.err.println("REQUEST: " + request);
		HttpSession session = request.getSession(false);
		if(session == null){
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
		String priority = request.getParameter("priority");
		String status = request.getParameter("status");
		String description = request.getParameter("description");
		String linkedIssueName = request.getParameter("linked_issues");
		String linkType = request.getParameter("link_type");
		String assigneeEmail = request.getParameter("assignee");
		User currentUser = (User) session.getAttribute("user");
		
		String currentUserEmail = currentUser.getEmail();
		System.err.println("Current user: " + currentUserEmail);
		
		System.out.println("Summary: " + summary);
		System.out.println("IssueType: " + issueType);
		System.out.println("projectName: " + projectName);
		System.out.println("priority: " + priority);
		System.out.println("status: " + status);
		System.out.println("description: " + description);
		System.out.println("linkedIssue: " + linkedIssueName);
		System.out.println("linkType: " + linkType);
		System.out.println("assigneeName: " + assigneeEmail);
		
		int projectId;
		Project project = null;
		
		int currentUserId;
		User user = null;
		
		int linkedIssueId;
		Issue linkedIssue = null;
		
		IssuePriority issuePriority = IssuePriority.getPriorityFromString(priority);
		IssueState issueState = IssueState.getIssueStateFromString(status);
		
		Issue issue = null;
		
		try {
			projectId = ProjectDAO.getProjectIdByName(projectName);
			project = ProjectDAO.getProjectById(projectId);
			
			currentUserId = UserDAO.getUserIdByEmail(currentUserEmail);
			user = UserDAO.getUserById(currentUserId);
			
			linkedIssueId = IssueDAO.getIssueIdByName(linkedIssueName);
			linkedIssue = IssueDAO.getIssueById(linkedIssueId);
			
			if(issueType.equalsIgnoreCase("Epic")){
				issue = new Epic(summary, issueType);
				
			}else{
				issue = new Issue(summary, issueType);
			}
			
			issue.setDescription(description);
			issue.setPriority(issuePriority);
			issue.setState(issueState);
			
			IssueDAO.createIssue(issue, project, user);
			
			request.setAttribute("succeed", "Succeed: Issue created");
		} catch (ProjectDAOException e) {
			request.setAttribute("issueError", e.getMessage());
			e.printStackTrace();
		} catch (UserDAOException e) {
			request.setAttribute("issueError", e.getMessage());
			e.printStackTrace();
		} catch (IssueDAOException e) {
			request.setAttribute("issueError", e.getMessage());
			e.printStackTrace();
		} catch (IssueException e) {
			request.setAttribute("issueError", e.getMessage());
			e.printStackTrace();
		} catch (EpicException e) {
			request.setAttribute("issueError", e.getMessage());
			e.printStackTrace();
		}
		
		System.err.println("----------------------------------" + request.getAttribute("issueError"));
		
		System.err.println("Project object: " + project);
		System.err.println("User object: " + user);
		System.err.println("Linked Issue object: " + linkedIssue);
		System.err.println("Created Issue: " + issue);
		

		return "HomePage";
	}
}
