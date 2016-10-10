package com.godzilla.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/createissue")
public class PopUpController {

	@RequestMapping(method = RequestMethod.GET)
	public String showPopUp(){
		return "CreateIssue";
	}
}
