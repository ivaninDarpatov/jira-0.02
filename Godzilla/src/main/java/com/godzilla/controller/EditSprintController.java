package com.godzilla.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.Sprint;
import com.godzilla.model.DAO.SprintDAO;
import com.godzilla.model.exceptions.SprintDAOException;
import com.godzilla.model.exceptions.SprintException;

@Controller
@RequestMapping(value = "/editSprint")
public class EditSprintController {

	@RequestMapping(method = RequestMethod.POST)
	public String editSprint(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if(session == null){
			return "redirect:login";
		} else {
			if (session.getAttribute("user") == null) {
				session.invalidate();
				return "redirect:login";
			}
		}
		String sprintIdAsString = request.getParameter("sprintId");
		int sprintId = Integer.parseInt(sprintIdAsString);
		
		String sprintNameInput = request.getParameter("name");
		String sprintGoalInput = request.getParameter("goal");
		
		try {
			Sprint sprintToEdit = SprintDAO.getSprintById(sprintId);
			sprintToEdit.setName(sprintNameInput);
			sprintToEdit.setSprintGoal(sprintGoalInput);
			
			SprintDAO.editSprint(sprintToEdit);
			sprintToEdit = SprintDAO.getSprintById(sprintId);
			
			session.setAttribute("succeed", "Succeed: Sprint edit");
		} catch (SprintDAOException e) {
			session.setAttribute("issueError", e.getMessage());
		} catch (SprintException e) {
			session.setAttribute("issueError", e.getMessage());
		}
		
		
		
		return "redirect:backlog";
	}
}
