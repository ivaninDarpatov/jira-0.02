package com.godzilla.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.godzilla.model.Company;
import com.godzilla.model.Project;
import com.godzilla.model.Sprint;
import com.godzilla.model.DAO.CompanyDAO;
import com.godzilla.model.DAO.ProjectDAO;
import com.godzilla.model.DAO.SprintDAO;
import com.godzilla.model.exceptions.CompanyDAOException;
import com.godzilla.model.exceptions.ProjectDAOException;
import com.godzilla.model.exceptions.SprintDAOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/deleteproject")
public class DeleteProjectController {
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
		
		int projectId = Integer.parseInt(request.getParameter("project_id"));
		
		try {
			Project toDelete = ProjectDAO.getProjectById(projectId);
			ProjectDAO.removeProject(toDelete);
			
			Company company = (Company) session.getAttribute("company");
			
			Set<Project> companyProjects = ProjectDAO.getAllProjectsByCompany(company);
			session.setAttribute("companyProjects", companyProjects);
		} catch (ProjectDAOException e) {
			session.setAttribute("error", e.getMessage());
		}

		
		
		return "redirect:homepage";
	}
}
