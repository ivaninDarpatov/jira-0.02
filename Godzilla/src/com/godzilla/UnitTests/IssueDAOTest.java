package com.godzilla.UnitTests;

import static org.junit.Assert.*;

import java.time.format.DateTimeParseException;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.godzilla.model.Bug;
import com.godzilla.model.Epic;
import com.godzilla.model.Issue;
import com.godzilla.model.Project;
import com.godzilla.model.Story;
import com.godzilla.model.Task;
import com.godzilla.model.User;
import com.godzilla.model.DAO.IssueDAO;
import com.godzilla.model.DAO.ProjectDAO;
import com.godzilla.model.DAO.UserDAO;
import com.godzilla.model.exceptions.EpicException;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.IssueException;
import com.godzilla.model.exceptions.ProjectDAOException;
import com.godzilla.model.exceptions.ProjectException;
import com.godzilla.model.exceptions.UserDAOException;
import com.godzilla.model.exceptions.UserException;



@SuppressWarnings("all")
public class IssueDAOTest {

	@Test
	public void addBugTest(){
		
		try {
			int projectId = ProjectDAO.getProjectIdByName("project 1");
			Project project = ProjectDAO.getProjectById(projectId);
			
			int reporterId = UserDAO.getUserIdByEmail("user1@abv.bg");
			User reporter = UserDAO.getUserById(reporterId);
		
			Issue issue = new Bug("Bug");
			
			IssueDAO.createIssue(issue, project, reporter);
			
			Assert.assertTrue(issue.getId() > 0);
		} catch (IssueException e) {
			Assert.assertTrue(e.getMessage().equals("issue's summary cannot be null") 
					|| e.getMessage().equals("issue priority cannot be null")
					|| e.getMessage().equals("issue state cannot be null"));
		} catch (ProjectDAOException e) {
			Assert.assertTrue(e.getMessage().equals("couldn't find a project with that name")
					|| e.getMessage().equals("failed to create project")
					|| e.getMessage().equals("can't find user with that id")
					|| e.getMessage().equals("there is no such user")
					|| e.getMessage().equals("user not created")
					|| e.getMessage().equals("permissions not set")
					|| e.getMessage().equals("failed to get issues"));
		} catch (UserDAOException e) {
			Assert.assertTrue(e.getMessage().equals("couldn't find a user with that email")
					|| e.getMessage().equals("invalid project id")
					|| e.getMessage().equals("failed to create project")
					|| e.getMessage().equals("failed to get sprints")
					|| e.getMessage().equals("failed to get issues"));
		} catch (IssueDAOException e) {
			Assert.assertTrue(
					   e.getMessage().equals("couldn't set issue's id")
					|| e.getMessage().equals("Ne bqha napraveni promeni")
					|| e.getMessage().equals("cannot create issue, required: project, reporter and issue model")
					|| e.getMessage().equals("could not set id of an issue")
					|| e.getMessage().equals("failed to create issue"));
		}
	}
	@Test
	public void addTaskTest(){
		
		try {
			int projectId = ProjectDAO.getProjectIdByName("project 1");
			Project project = ProjectDAO.getProjectById(projectId);
			
			int reporterId = UserDAO.getUserIdByEmail("user1@abv.bg");
			User reporter = UserDAO.getUserById(reporterId);
		
			Issue issue = new Task("Task");
			
			IssueDAO.createIssue(issue, project, reporter);
			
			Assert.assertTrue(issue.getId() > 0);
		} catch (IssueException e) {
			Assert.assertTrue(e.getMessage().equals("issue's summary cannot be null") 
					|| e.getMessage().equals("issue priority cannot be null")
					|| e.getMessage().equals("issue state cannot be null"));
		} catch (ProjectDAOException e) {
			Assert.assertTrue(e.getMessage().equals("couldn't find a project with that name")
					|| e.getMessage().equals("can't find user with that id")
					|| e.getMessage().equals("there is no such user")
					|| e.getMessage().equals("user not created")
					|| e.getMessage().equals("permissions not set")
					|| e.getMessage().equals("failed to get issues"));
		} catch (UserDAOException e) {
			Assert.assertTrue(e.getMessage().equals("couldn't find a user with that email")
					|| e.getMessage().equals("invalid project id")
					|| e.getMessage().equals("failed to create project")
					|| e.getMessage().equals("failed to get sprints")
					|| e.getMessage().equals("failed to get issues"));
		} catch (IssueDAOException e) {
			Assert.assertTrue(
					   e.getMessage().equals("couldn't set issue's id")
					|| e.getMessage().equals("Ne bqha napraveni promeni")
					|| e.getMessage().equals("cannot create issue, required: project, reporter and issue model")
					|| e.getMessage().equals("could not set id of an issue")
					|| e.getMessage().equals("failed to create issue"));
		}
	}
	@Test
	public void addStoryTest(){
		
		try {
			int projectId = ProjectDAO.getProjectIdByName("project 1");
			Project project = ProjectDAO.getProjectById(projectId);
			
			int reporterId = UserDAO.getUserIdByEmail("user1@abv.bg");
			User reporter = UserDAO.getUserById(reporterId);
		
			Issue issue = new Story("Story");
			
			IssueDAO.createIssue(issue, project, reporter);
			
			Assert.assertTrue(issue.getId() > 0);
		} catch (IssueException e) {
			Assert.assertTrue(e.getMessage().equals("issue's summary cannot be null") 
					|| e.getMessage().equals("issue priority cannot be null")
					|| e.getMessage().equals("issue state cannot be null"));
		} catch (ProjectDAOException e) {
			Assert.assertTrue(e.getMessage().equals("couldn't find a project with that name")
					|| e.getMessage().equals("can't find user with that id")
					|| e.getMessage().equals("there is no such user")
					|| e.getMessage().equals("user not created")
					|| e.getMessage().equals("permissions not set")
					|| e.getMessage().equals("failed to get issues"));
		} catch (UserDAOException e) {
			Assert.assertTrue(
					   e.getMessage().equals("couldn't find a user with that email")
					|| e.getMessage().equals("invalid project id")
					|| e.getMessage().equals("failed to create project")
					|| e.getMessage().equals("failed to get sprints")
					|| e.getMessage().equals("failed to get issues"));
		} catch (IssueDAOException e) {
			Assert.assertTrue(
					   e.getMessage().equals("couldn't set issue's id")
					|| e.getMessage().equals("Ne bqha napraveni promeni")
					|| e.getMessage().equals("cannot create issue, required: project, reporter and issue model")
					|| e.getMessage().equals("could not set id of an issue")
					|| e.getMessage().equals("failed to create issue"));
		}
	}
	@Test
	public void addEpicTest(){
		
		try {
			int projectId = ProjectDAO.getProjectIdByName("project 1");
			Project project = ProjectDAO.getProjectById(projectId);
			
			int reporterId = UserDAO.getUserIdByEmail("user1@abv.bg");
			User reporter = UserDAO.getUserById(reporterId);
		
			Issue issue = new Epic("Epic");
			((Epic) issue).setName("epic 1");
			
			IssueDAO.createIssue(issue, project, reporter);
			
			Assert.assertTrue(issue.getId() > 0);
		} catch (IssueException e) {
			Assert.assertTrue(e.getMessage().equals("issue's summary cannot be null") 
					|| e.getMessage().equals("issue priority cannot be null")
					|| e.getMessage().equals("issue state cannot be null"));
		} catch (ProjectDAOException e) {
			e.printStackTrace();
			Assert.assertTrue(e.getMessage().equals("couldn't find a project with that name")
					|| e.getMessage().equals("can't find user with that id")
					|| e.getMessage().equals("there is no such user")
					|| e.getMessage().equals("user not created")
					|| e.getMessage().equals("permissions not set")
					|| e.getMessage().equals("failed to get issues"));
		} catch (UserDAOException e) {
			Assert.assertTrue(e.getMessage().equals("couldn't find a user with that email")
					|| e.getMessage().equals("invalid project id")
					|| e.getMessage().equals("failed to create project")
					|| e.getMessage().equals("failed to get sprints")
					|| e.getMessage().equals("failed to get issues"));
		} catch (IssueDAOException e) {
			Assert.assertTrue(
					   e.getMessage().equals("couldn't set issue's id")
					|| e.getMessage().equals("Ne bqha napraveni promeni")
					|| e.getMessage().equals("cannot create issue, required: project, reporter and issue model")
					|| e.getMessage().equals("could not set id of an issue")
					|| e.getMessage().equals("failed to create issue"));
		} catch (EpicException e) {
			Assert.assertTrue(e.getMessage().equals("epic's name cannot be null"));
		}
	}
	
	//@Test
	public void createRemoveBugTest() {
		try {
			int projectId = ProjectDAO.getProjectIdByName("project 1");
			Project project = ProjectDAO.getProjectById(projectId);
			
			int reporterId = UserDAO.getUserIdByEmail("user1@abv.bg");
			User reporter = UserDAO.getUserById(reporterId);
		
			Issue issue = new Bug("Bug");
			
			IssueDAO.createIssue(issue, project, reporter);
			Assert.assertTrue(issue.getId() > 0);
			int issueId = issue.getId();
			IssueDAO.removeIssue(issue);
			
			
		} catch (IssueException e) {
			Assert.assertTrue(e.getMessage().equals("issue's summary cannot be null") 
					|| e.getMessage().equals("issue priority cannot be null")
					|| e.getMessage().equals("issue state cannot be null"));
		} catch (ProjectDAOException e) {
			Assert.assertTrue(e.getMessage().equals("couldn't find a project with that name")
					|| e.getMessage().equals("failed to create project")
					|| e.getMessage().equals("can't find user with that id")
					|| e.getMessage().equals("there is no such user")
					|| e.getMessage().equals("user not created")
					|| e.getMessage().equals("permissions not set")
					|| e.getMessage().equals("failed to get issues"));
		} catch (UserDAOException e) {
			Assert.assertTrue(e.getMessage().equals("couldn't find a user with that email")
					|| e.getMessage().equals("invalid project id")
					|| e.getMessage().equals("failed to create project")
					|| e.getMessage().equals("failed to get sprints")
					|| e.getMessage().equals("failed to get issues"));
		} catch (IssueDAOException e) {
			Assert.assertTrue(
					   e.getMessage().equals("couldn't set issue's id")
					|| e.getMessage().equals("Ne bqha napraveni promeni")
					|| e.getMessage().equals("cannot create issue, required: project, reporter and issue model")
					|| e.getMessage().equals("could not set id of an issue")
					|| e.getMessage().equals("failed to create issue")
					|| e.getMessage().equals("invalid issue to remove"));
		}
		
		
	}
}