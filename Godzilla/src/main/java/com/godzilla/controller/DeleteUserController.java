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
@RequestMapping(value = "/deleteUser")
public class DeleteUserController {

	@RequestMapping(method = RequestMethod.POST)
	public String deletePassword(HttpServletRequest request){
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
		String currentUserPassword = currentUser.getPassword();
		
		String passwordInput = request.getParameter("delete_password");
		String confirmPasswordInput = request.getParameter("delete_conf_password");
		
		if(!passwordInput.equals(confirmPasswordInput)){
			session.setAttribute("issueError", "Password and Confirm password does not match");
			return "redirect:profilepage";
		}
		
		if(!passwordInput.equals(currentUserPassword)){
			session.setAttribute("issueError", "Wrong password");
			return "redirect:profilepage";
		}
		
		try {
			UserDAO.removeUser(currentUser);
		} catch (UserDAOException e) {
			session.setAttribute("issueError", "Could not remove user");
			return "redirect:profilepage";
		}
		
		session.invalidate();
		return "redirect:login";
	}
}