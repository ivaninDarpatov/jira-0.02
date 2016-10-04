package com.godzilla.UnitTests;

import java.util.Random;

import javax.annotation.Priority;

import org.junit.Assert;
import org.junit.Test;

import com.godzilla.model.User;
import com.godzilla.model.DAO.UserDAO;
import com.godzilla.model.exceptions.UserDAOException;
import com.godzilla.model.exceptions.UserException;

@SuppressWarnings("all")
public class UserDAOTest {

	//@Test
	public void getUserById() {
		int userId = 1;
		
		try {
			User user = UserDAO.getUserById(userId);
			System.out.println(user);
		} catch (UserDAOException e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void registerTwoUsers(){
		try {
			User testUser = new User("ivan2@abv.bg", "password2","company1");
			
			UserDAO.registerUser(testUser);
			
			
		} catch (UserException e) {
			Assert.assertTrue(e.getMessage().equals("Invalid email")
					||	e.getMessage().equals("Invalid password")
					||	e.getMessage().equals("Invalid permissions")
					||	e.getMessage().equals("Invalid Company"));
		} catch (UserDAOException e) {
			Assert.assertTrue(e.getMessage().equals("couldn't create company") 
					|| e.getMessage().equals("failed to create company")
					|| e.getMessage().equals("invalid permissions")
					|| e.getMessage().equals("failed to register"));
			e.printStackTrace();
		}
	}
	
	//@Test
	public void removeUser(){
		int userId = 10;
		try {
			User user = UserDAO.getUserById(userId);
			UserDAO.removeUser(user);
		} catch (UserDAOException e) {
			Assert.assertTrue(e.getMessage().equals("couldn't create company") 
					|| e.getMessage().equals("failed to create company")
					|| e.getMessage().equals("invalid permissions")
					|| e.getMessage().equals("failed to register")
					|| e.getMessage().equals("can't find user to remove")
					|| e.getMessage().equals("failed to get issues")
					|| e.getMessage().equals("failed to remove user"));
		}
	}
}
