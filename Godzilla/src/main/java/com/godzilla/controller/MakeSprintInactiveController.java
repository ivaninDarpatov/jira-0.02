package com.godzilla.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.godzilla.model.Sprint;
import com.godzilla.model.DAO.SprintDAO;
import com.godzilla.model.exceptions.SprintDAOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/makeSprintInactive")
public class MakeSprintInactiveController {
	@RequestMapping(method = RequestMethod.POST)
	public String makeSprintInactive(HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		if (session == null) {
			return "redirect:login";
		} else {
			if (session.getAttribute("user") == null) {
				session.invalidate();
				return "redirect:login";
			}
		}

		try {
			String sprintJSON = request.getParameter("deactivate");
			Sprint sprintToMakeInactive = new Gson().fromJson(sprintJSON, Sprint.class);
			SprintDAO.makeInactive(sprintToMakeInactive);
			
		} catch (SprintDAOException e) {
			session.setAttribute("issueError", e.getMessage());
		}
		return "redirect:backlog";
	}
}
