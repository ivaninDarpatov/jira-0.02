package com.godzilla.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.User;
import com.godzilla.model.DAO.UserDAO;
import com.godzilla.model.enums.Permissions;
import com.godzilla.model.exceptions.UserDAOException;

@Controller
@RequestMapping(value = "/updateuser")
public class UpdateUserController {
	@RequestMapping(method = RequestMethod.POST)
	public String updateUser(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session == null){
			return "redirect:login";
		} else {
			if (session.getAttribute("user") == null) {
				session.invalidate();
				return "redirect:login";
			}
		}
		
		String method = request.getParameter("update_method");
		try {
			String userEmail = request.getParameter("user_to_update");
			User toUpdate = UserDAO.getUserById(UserDAO.getUserIdByEmail(userEmail));
			if (method.equals("remove")) {
				UserDAO.removeUser(toUpdate);
			} else {
				if (method.equals("update")){
					int permissions = Integer.parseInt(request.getParameter("position"));
					Permissions newPermissions = Permissions.getPermissionsById(permissions);
					
					UserDAO.updatePermissions(toUpdate, newPermissions);
				}
			}
		
		} catch (UserDAOException e) {
			session.setAttribute("error", e.getMessage());
		}
		
		return "redirect:profilepage";
	}
}
