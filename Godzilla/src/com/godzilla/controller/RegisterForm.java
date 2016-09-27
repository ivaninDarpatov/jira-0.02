package com.godzilla.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.Messaging.SyncScopeHelper;

import com.godzilla.model.User;
import com.godzilla.model.DAO.UserDAO;
import com.godzilla.model.exceptions.UserDAOException;
import com.godzilla.model.exceptions.UserException;

/**
 * Servlet implementation class RegisterForm
 */
@WebServlet("/Register")
public class RegisterForm extends HttpServlet {
	
	private static final long serialVersionUID = -3829303115685751168L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("view/RegisterForm.jsp");
		rd.forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String company = request.getParameter("company");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("conf_password");
		
		StringBuilder exceptionMessage = new StringBuilder();
		
		if(password.equals(confirmPassword)){
			User userToReg = null;
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
		
		
		response.getWriter().println("<html> <body> <h1>" + exceptionMessage.toString() + "</h1> </body> </html>");
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
