package com.godzilla.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.godzilla.model.User;
import com.godzilla.model.DAO.UserDAO;
import com.godzilla.model.exceptions.UserDAOException;
import com.godzilla.model.exceptions.UserException;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("view/LogInForm.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String companyName = request.getParameter("companyName");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		User userToLogIn = null;
		StringBuilder builder = new StringBuilder();
		try {
			userToLogIn = new User(email, password,companyName);
			try {
				if(UserDAO.validateLogin(userToLogIn)){
					response.getWriter().println("<html> <body> <h1> uspeshen login </h1> </body> </html>");
				} else {
					builder.append("Not registered user");
				}
			} catch (UserDAOException e) {
				builder.append(e.getMessage());
			}
		} catch (UserException e) {
			builder.append(e.getMessage());
		}
		
		
		
		response.getWriter().println("<html> <body> <h1>" + builder.toString() + "</h1> </body> </html>");
		
		
	}

}
