package com.godzilla.UnitTests;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import com.godzilla.model.Project;
import com.godzilla.model.DAO.ProjectDAO;

public class ProjectDAOTest {

	@Test
	public void newProjectTest() {
		Project project = new Project("Test project","someCompanys");
		
		ProjectDAO.addProject(project, "test1");
		
		Assert.assertTrue(project.getId() == 0);
	}

}
