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
}	
	
