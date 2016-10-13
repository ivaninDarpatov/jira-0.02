package com.godzilla.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.User;
import com.godzilla.model.DAO.UserDAO;
import com.godzilla.model.exceptions.UserDAOException;

@Controller
@RequestMapping(value="/changePassword")
public class ChangePasswordController {

	@RequestMapping(method = RequestMethod.POST)
	public String changePassword(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if (session == null) {
			return "redirect:login";
		} else {
			if (session.getAttribute("user") == null) {
				session.invalidate();
				return "redirect:login";
			}
		}
		//current_pass,conf_current_pass,new_password,conf_new_password
		String currentPasswordInput = request.getParameter("current_pass");
		String confirmCurrentPasswordInput = request.getParameter("conf_current_pass");
		String newPasswordInput = request.getParameter("new_password");
		String confirmNewPasswordInput = request.getParameter("conf_new_password");
		
		User currentUser = ((User)session.getAttribute("user"));
		String currentPassword = currentUser.getPassword();
		
		if(!currentPasswordInput.equals(confirmCurrentPasswordInput)){
			session.setAttribute("issueError", "Current Password and Confirm current Password does not match");
			return "redirect:profilepage";
		}
		
		if(!currentPasswordInput.equals(currentPassword)){
			session.setAttribute("issueError", "Wrong password");
			return "redirect:profilepage";
		}
		
		if(!newPasswordInput.equals(confirmNewPasswordInput)){
			session.setAttribute("issueError", "New password and Confirm password does not match");
			return "redirect:profilepage";
		}
		
		if(!User.validatePassword(newPasswordInput)){
			session.setAttribute("issueError", "Your new password must be 8 characters long and must contain atleast one character and one digit");
			return "redirect:profilepage";
		}
		
		try {
			UserDAO.changePassword(currentUser, newPasswordInput);
			currentUser = UserDAO.getUserById(currentUser.getId());
			session.setAttribute("user", currentUser);
			session.setAttribute("succeed", "Succeed: Password changed");
		} catch (UserDAOException e) {
			session.setAttribute("issueError", e.getMessage());
		}
		
		return "redirect:profilepage";
	}
}
