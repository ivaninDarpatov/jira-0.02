package com.godzilla.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.Company;
import com.godzilla.model.User;
import com.godzilla.model.DAO.CompanyDAO;
import com.godzilla.model.DAO.UserDAO;
import com.godzilla.model.exceptions.CompanyDAOException;
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
		try {
		if(!User.convertToMd5(passwordInput).equals(currentUserPassword)){
			session.setAttribute("issueError", "Wrong password");
			return "redirect:profilepage";
		}
		
			if (!currentUser.isManager()) {
				UserDAO.removeUser(currentUser);
			} else {
				//if the user is the manager -> delete the whole company
				String companyName = currentUser.getCompany();
				int companyId = CompanyDAO.getIdOfCompanyWithName(companyName);
				Company companyToRemove = CompanyDAO.getCompanyById(companyId);
				
				CompanyDAO.removeCompany(companyToRemove);
			}
		} catch (UserDAOException e) {
			session.setAttribute("issueError", "Could not remove user");
			return "redirect:profilepage";
		} catch (CompanyDAOException e) {
			return "redirect:login";
		} catch (NoSuchAlgorithmException e) {
			session.setAttribute("issueError", "There was a problem while deleting user");
		} catch (UnsupportedEncodingException e) {
			session.setAttribute("issueError", "There was a problem while deleting user");
		}
		
		session.invalidate();
		return "redirect:login";
	}
}
