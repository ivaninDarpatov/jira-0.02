package com.godzilla.UnitTests;



import org.junit.Assert;
import org.junit.Test;

import com.godzilla.model.Company;
import com.godzilla.model.DAO.CompanyDAO;
import com.godzilla.model.DAO.UserDAO;
import com.godzilla.model.exceptions.CompanyDAOException;
import com.godzilla.model.exceptions.CompanyException;

public class CompanyDAOTest {

	@Test
	public void testCreationOfComapny() throws CompanyException, CompanyDAOException {
		try {
			Company testCompany = CompanyDAO.getCompanyById(2);
			//UserDAO.getUserById(2);
			CompanyDAO.removeCompany(testCompany);
		} catch (Exception e) {
			throw e;
//			StringBuilder builder = new StringBuilder();
//			DAOTest.appendExceptions(builder, e);
//			System.out.println(builder.toString());
		}
		
	}

}
