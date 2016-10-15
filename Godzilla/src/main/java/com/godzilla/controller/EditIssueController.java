package com.godzilla.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.Issue;
import com.godzilla.model.User;
import com.godzilla.model.DAO.IssueDAO;
import com.godzilla.model.DAO.UserDAO;
import com.godzilla.model.enums.IssuePriority;
import com.godzilla.model.enums.IssueState;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.IssueException;
import com.godzilla.model.exceptions.UserDAOException;
import com.google.gson.Gson;

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
		String assigneeEmail = request.getParameter("assignee");
		
		try {
			int issueToEditId = Integer.parseInt(issueId);
			Issue issueToEdit = IssueDAO.getIssueById(issueToEditId);
			
			issueToEdit.setPriority(IssuePriority.getPriorityFromString(priority));
			
			issueToEdit.setState(IssueState.getIssueStateFromString(state));
			
			issueToEdit.setSummary(summary);
			issueToEdit.setDescription(description);
			
			IssueDAO.editIssue(issueToEdit);
			
			issueToEdit = IssueDAO.getIssueById(Integer.parseInt(issueId));
			
			User currentUser = (User) session.getAttribute("user");
			int userId = currentUser.getId();
			
			int assigneeId = UserDAO.getUserIdByEmail(assigneeEmail);
			User assignee = (User) UserDAO.getUserById(assigneeId);
			
			IssueDAO.assignIssue(issueToEdit, assignee);
			
			currentUser = UserDAO.getUserById(userId);
			Gson jsonMaker = new Gson();
			String userJSON = jsonMaker.toJson(currentUser);
			
			session.setAttribute("succeed", "Succeed: Issue edited");
			session.setAttribute("user", currentUser);
			session.setAttribute("userJSON", userJSON);
			
			
		} catch (NumberFormatException | IssueDAOException e) {
			session.setAttribute("issueError", e.getMessage());
		} catch (IssueException e) {
			session.setAttribute("issueError", e.getMessage());
		} catch (UserDAOException e) {
			session.setAttribute("issueError", e.getMessage());
		}
		return "redirect:profilepage";
	}
}
