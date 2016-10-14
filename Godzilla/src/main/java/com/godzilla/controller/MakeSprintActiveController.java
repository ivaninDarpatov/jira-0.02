package com.godzilla.controller;

import java.lang.reflect.Type;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.godzilla.model.Company;
import com.godzilla.model.Project;
import com.godzilla.model.Sprint;
import com.godzilla.model.DAO.ProjectDAO;
import com.godzilla.model.DAO.SprintDAO;
import com.godzilla.model.exceptions.ProjectDAOException;
import com.godzilla.model.exceptions.ProjectException;
import com.godzilla.model.exceptions.SprintDAOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/makeSprintActive")
public class MakeSprintActiveController {
	@RequestMapping(method = RequestMethod.POST)
	public String makeSprintActive(HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		if (session == null) {
			return "redirect:login";
		} else {
			if (session.getAttribute("user") == null) {
				session.invalidate();
				return "redirect:login";
			}
		}

		Project currentProject;
		try {
			String sprintJSON = request.getParameter("activate");
			Sprint sprintToMakeActive = new Gson().fromJson(sprintJSON, Sprint.class);
			SprintDAO.makeActive(sprintToMakeActive);
			
			Company company = (Company) session.getAttribute("company");
			Set<Project> companyProjects = ProjectDAO.getAllProjectsByCompany(company);
			session.setAttribute("companyProjects", companyProjects);
		} catch (SprintDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProjectDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:backlog";
	}
}
