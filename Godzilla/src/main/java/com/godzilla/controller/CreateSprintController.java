package com.godzilla.controller;

import java.time.LocalDate;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.Company;
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
			
			System.err.println(sprint.getStartingDate());
			System.err.println(sprint.getEndDate());
			
			Company currentCompany = ((Company) session.getAttribute("company"));
			Set<Project> companyProjects = ProjectDAO.getAllProjectsByCompany(currentCompany);
			
			session.setAttribute("companyProjects", companyProjects);;
			session.setAttribute("succeed", "Succeed: Sprint created");
			
			return "redirect:backlog";
		} catch (ProjectDAOException e) {
			session.setAttribute("issueError", "Could not find project with tha name try again");
			e.printStackTrace();
		} catch (SprintException e) {
			session.setAttribute("issue error", "Could not create Sprint");
			e.printStackTrace();
		} catch (SprintDAOException e) {
			session.setAttribute("issueError", "Could not create Sprint");
			e.printStackTrace();
		}
		
		return "redirect:backlog";
	}
}
