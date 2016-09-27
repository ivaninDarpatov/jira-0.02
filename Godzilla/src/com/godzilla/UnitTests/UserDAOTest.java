package com.godzilla.UnitTests;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import com.godzilla.model.User;
import com.godzilla.model.DAO.UserDAO;

public class UserDAOTest {

	@Test
	public void registerNewUserTest() {
		int randomNumber = new Random().nextInt(100000);
		String email = "someEmail" + randomNumber + "@abv.bg";
		String password = "somePassword1";
		String company = "someCompany" + randomNumber;
		
		
		User userToReg = new User(email,password,company);
		UserDAO.registerUser(userToReg);
		
		Assert.assertTrue(userToReg.getId() > 0);
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
