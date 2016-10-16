package com.godzilla.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
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
import com.godzilla.model.DAO.ProjectDAO;
import com.godzilla.model.DAO.SprintDAO;
import com.godzilla.model.DAO.UserDAO;
import com.godzilla.model.exceptions.CompanyDAOException;
import com.godzilla.model.exceptions.CompanyException;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.ProjectDAOException;
import com.godzilla.model.exceptions.SprintDAOException;
import com.godzilla.model.exceptions.UserDAOException;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/homepage")
public class HomePageController {

	@RequestMapping(method = RequestMethod.GET)
	public String homePage(HttpServletRequest request) {
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
			User currentUser = (User) session.getAttribute("user");
			Company company = null;
			Set<Project> companyProjects = null;
			HashMap<String, Set<Issue>> assignedIssuesByProject = new HashMap<>();
			HashMap<String, Set<Issue>> reportedIssuesByProject = new HashMap<>();
			Set<Project> userProjects = new HashSet<Project>();
			// projectName -> (sprintName -> sprintIssues) ((NO SPRINT -> sprintName = "-"))
			Map<String, Map<String, Set<Issue>>> projectSprintsIssues = new HashMap<>();
			Map<String, Set<Sprint>> projectSprints = new HashMap<String, Set<Sprint>>();
			String companyName = currentUser.getCompany();
			int companyId = CompanyDAO.getIdOfCompanyWithName(companyName);

			currentUser = UserDAO.getUserById(currentUser.getId());
			company = CompanyDAO.getCompanyById(companyId);
			companyProjects = ProjectDAO.getAllProjectsByCompany(company);
			Set<Project> orderedProjects = new TreeSet<>((p1, p2) -> p1.getId() - p2.getId());
			orderedProjects.addAll(companyProjects);

			for (Project proj : companyProjects) {
				Set<Issue> assigned = IssueDAO.getAllIssuesAssignedTo(currentUser, proj);
				Set<Issue> reported = IssueDAO.getAllReportedIssuesByUser(currentUser, proj);

				if (assigned.size() > 0 || reported.size() > 0) {
					userProjects.add(proj);

					assignedIssuesByProject.put(proj.getName(), assigned);
					reportedIssuesByProject.put(proj.getName(), reported);
				}
			}

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

			Gson jsonMaker = new Gson();
			String assignedIssuesJSON = jsonMaker.toJson(assignedIssuesByProject);
			String reportedIssuesJSON = jsonMaker.toJson(reportedIssuesByProject);
			String userJSON = jsonMaker.toJson(currentUser);
			String companyProjectsJSON = jsonMaker.toJson(orderedProjects);
			String companyUsersJSON = jsonMaker.toJson(company.getUsers());
			String projectIssuesJSON = jsonMaker.toJson(projectSprintsIssues);
			String projectSprintsJSON = jsonMaker.toJson(projectSprints);

			session.setAttribute("user", currentUser);
			session.setAttribute("companyUsersJSON", companyUsersJSON);
			session.setAttribute("userJSON", userJSON);
			session.setAttribute("assignedIssues", assignedIssuesJSON);
			session.setAttribute("reportedIssues", reportedIssuesJSON);
			session.setAttribute("company", company);
			session.setAttribute("companyUsers", company.getUsers());
			session.setAttribute("companyProjects", orderedProjects);
			session.setAttribute("userProjects", userProjects);
			session.setAttribute("companyProjectsJSON", companyProjectsJSON);
			session.setAttribute("projectSprintIssuesMap", projectIssuesJSON);
			session.setAttribute("projectSprintsMap", projectSprintsJSON);

		} catch (CompanyDAOException e) {
			request.setAttribute("error", e.getMessage());
		} catch (CompanyException e) {
			request.setAttribute("error", e.getMessage());
		} catch (IssueDAOException e) {
			request.setAttribute("error", e.getMessage());
		} catch (UserDAOException e) {
			request.setAttribute("error", e.getMessage());
		} catch (ProjectDAOException e1) {
			request.setAttribute("error", e1.getMessage());
		} catch (SprintDAOException e) {
			request.setAttribute("error", e.getMessage());
		}

		return "HomePage";
	}
}
