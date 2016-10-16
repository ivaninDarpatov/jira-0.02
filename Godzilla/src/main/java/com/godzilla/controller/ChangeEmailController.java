package com.godzilla.controller;

import java.util.Set;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.User;
import com.godzilla.model.DAO.CompanyDAO;
import com.godzilla.model.DAO.UserDAO;
import com.godzilla.model.exceptions.EmailException;
import com.godzilla.model.exceptions.UserDAOException;

@Controller
@RequestMapping(value = "/changeEmail")
public class ChangeEmailController {

	@RequestMapping(method = RequestMethod.POST)
	public String changeEmail(HttpServletRequest request){
		
		HttpSession session = request.getSession(false);
		if (session == null) {
			return "redirect:login";
		} else {
			if (session.getAttribute("user") == null) {
				session.invalidate();
				return "redirect:login";
			}
		}
		
		User currentUser = ((User)session.getAttribute("user"));
		String currentUserEmail = currentUser.getEmail();
		System.err.println("Current User Email: " + currentUserEmail);
		
		
		String emailInput = request.getParameter("current_email");
		System.err.println("Input user email: " + emailInput);
		String newEmailInput = request.getParameter("new_email");
		String confirmNewEmailInput = request.getParameter("repeat_email");
		String passwordInput = request.getParameter("password");
		String confirmPasswordInput = request.getParameter("repeat_password");
		
		if(!emailInput.equals(currentUserEmail)){
			session.setAttribute("issueError", "Current user email dont match");
			return "redirect:profilepage";
		}
		
		if(!newEmailInput.equals(confirmNewEmailInput)){
			session.setAttribute("issueError", "email and confirm email fields does not match");
			return "redirect:profilepage";
		}
		
		try {
			int userId = UserDAO.getUserIdByEmail(emailInput);
			currentUser = UserDAO.getUserById(userId);
			
			Set<User> companyUsers = ((Set)session.getAttribute("companyUsers"));
			
			for(User companyUser : companyUsers){
				if(companyUser.getEmail().equals(newEmailInput)){
					session.setAttribute("issueError", "There is allready a user with that email in the company");
					return "redirect:profilepage";
				}
			}
			
			
			
			if(!passwordInput.equals(confirmPasswordInput)){
				session.setAttribute("issueError", "Passwords does not match");
				return "redirect:profilepage";
			}
			
			if(!currentUser.getPassword().equals(User.convertToMd5(passwordInput))){
				session.setAttribute("issueError", "Wrong Password");
				return "redirect:profilepage";
			}
			
			UserDAO.changeEmail(currentUser,newEmailInput);
			currentUser.setEmail(newEmailInput);
			
			session.setAttribute("succeed", "Succeed: email changed");
			session.setAttribute("user", currentUser);
		} catch (UserDAOException e) {
			session.setAttribute("issueError", e.getMessage());
		} catch (EmailException e) {
			session.setAttribute("issueError", "The email that you have entered is not valid");
		} catch (NoSuchAlgorithmException e) {
			session.setAttribute("issueError", "There was an error while editing email");
		} catch (UnsupportedEncodingException e) {
			session.setAttribute("issueError", "There was an error while editing email");
		} 

		return "redirect:profilepage";
	}
}
