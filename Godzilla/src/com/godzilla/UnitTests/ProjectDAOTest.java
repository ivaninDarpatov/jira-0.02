package com.godzilla.UnitTests;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.godzilla.model.Company;
import com.godzilla.model.Issue;
import com.godzilla.model.Project;
import com.godzilla.model.DAO.CompanyDAO;
import com.godzilla.model.DAO.IssueDAO;
import com.godzilla.model.DAO.ProjectDAO;
import com.godzilla.model.exceptions.CompanyDAOException;
import com.godzilla.model.exceptions.CompanyException;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.ProjectDAOException;
import com.godzilla.model.exceptions.ProjectException;

public class ProjectDAOTest {

	@Test
	public void newProjectTest() throws ProjectException, ProjectDAOException, CompanyDAOException {
		Project project = new Project("Test project22");
		int companyId = CompanyDAO.getIdOfCompanyWithName("Test Company2");
		Company company = CompanyDAO.getCompanyById(companyId);
		
		ProjectDAO.addProject(project, company);
		
		Assert.assertTrue(project.getId() > 0);
	}
	
	@Test
	public void getAllProjectsByCompany() throws CompanyException, ProjectDAOException{
		Company company = new Company("Some company");
		company.setId(1);
		
		Set<Project> projects = ProjectDAO.getAllProjectsByCompany(company);
		System.out.println(projects.size());
		
		for(Project p : projects){
			System.out.println(p);
		}
	}
	
//	@Test
//	public void deleteProjectTest(){
//		
//		ProjectDAO.
//	}

}
