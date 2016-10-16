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
@RequestMapping(value = "/changeAvatar")
public class ChangeAvatarController {
	
	@RequestMapping(method = RequestMethod.POST)
	public String changeAvatar(HttpServletRequest request){
		
		HttpSession session = request.getSession(false);
		if (session == null) {
			return "redirect:login";
		} else {
			if (session.getAttribute("user") == null) {
				session.invalidate();
				return "redirect:login";
			}
		}
		
		User currentUser = (User) session.getAttribute("user");
		String newNinjaColor = request.getParameter("avatar");
		
		try {
			UserDAO.changeAvatar(currentUser, newNinjaColor);
		} catch (UserDAOException e) {
			session.setAttribute("issueError", e.getMessage());
		}
		
		return "redirect:profilepage";
	}
}
