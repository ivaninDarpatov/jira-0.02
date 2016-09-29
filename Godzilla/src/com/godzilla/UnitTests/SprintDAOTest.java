package com.godzilla.UnitTests;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import com.godzilla.model.Project;
import com.godzilla.model.Sprint;
import com.godzilla.model.DAO.SprintDAO;
import com.godzilla.model.exceptions.ProjectException;
import com.godzilla.model.exceptions.SprintDAOException;
import com.godzilla.model.exceptions.SprintException;

public class SprintDAOTest {

	@Test
	public void addSprint() throws SprintDAOException, SprintException, ProjectException {
		Project p = new Project("someProjectt");
		p.setId(1);
		
		SprintDAO.addSprint(new Sprint("SomeOthe1rSprintt"), p);
	}
	
	@Test
	public void getSprintsByProjectId() throws SprintDAOException{
		Set<Sprint> sprints = SprintDAO.getAllSprintsByProjectId(1);
		
		for(Sprint s : sprints){
			System.out.println(s);
		}
		
		
	}

}
