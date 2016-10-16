package com.godzilla.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/profilepage")
public class ProfilePageController {

	@RequestMapping(method = RequestMethod.GET)
	public String profilePage(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		
		if (session == null) {
			return "redirect:login";
		}if (session.getAttribute("user") == null) {
			session.invalidate();
			return "redirect:login";
		}
		
		Gson jsonMaker = new Gson();
		String userJSON = jsonMaker.toJson(session.getAttribute("user"));
		session.setAttribute("userJSON", userJSON);
		
		return "ProfilePage";
	}
}
