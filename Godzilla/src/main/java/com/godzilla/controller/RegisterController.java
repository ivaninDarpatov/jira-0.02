package com.godzilla.controller;


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
import com.godzilla.model.exceptions.CompanyException;
import com.godzilla.model.exceptions.UserDAOException;
import com.godzilla.model.exceptions.UserException;



@Controller
@RequestMapping(value = "/registration")
public class RegisterController {

	@RequestMapping(method = RequestMethod.GET)
	public String register(){
		return "RegisterForm";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String register(HttpServletRequest request,HttpSession userSession){
		HttpSession session = request.getSession(false);
		session.invalidate();
		session = request.getSession();
		String companyName = request.getParameter("company");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("conf_password");
		
		User userToReg = null;
		Company company = null;
		if(password.equals(confirmPassword)){

			try {
				userToReg = new User(email, password, companyName);
				UserDAO.registerUser(userToReg);
				
				userToReg = UserDAO.getUserById(userToReg.getId());
				
				int companyId = CompanyDAO.getIdOfCompanyWithName(companyName);
				company = CompanyDAO.getCompanyById(companyId);
			
				
				session.setAttribute("company", company);
				session.setAttribute("user", userToReg);
				session.setAttribute("companyUsers", company.getUsers());
				session.setAttribute("companyProjects", company.getProjects());
				
			} catch (UserException | UserDAOException | CompanyDAOException | CompanyException   e) {
				e.printStackTrace();
				session.setAttribute("error", e.getMessage());
				return "redirect:registration";
			}
			
		} else {
			session.setAttribute("error", "Passwords dont match");
			return "redirect:registration";
		}
		return "redirect:homepage";
	}
}
