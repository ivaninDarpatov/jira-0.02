package com.godzilla.UnitTests;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.godzilla.model.Project;
import com.godzilla.model.Sprint;
import com.godzilla.model.DAO.ProjectDAO;
import com.godzilla.model.DAO.SprintDAO;
import com.godzilla.model.exceptions.ProjectDAOException;
import com.godzilla.model.exceptions.ProjectException;
import com.godzilla.model.exceptions.SprintDAOException;
import com.godzilla.model.exceptions.SprintException;


@SuppressWarnings("all")
public class SprintDAOTest {

	@Test
	public void addSprintTest() {
		try {
			int projectId = ProjectDAO.getProjectIdByName("project 1");
			Project project = ProjectDAO.getProjectById(projectId);
			
			Sprint sprint = new Sprint("sprint 1", "goal 1");
			
			SprintDAO.addSprint(sprint, project);
			
		} catch (SprintException e) {
					Assert.assertTrue(e.getMessage().equals("cannot add issue with a null value"));
		} catch (ProjectDAOException e) {
			Assert.assertTrue(e.getMessage().equals("couldn't find a project with that name")
					|| e.getMessage().equals("failed to create project")
					|| e.getMessage().equals("can't find user with that id")
					|| e.getMessage().equals("there is no such user")
					|| e.getMessage().equals("user not created")
					|| e.getMessage().equals("permissions not set")
					|| e.getMessage().equals("failed to get issues"));
		} catch (SprintDAOException e) {
			Assert.assertTrue(
					e.getMessage().equals("can't add sprint, to do so you need a valid project and sprint model") ||
					e.getMessage().equals("failed to create sprint") ||
					e.getMessage().equals("couldn't set sprint id"));
		}
		
	}
}
