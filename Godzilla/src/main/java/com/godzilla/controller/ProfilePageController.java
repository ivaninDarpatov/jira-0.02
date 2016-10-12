package com.godzilla.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.User;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/profilepage")
public class ProfilePageController {

	@RequestMapping(method = RequestMethod.GET)
	public String profilePage(HttpServletRequest request) {
		if (request.getSession(false) == null) {
			return "redirect:login";
		}
		
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("user");
		
		Gson jsonMaker = new Gson();
		String userJSON = jsonMaker.toJson(currentUser);
		
		session.setAttribute("userJSON", userJSON);
		
		return "ProfilePage";
	}
}
