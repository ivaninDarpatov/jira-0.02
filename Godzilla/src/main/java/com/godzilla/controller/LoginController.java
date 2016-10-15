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
				
				int userId = UserDAO.getUserIdByEmail(email);
				
				toLogin = UserDAO.getUserById(userId);
				session.setAttribute("user", toLogin);
			} catch (UserDAOException | UserException e) {
				session.setAttribute("error", e.getMessage());
				return "LogInForm";
			}
			
			return "redirect:homepage";
		}
}
