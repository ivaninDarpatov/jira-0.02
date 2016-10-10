package com.godzilla.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.Company;
import com.godzilla.model.Issue;
import com.godzilla.model.Project;
import com.godzilla.model.User;
import com.godzilla.model.DAO.CompanyDAO;
import com.godzilla.model.DAO.IssueDAO;
import com.godzilla.model.DAO.UserDAO;
import com.godzilla.model.exceptions.CompanyDAOException;
import com.godzilla.model.exceptions.CompanyException;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.UserDAOException;
import com.godzilla.model.exceptions.UserException;
import com.google.gson.Gson;

import ch.qos.logback.core.net.SyslogOutputStream;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

	
		@RequestMapping(method = RequestMethod.GET)
		public String login(HttpServletRequest request){
			
			HttpSession session = request.getSession(false);
			if(session != null){
				session.invalidate();
			}
			
			
			return "LogInForm";
		}
		
		@RequestMapping(method = RequestMethod.POST)
		public String login(HttpServletRequest request,HttpServletResponse response,HttpSession Usersession){
			HttpSession session = request.getSession(false);
			session.invalidate();
			session = request.getSession();
			
			String companyName = request.getParameter("companyName");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			
			try {
				User toLogin = new User(email, password, companyName);

				if(!UserDAO.validateLogin(toLogin)){
					throw new UserDAOException("Not valid user");
				} 
			} catch (UserDAOException | UserException e) {
				session.setAttribute("error", e.getMessage());
				return "redirect:login";
			}
			
			
			String errorMessage = "";
			User userToLogIn = null;
			Company company = null;
			Set<Project> companyProjects = null;
			HashMap<String,Set<Issue>> assignedIssuesByProject = new HashMap<>();
			HashMap<String,Set<Issue>> reportedIssuesByProject = new HashMap<>();
			Set<Project> userProjects = new HashSet<Project>();
			
			StringBuilder builder = new StringBuilder();
			try {
				int userId = UserDAO.getUserIdByEmail(email);
				userToLogIn = UserDAO.getUserById(userId);
				int companyId = CompanyDAO.getIdOfCompanyWithName(companyName);
				company = CompanyDAO.getCompanyById(companyId);
				
				companyProjects = company.getProjects();
				for (Project proj : companyProjects) {
					Set<Issue> assigned = IssueDAO.getAllIssuesAssignedTo(userToLogIn, proj);
					Set<Issue> reported = IssueDAO.getAllReportedIssuesByUser(userToLogIn, proj);
					
					if (assigned.size() > 0 || reported.size() > 0) {
						userProjects.add(proj);
						
						assignedIssuesByProject.put(proj.getName(), assigned);
						reportedIssuesByProject.put(proj.getName(), reported);
					}
				}
				
				Gson jsonMaker = new Gson();
				String assignedIssuesJSON = jsonMaker.toJson(assignedIssuesByProject);
				String reportedIssuesJSON = jsonMaker.toJson(reportedIssuesByProject);

				session.setAttribute("assignedIssues", assignedIssuesJSON);
				session.setAttribute("reportedIssues", reportedIssuesJSON);
				session.setAttribute("user", userToLogIn);
				session.setAttribute("company", company);
				session.setAttribute("companyUsers", company.getUsers());
				session.setAttribute("companyProjects", companyProjects);
				session.setAttribute("userProjects", userProjects);
				
			} catch (CompanyDAOException e) {
				builder.append(e.getMessage());
			} catch (UserDAOException e) {
				errorMessage = e.getMessage();
				request.setAttribute("error", errorMessage);
			} catch (CompanyException e) {
				builder.append(e.getMessage());
			} catch (IssueDAOException e) {
				//todo
				e.printStackTrace();
			}
			
//			response.sendRedirect("../HomePage");
			
//			response.sendRedirect(request.getContextPath() + "/HomePage");
			
//			response.getWriter().println("<html> <body> <h1>" + builder.toString() + "</h1> </body> </html>");

			
			return "redirect:HomePage";
		}
}
