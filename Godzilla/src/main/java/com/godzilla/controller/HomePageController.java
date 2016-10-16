package com.godzilla.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.Company;
import com.godzilla.model.Issue;
import com.godzilla.model.Project;
import com.godzilla.model.Sprint;
import com.godzilla.model.User;
import com.godzilla.model.DAO.CompanyDAO;
import com.godzilla.model.DAO.IssueDAO;
import com.godzilla.model.DAO.SprintDAO;
import com.godzilla.model.exceptions.CompanyDAOException;
import com.godzilla.model.exceptions.CompanyException;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.SprintDAOException;
import com.google.gson.Gson;


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
		try {
			User currentUser = (User) session.getAttribute("user");
			Company company = null;
			Set<Project> companyProjects = null;
			HashMap<String,Set<Issue>> assignedIssuesByProject = new HashMap<>();
			HashMap<String,Set<Issue>> reportedIssuesByProject = new HashMap<>();
			Set<Project> userProjects = new HashSet<Project>();
			
			String companyName = currentUser.getCompany();

			int companyId = CompanyDAO.getIdOfCompanyWithName(companyName);
			company = CompanyDAO.getCompanyById(companyId);
			
			companyProjects = company.getProjects();
			
			for (Project proj : companyProjects) {
				Set<Issue> assigned = IssueDAO.getAllIssuesAssignedTo(currentUser, proj);
				Set<Issue> reported = IssueDAO.getAllReportedIssuesByUser(currentUser, proj);
				
				if (assigned.size() > 0 || reported.size() > 0) {
					userProjects.add(proj);
					
					assignedIssuesByProject.put(proj.getName(), assigned);
					reportedIssuesByProject.put(proj.getName(), reported);
				}
			}
			
			Gson jsonMaker = new Gson();
			String assignedIssuesJSON = jsonMaker.toJson(assignedIssuesByProject);
			String reportedIssuesJSON = jsonMaker.toJson(reportedIssuesByProject);
			String userJSON = jsonMaker.toJson(currentUser);
			
			Set<Project> orderedProjects= new TreeSet<>((p1, p2) -> p1.getId() - p2.getId());
			orderedProjects.addAll(companyProjects);
			String companyProjectsJSON = jsonMaker.toJson(orderedProjects);
			String companyUsersJSON = jsonMaker.toJson(company.getUsers());
			
			session.setAttribute("companyUsersJSON", companyUsersJSON);
			session.setAttribute("userJSON", userJSON);
			session.setAttribute("assignedIssues", assignedIssuesJSON);
			session.setAttribute("reportedIssues", reportedIssuesJSON);
			session.setAttribute("company", company);
			session.setAttribute("companyUsers", company.getUsers());
			session.setAttribute("companyProjects", orderedProjects);
			session.setAttribute("userProjects", userProjects);
			session.setAttribute("companyProjectsJSON", companyProjectsJSON);
			

			
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
			
			String projectIssuesJSON = jsonMaker.toJson(projectSprintsIssues);
			session.setAttribute("projectSprintIssuesMap", projectIssuesJSON);
			
			String projectSprintsJSON = jsonMaker.toJson(projectSprints);
			session.setAttribute("projectSprintsMap", projectSprintsJSON);

			
			} catch (SprintDAOException | IssueDAOException e) {
				session.setAttribute("error", e.getMessage());
			}
			
			
		} catch (CompanyDAOException e) {
			request.setAttribute("error", e.getMessage());
		} catch (CompanyException e) {
			request.setAttribute("error", e.getMessage());
		} catch (IssueDAOException e) {
			request.setAttribute("error", e.getMessage());
		}
		
		return "HomePage";
	}
}	
	
