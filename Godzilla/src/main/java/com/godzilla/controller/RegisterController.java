package com.godzilla.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.godzilla.model.User;
import com.godzilla.model.DAO.UserDAO;
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
	public String register(HttpServletRequest request){
		String company = request.getParameter("company");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("conf_password");
		
		StringBuilder exceptionMessage = new StringBuilder();
		User userToReg = null;
		if(password.equals(confirmPassword)){
			userToReg = null;
			try {
				userToReg = new User(email, password, company);
			} catch (UserException e1) {
				appendExceptions(exceptionMessage, e1);
				
			}
				try {
					UserDAO.registerUser(userToReg);
				} catch (UserException e) {
					appendExceptions(exceptionMessage, e);
				} catch (UserDAOException e) {
					appendExceptions(exceptionMessage, e);
				}
		} else {
			exceptionMessage.append("passwords !=");
		}
		request.setAttribute("user", userToReg);
		
//		response.getWriter().println("<html> <body> <h1>" + exceptionMessage.toString() + "</h1> </body> </html>");
		return "HomePage";
	}
	
	private void appendExceptions(StringBuilder builder,Throwable ex){
		
		builder.append(ex.getMessage() + "\n");
		
		if(ex.getCause() == null){
			return;
		}

		Throwable cause = ex.getCause();
		
		appendExceptions(builder, cause);
		
	}
	
}
