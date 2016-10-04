package com.godzilla.controller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.Company;
import com.godzilla.model.User;
import com.godzilla.model.DAO.CompanyDAO;
import com.godzilla.model.DAO.UserDAO;
import com.godzilla.model.exceptions.CompanyDAOException;
import com.godzilla.model.exceptions.CompanyException;
import com.godzilla.model.exceptions.UserDAOException;
import com.godzilla.model.exceptions.UserException;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

	
		@RequestMapping(method = RequestMethod.GET)
		public String login(){
			return "LogInForm";
		}
		
		@RequestMapping(method = RequestMethod.POST)
		public String login(HttpServletRequest request){
			String companyName = request.getParameter("companyName");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			User userToLogIn = null;
			Company company = null;
			StringBuilder builder = new StringBuilder();
			try {
				userToLogIn = new User(email, password,companyName);
				company = new Company(companyName);
				
				int id = CompanyDAO.getIdOfCompanyWithName(companyName);
				company.setId(id);
				
				company = CompanyDAO.getCompanyById(id);
				
//				for(Project p : ProjectDAO.getAllProjectsByCompany(company)){
//					company.addNewProject(p);
//				}
				
				try {
					if(UserDAO.validateLogin(userToLogIn)){
						System.err.println("Cannot validate login");
//						response.getWriter().println("<html> <body> <h1> uspeshen login </h1> </body> </html>");
					} else {
						builder.append("Not registered user");
					}
				} catch (UserDAOException e) {
					builder.append(e.getMessage());
				}
			} catch (UserException e) {
				builder.append(e.getMessage());
			} catch (CompanyException e1) {
				builder.append(e1.getMessage());
			} catch (CompanyDAOException e1) {
				builder.append(e1.getMessage());
			}
			
			request.setAttribute("user", userToLogIn);
			request.setAttribute("company", company);
			request.setAttribute("companyUsers", company.getUsers());
			request.setAttribute("projects", company.getProjects());
//			response.sendRedirect("../HomePage");
			
//			response.sendRedirect(request.getContextPath() + "/HomePage");
			
//			response.getWriter().println("<html> <body> <h1>" + builder.toString() + "</h1> </body> </html>");

			return "HomePage";
		}
}
