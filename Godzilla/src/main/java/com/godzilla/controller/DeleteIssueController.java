package com.godzilla.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.Issue;
import com.godzilla.model.DAO.IssueDAO;
import com.godzilla.model.exceptions.IssueDAOException;


@Controller
@RequestMapping(value = "/deleteissue")
public class DeleteIssueController {
	@RequestMapping(method = RequestMethod.GET)
	public String deleteIssue(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session == null){
			return "redirect:login";
		} else {
			if (session.getAttribute("user") == null) {
				session.invalidate();
				return "redirect:login";
			}
		}
		
		int issueId = Integer.parseInt(request.getParameter("issue_id"));

		try {
			Issue toRemove = IssueDAO.getIssueById(issueId);
			String issueName = toRemove.getName();
					
			IssueDAO.removeIssue(toRemove);
			session.setAttribute("succeed", "Succeed: Deleted Issue " + issueName);
		} catch (IssueDAOException e) {
			session.setAttribute("issueError", e.getMessage());
		}
		
		return "redirect:homepage";
	}
}
