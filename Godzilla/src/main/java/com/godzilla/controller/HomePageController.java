package com.godzilla.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/HomePage")
public class HomePageController {

	@RequestMapping(method = RequestMethod.GET)
	public String homePage(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if(session == null){
			return "redirect:login";
		} else {
			if (session.getAttribute("user") == null) {
				session.invalidate();
				return "redirect:login";
			}
		}
		
		return "HomePage";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String homePage(HttpServletRequest request,HttpServletResponse response,HttpSession Usersession){
		
		
		
		
		return "HomePage";
	}
}
