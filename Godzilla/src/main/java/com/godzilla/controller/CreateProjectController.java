package com.godzilla.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.Company;
import com.godzilla.model.Project;
import com.godzilla.model.DAO.ProjectDAO;
import com.godzilla.model.exceptions.ProjectDAOException;
import com.godzilla.model.exceptions.ProjectException;

@Controller
@RequestMapping(value = "/createProject")
public class CreateProjectController {

	@RequestMapping(method = RequestMethod.POST)
	public String createProject(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if(session == null){
			return "redirect:login";
		} else {
			if (session.getAttribute("user") == null) {
				session.invalidate();
				return "redirect:login";
			}
		}	
		
		String projectName = request.getParameter("project_name");
		Company currentCompany =((Company) session.getAttribute("company"));
		String companyName = currentCompany.getName();
		
		try {
			Project project = new Project(projectName);
			if(!ProjectDAO.isThereProjectWithThatNameInCompany(projectName, companyName)){
				ProjectDAO.addProject(project, currentCompany);
				
				Set<Project> companyProject = ProjectDAO.getAllProjectsByCompany(currentCompany);
				session.setAttribute("companyProjects", companyProject);
				
				session.setAttribute("succeed", "Succeed: Project created");
			}else{
				session.setAttribute("issueError", "THere is allready a project with that name in the same company");
			}
		} catch (ProjectDAOException e) {
			e.printStackTrace();
			session.setAttribute("issueError", "Cannot create project");
			e.printStackTrace();
		} catch (ProjectException e) {
			session.setAttribute("issueError", "Cant create project");
			e.printStackTrace();
		}
		
		return "redirect:homepage";
	}	
}
