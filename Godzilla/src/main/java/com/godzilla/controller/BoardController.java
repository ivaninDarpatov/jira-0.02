package com.godzilla.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.Issue;
import com.godzilla.model.Project;
import com.godzilla.model.Sprint;
import com.godzilla.model.DAO.IssueDAO;
import com.godzilla.model.DAO.SprintDAO;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.SprintDAOException;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/board")
public class BoardController {
	@RequestMapping(method = RequestMethod.GET)
	public String board(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (request.getSession(false) == null) {
			return "redirect:login";
		}if (session.getAttribute("user") == null) {
			session.invalidate();
			return "redirect:login";
		}
		
		session = request.getSession();
		
		Set<Project> companyProjects = (Set<Project>) session.getAttribute("companyProjects");
		//projectName -> (sprintName -> sprintIssues) ((NO SPRINT -> sprintName = "-"))
		Map<String, Map<String, Set<Issue>>> projectSprintsIssues = new HashMap<>();
		Map<String, Set<Sprint>> projectSprints = new HashMap<String, Set<Sprint>>();

		try {
			for(Project project : companyProjects){
			Map<String, Set<Issue>> sprintIssues = new HashMap<>();
			Set<Sprint> projectSprintsSet = SprintDAO.getAllSprintsByProject(project);
			for (Sprint sprint : projectSprintsSet) {
				Set<Issue> sprintIssuesSet = IssueDAO.getAllIssuesBySprint(sprint);
				sprintIssues.put(sprint.getName(), sprintIssuesSet);
			}
			
			Set<Issue> freeIssues = IssueDAO.getAllFreeIssuesByProject(project);
			sprintIssues.put("-", freeIssues);
			projectSprintsIssues.put(project.getName(), sprintIssues);
			
			projectSprints.put(project.getName(), projectSprintsSet);
			}
		
		Gson jsonMaker = new Gson();
		String projectIssuesJSON = jsonMaker.toJson(projectSprintsIssues);
		session.setAttribute("projectSprintIssuesMap", projectIssuesJSON);
		
		String projectSprintsJSON = jsonMaker.toJson(projectSprints);
		session.setAttribute("projectSprintsMap", projectSprintsJSON);

		String userJSON = jsonMaker.toJson(session.getAttribute("user"));
		session.setAttribute("userJSON", userJSON);
		
		} catch (SprintDAOException | IssueDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Board";
	}
}
