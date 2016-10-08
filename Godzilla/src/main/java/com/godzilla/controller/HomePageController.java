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
	public String homePage(){
		return "HomePage";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String homePage(HttpServletRequest request,HttpServletResponse response,HttpSession Usersession){
		
		String summary = request.getParameter("summary");
		System.err.println(request.getParameter("issue_type"));
		
		return "HomePage";
	}
}
