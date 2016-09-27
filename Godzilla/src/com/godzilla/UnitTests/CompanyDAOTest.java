package com.godzilla.UnitTests;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import com.godzilla.model.Company;
import com.godzilla.model.DAO.CompanyDAO;

public class CompanyDAOTest {

	@Test
	public void testCreationOfComapny() {
		Company testCompany = new Company("Test Company");
		 
		CompanyDAO.createNewCompany(testCompany);
		System.out.println(testCompany.getId());
		
		Assert.assertTrue(testCompany.getId() > 0);
		
		
	}

}
