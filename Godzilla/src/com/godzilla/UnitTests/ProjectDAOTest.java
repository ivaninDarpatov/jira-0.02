package com.godzilla.UnitTests;

import org.junit.Assert;
import org.junit.Test;

import com.godzilla.model.Company;
import com.godzilla.model.Project;
import com.godzilla.model.DAO.CompanyDAO;
import com.godzilla.model.DAO.ProjectDAO;
import com.godzilla.model.exceptions.CompanyDAOException;
import com.godzilla.model.exceptions.ProjectDAOException;
import com.godzilla.model.exceptions.ProjectException;

public class ProjectDAOTest {

	@Test
	public void newProjectTest() throws ProjectException, ProjectDAOException {
		Project project = new Project("Test project3");
		StringBuilder builder = new StringBuilder();
		try {
			Company company = CompanyDAO.getCompanyById(2);
			
			ProjectDAO.addProject(project, company);
			
			Assert.assertTrue(project.getId() > 0);
		} catch (CompanyDAOException e) {
			DAOTest.appendExceptions(builder, e);
			System.out.println(builder.toString());
		}
	}

}
