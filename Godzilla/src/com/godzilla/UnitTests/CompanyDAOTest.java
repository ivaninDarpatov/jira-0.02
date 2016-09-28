package com.godzilla.UnitTests;



import org.junit.Assert;
import org.junit.Test;

import com.godzilla.model.Company;
import com.godzilla.model.DAO.CompanyDAO;
import com.godzilla.model.exceptions.CompanyDAOException;
import com.godzilla.model.exceptions.CompanyException;

public class CompanyDAOTest {

	@Test
	public void testCreationOfComapny() throws CompanyException, CompanyDAOException {
		Company testCompany = new Company("Test Company");
		 
		CompanyDAO.createNewCompany(testCompany);
		System.out.println(testCompany.getId());
		
		Assert.assertTrue(testCompany.getId() > 0);
		
		
	}

}
