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
@RequestMapping(value = "/backlog")
public class BacklogController {

	@RequestMapping(method = RequestMethod.GET)
	public String backlog(HttpServletRequest request){
		
		HttpSession session = request.getSession(false);
		if(session == null){
			return "redirect:login";
		} else {
			if (session.getAttribute("user") == null) {
				session.invalidate();
				return "redirect:login";
			}
		}
		

		Gson jsonMaker = new Gson();

		//projectName -> (sprintName -> sprintIssues) ((NO SPRINT -> sprintName = "-"))
		Map<String, Map<String, Set<Issue>>> projectSprintsIssues = new HashMap<>();
		Map<String, Set<Sprint>> projectSprints = new HashMap<String, Set<Sprint>>();
		Set<Project> companyProjects = (Set<Project>) session.getAttribute("companyProjects");
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
		
		String projectIssuesJSON = jsonMaker.toJson(projectSprintsIssues);
		session.setAttribute("projectSprintIssuesMap", projectIssuesJSON);
		
		String projectSprintsJSON = jsonMaker.toJson(projectSprints);
		session.setAttribute("projectSprintsMap", projectSprintsJSON);

		
		} catch (SprintDAOException | IssueDAOException e) {
			session.setAttribute("error", e.getMessage());
		}
		String userJSON = jsonMaker.toJson(session.getAttribute("user"));
		session.setAttribute("userJSON", userJSON);
		
		
		
		return "Backlog";
	}
}
