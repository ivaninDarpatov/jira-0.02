package com.godzilla.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.Issue;
import com.godzilla.model.DAO.IssueDAO;
import com.godzilla.model.exceptions.IssueDAOException;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/filters")
public class FiltersController {

	@RequestMapping(method = RequestMethod.GET)
	public String filters(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (request.getSession(false) == null) {
			return "redirect:login";
		}if (session.getAttribute("user") == null) {
			session.invalidate();
			return "redirect:login";
		}
		
		return "Filters";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String filters(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();

		String projectName = request.getParameter("project_name");
		String issueState = request.getParameter("issue_state");
		String sprintName = request.getParameter("sprint_name");
		String assigneeEmail = request.getParameter("assignee");
		String reporterEmail = request.getParameter("reporter");
		String companyName = request.getParameter("company_name");
		System.err.println(projectName + '\n' + issueState + '\n' + sprintName + '\n' + assigneeEmail + '\n' + 
							reporterEmail + '\n' + companyName + '\n');
		
		try {
			String result = IssueDAO.getAllIssuesFilteredByJSON(issueState, projectName, sprintName, 
											reporterEmail, assigneeEmail, companyName);
			
			session.setAttribute("filterResultIssues", result);
			session.setAttribute("succeed", "Showing issues in: Project #" + projectName +
											", State #" + issueState + ", Sprint #" + sprintName +
											", Reporter #" + reporterEmail + ", Assignee #" + assigneeEmail);
		} catch (IssueDAOException e) {
			session.setAttribute("error", e.getMessage());
		}
		
		return "Filters";
	}
}
