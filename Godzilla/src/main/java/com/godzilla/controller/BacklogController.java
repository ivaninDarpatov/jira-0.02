package com.godzilla.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.Company;
import com.godzilla.model.Issue;
import com.godzilla.model.Project;
import com.godzilla.model.Sprint;
import com.godzilla.model.DAO.IssueDAO;
import com.godzilla.model.DAO.ProjectDAO;
import com.godzilla.model.DAO.SprintDAO;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.ProjectDAOException;
import com.godzilla.model.exceptions.SprintDAOException;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/backlog")
public class BacklogController {

	@RequestMapping(method = RequestMethod.GET)
	public String backlog(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return "redirect:login";
		} else {
			if (session.getAttribute("user") == null) {
				session.invalidate();
				return "redirect:login";
			}
		}

		Gson jsonMaker = new Gson();

		// projectName -> (sprintName -> sprintIssues) ((NO SPRINT -> sprintName
		// = "-"))
		Map<String, Map<String, Set<Issue>>> projectSprintsIssues = new HashMap<>();

		// projectName -> projectSprints
		Map<String, Set<Sprint>> projectSprints = new HashMap<String, Set<Sprint>>();

		Company company = (Company) session.getAttribute("company");
		Set<Project> companyProjects = new HashSet<Project>();
		try {
			companyProjects = ProjectDAO.getAllProjectsByCompany(company);
			for (Project project : companyProjects) {
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

		} catch (SprintDAOException | IssueDAOException e) {
			session.setAttribute("error", e.getMessage());
		} catch (ProjectDAOException e) {
			session.setAttribute("error", e.getMessage());
		}

		String userJSON = jsonMaker.toJson(session.getAttribute("user"));
		String projectSprintsJSON = jsonMaker.toJson(projectSprints);
		String projectIssuesJSON = jsonMaker.toJson(projectSprintsIssues);
		
		session.setAttribute("projectSprintIssuesMap", projectIssuesJSON);
		session.setAttribute("projectSprintsMap", projectSprintsJSON);
		session.setAttribute("companyProjects", companyProjects);
		session.setAttribute("userJSON", userJSON);

		return "Backlog";
	}
}
