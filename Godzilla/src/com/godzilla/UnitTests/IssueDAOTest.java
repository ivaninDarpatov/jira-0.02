package com.godzilla.UnitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.godzilla.model.*;
import com.godzilla.model.DAO.*;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.ProjectDAOException;
import com.godzilla.model.exceptions.UserDAOException;

public class IssueDAOTest {

	@Test
	public void test() {
		User reporter;
		Project project;
		try {
			reporter = UserDAO.getUserById(2);
			project = ProjectDAO.getProjectById(1);
			Issue toCreate = new Epic("epic1");
			((Epic)toCreate).setName("epic1");
			
			IssueDAO.createIssue(toCreate, project, reporter);
		} catch (ProjectDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IssueDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
