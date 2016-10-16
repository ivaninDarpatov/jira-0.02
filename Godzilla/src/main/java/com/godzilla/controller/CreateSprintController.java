package com.godzilla.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.Project;
import com.godzilla.model.Sprint;
import com.godzilla.model.DAO.ProjectDAO;
import com.godzilla.model.DAO.SprintDAO;
import com.godzilla.model.exceptions.ProjectDAOException;
import com.godzilla.model.exceptions.SprintDAOException;
import com.godzilla.model.exceptions.SprintException;

@Controller
@RequestMapping(value = "/createSprint")
public class CreateSprintController {

	@RequestMapping(method = RequestMethod.POST)
	public String createSpring(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if (session == null) {
			return "redirect:login";
		} else {
			if (session.getAttribute("user") == null) {
				session.invalidate();
				return "redirect:login";
			}
		}
		
		String sprintName = request.getParameter("sprint_name");
		String projectName = request.getParameter("project");
		String sprintGoal = request.getParameter("description");
		String startDate = request.getParameter("start_date");
		String endDate = request.getParameter("end_date");
		
		int selectedProjectId;
		try {
			selectedProjectId = ProjectDAO.getProjectIdByName(projectName);
			Project selectedProject = ProjectDAO.getProjectById(selectedProjectId);
			
			Sprint sprint = new Sprint(sprintName, sprintGoal);
			sprint.setStartingDate(LocalDate.parse(startDate));
			sprint.setEndDate(LocalDate.parse(endDate));
			
			SprintDAO.addSprint(sprint, selectedProject);
			
			session.setAttribute("succeed", "Succeed: Sprint created");
			
			return "redirect:backlog";
		} catch (ProjectDAOException e) {
			session.setAttribute("issueError", "Could not find project with tha name try again");
		} catch (SprintException e) {
			session.setAttribute("issue error", "Could not create Sprint");
		} catch (SprintDAOException e) {
			session.setAttribute("issueError", "Could not create Sprint");
		}
		
		return "redirect:backlog";
	}
}
