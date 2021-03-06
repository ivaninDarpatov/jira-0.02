package com.godzilla.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.godzilla.model.Project;
import com.godzilla.model.DAO.ProjectDAO;
import com.godzilla.model.exceptions.ProjectDAOException;
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
		} catch (ProjectDAOException e) {
			session.setAttribute("error", e.getMessage());
		}

		
		
		return "redirect:homepage";
	}
}
