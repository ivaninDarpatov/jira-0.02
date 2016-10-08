package com.godzilla.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.Company;
import com.godzilla.model.User;
import com.godzilla.model.DAO.CompanyDAO;
import com.godzilla.model.DAO.UserDAO;
import com.godzilla.model.exceptions.CompanyDAOException;
import com.godzilla.model.exceptions.CompanyException;
import com.godzilla.model.exceptions.UserDAOException;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

	
		@RequestMapping(method = RequestMethod.GET)
		public String login(){
			return "LogInForm";
		}
		
		@RequestMapping(method = RequestMethod.POST)
		public String login(HttpServletRequest request,HttpServletResponse response,HttpSession Usersession){
			HttpSession session = request.getSession();
			String companyName = request.getParameter("companyName");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			
			String errorMessage = "";
			User userToLogIn = null;
			Company company = null;
			StringBuilder builder = new StringBuilder();
			try {
				int userId = UserDAO.getUserIdByEmail(email);
				userToLogIn = UserDAO.getUserById(userId);
				int companyId = CompanyDAO.getIdOfCompanyWithName(companyName);
				company = CompanyDAO.getCompanyById(companyId);
				
				if(!UserDAO.validateLogin(userToLogIn)){
					request.setAttribute("error", "not valid user");
				} 
				
				
				session.setAttribute("user", userToLogIn);
				session.setAttribute("company", company);
				session.setAttribute("companyUsers", company.getUsers());
				session.setAttribute("projects", company.getProjects());
				
			} catch (CompanyDAOException e) {
				builder.append(e.getMessage());
			} catch (UserDAOException e) {
				errorMessage = e.getMessage();
				request.setAttribute("error", errorMessage);
			} catch (CompanyException e) {
				builder.append(e.getMessage());
			}
			
//			response.sendRedirect("../HomePage");
			
//			response.sendRedirect(request.getContextPath() + "/HomePage");
			
//			response.getWriter().println("<html> <body> <h1>" + builder.toString() + "</h1> </body> </html>");

			
			return "redirect:HomePage";
		}
}
