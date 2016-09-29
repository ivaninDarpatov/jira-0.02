package com.godzilla.UnitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.godzilla.model.Issue;
import com.godzilla.model.Project;
import com.godzilla.model.Story;
import com.godzilla.model.User;
import com.godzilla.model.DAO.IssueDAO;
import com.godzilla.model.DAO.UserDAO;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.ProjectException;
import com.godzilla.model.exceptions.UserException;

public class DAOTest {

	@Test
	public void test() {
		
		try {
			Issue testIssue = new Story("test");
			Project testProject = new Project("testProject");
			testProject.setId(1);
			User testUser = new User("ivan@abv.bg", "123456abc", "testCompany");
			testUser.setId(12);
			
			IssueDAO.createIssue(testIssue, testProject, testUser);
		} catch (IssueDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
