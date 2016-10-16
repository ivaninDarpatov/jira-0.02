package com.godzilla.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.Company;
import com.godzilla.model.User;
import com.godzilla.model.DAO.UserDAO;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/profilepage")
public class ProfilePageController {

	@RequestMapping(method = RequestMethod.GET)
	public String profilePage(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		if (session == null) {
			return "redirect:login";
		}
		if (session.getAttribute("user") == null) {
			session.invalidate();
			return "redirect:login";
		}

		try {
			Company company = (Company) session.getAttribute("company");
			Set<User> companyUsers = UserDAO.getAllUsersByCompany(company);
			User currentUser = (User) session.getAttribute("user");
			currentUser = UserDAO.getUserById(currentUser.getId());

			Gson jsonMaker = new Gson();
			String userJSON = jsonMaker.toJson(currentUser);
			
			session.setAttribute("companyUsers", companyUsers);
			session.setAttribute("userJSON", userJSON);
			session.setAttribute("user", currentUser);
		} catch (Exception e) {
			session.setAttribute("error", e.getMessage());
		}
		return "ProfilePage";
	}
}
