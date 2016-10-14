package com.godzilla.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.Issue;
import com.godzilla.model.DAO.CompanyDAO;
import com.godzilla.model.DAO.IssueDAO;
import com.godzilla.model.enums.IssuePriority;
import com.godzilla.model.enums.IssueState;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.IssueException;

@Controller
@RequestMapping(value = "/editIssue")
public class EditIssueController {

	@RequestMapping(method = RequestMethod.POST)
	public String editIssue(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if(session == null){
			return "redirect:login";
		} else {
			if (session.getAttribute("user") == null) {
				session.invalidate();
				return "redirect:login";
			}
		}
		
		String issueId = request.getParameter("hidden");
		String summary = request.getParameter("summary");
		String priority = request.getParameter("priority");
		String state = request.getParameter("status");
		String description = request.getParameter("description");
		
		try {
			Issue issueToEdit = IssueDAO.getIssueById(Integer.parseInt(issueId));
			issueToEdit.setPriority(IssuePriority.getPriorityFromString(priority));
			issueToEdit.setState(IssueState.getIssueStateFromString(state));
			issueToEdit.setSummary(summary);
			issueToEdit.setDescription(description);
			
			IssueDAO.editIssue(issueToEdit);
			
			session.setAttribute("succeed", "Succeed: Issue edited");
			issueToEdit = IssueDAO.getIssueById(Integer.parseInt(issueId));
			
			//TODO: method getAllProjectByUser
		} catch (NumberFormatException | IssueDAOException e) {
			session.setAttribute("issueError", e.getMessage());
		} catch (IssueException e) {
			session.setAttribute("issueError", e.getMessage());
		}
		return "redirect:profilepage";
	}
}
