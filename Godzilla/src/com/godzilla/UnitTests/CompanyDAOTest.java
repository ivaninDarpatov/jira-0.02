package com.godzilla.UnitTests;



import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import com.godzilla.model.Company;
import com.godzilla.model.Project;
import com.godzilla.model.User;
import com.godzilla.model.DAO.CompanyDAO;
import com.godzilla.model.DAO.StaticValuesInitializerDAO;
import com.godzilla.model.exceptions.CompanyDAOException;
import com.godzilla.model.exceptions.CompanyException;
import com.godzilla.model.exceptions.UserException;

@SuppressWarnings("all")
public class CompanyDAOTest {

	@Test
	public void registerCompany(){
		try {
			Company company = new Company("Company 1");
			CompanyDAO.createNewCompany(company);
			int companyId = company.getId();
			Assert.assertTrue(companyId > 0);
			
		} catch (CompanyDAOException e) {
			Assert.assertTrue(e.getMessage().equals("company's name is taken"));
			
		} catch (CompanyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
