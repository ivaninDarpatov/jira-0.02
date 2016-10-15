package com.godzilla.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.godzilla.model.Sprint;
import com.godzilla.model.DAO.SprintDAO;
import com.godzilla.model.exceptions.SprintDAOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/deletesprint")
public class DeleteSprintController {
	@RequestMapping(method = RequestMethod.GET) 
	public String deleteSprint(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session == null){
			return "redirect:login";
		} else {
			if (session.getAttribute("user") == null) {
				session.invalidate();
				return "redirect:login";
			}
		}
		
		int sprintId = Integer.parseInt(request.getParameter("sprint_id"));
		
		try {
			Sprint toDelete = SprintDAO.getSprintById(sprintId);
			SprintDAO.removeSprint(toDelete);
		} catch (SprintDAOException e) {
			session.setAttribute("error", e.getMessage());
		}
		
		
		return "redirect:backlog";
	}
}
