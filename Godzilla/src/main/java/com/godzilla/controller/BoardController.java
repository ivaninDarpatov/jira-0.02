package com.godzilla.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.Issue;
import com.godzilla.model.Project;
import com.godzilla.model.Sprint;
import com.godzilla.model.DAO.IssueDAO;
import com.godzilla.model.DAO.SprintDAO;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.SprintDAOException;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/board")
public class BoardController {
	@RequestMapping(method = RequestMethod.GET)
	public String board(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (request.getSession(false) == null) {
			return "redirect:login";
		}if (session.getAttribute("user") == null) {
			session.invalidate();
			return "redirect:login";
		}
		
		session = request.getSession();
		
		Gson jsonMaker = new Gson();
		String userJSON = jsonMaker.toJson(session.getAttribute("user"));
		session.setAttribute("userJSON", userJSON);
		
		return "Board";
	}
}
