package com.godzilla.UnitTests;

import org.junit.Assert;
import org.junit.Test;

import com.godzilla.model.Project;
import com.godzilla.model.DAO.ProjectDAO;
import com.godzilla.model.exceptions.ProjectDAOException;
import com.godzilla.model.exceptions.ProjectException;

public class ProjectDAOTest {

	@Test
	public void newProjectTest() throws ProjectException, ProjectDAOException {
		Project project = new Project("Test project3");
		
		ProjectDAO.addProject(project, "Company");
		
		Assert.assertTrue(project.getId() > 0);
	}

}
