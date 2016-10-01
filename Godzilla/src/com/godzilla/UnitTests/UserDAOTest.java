package com.godzilla.UnitTests;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import com.godzilla.model.User;
import com.godzilla.model.DAO.UserDAO;
import com.godzilla.model.exceptions.UserDAOException;
import com.godzilla.model.exceptions.UserException;

public class UserDAOTest {

	@Test
	public void registerNewUserTest() {
		try {
			User testUser = new User("ivan1@abv.bg", "123456abc", "company1");
			UserDAO.registerUser(testUser);
			
			System.out.println(testUser.getId());
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			DAOTest.appendExceptions(sb, e);
			
			System.out.println(sb.toString());
			e.printStackTrace();
		}
	}
	
//	@Test
//	public void registerUserWithTheSameName(){
//		int randomNumber = new Random().nextInt(100000);
//		String email = "sameEmail" + randomNumber + "@abv.bg";
//		String password = "samePassword1";
//		String company = "sameCompany" + randomNumber;
//		
//		
//		User userToReg = new User(email,password,company);
//		UserDAO.registerUser(userToReg);
//		
//		UserDAO.registerUser(userToReg);
//	}
	
//	@Test
//	public void removeUser(User userToRemove){
//		
//	}

}
