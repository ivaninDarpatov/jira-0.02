package com.godzilla.UnitTests;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import com.godzilla.model.Bug;
import com.godzilla.model.Epic;
import com.godzilla.model.Issue;
import com.godzilla.model.Project;
import com.godzilla.model.Story;
import com.godzilla.model.User;
import com.godzilla.model.DAO.IssueDAO;
import com.godzilla.model.DAO.UserDAO;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.ProjectException;
import com.godzilla.model.exceptions.UserException;


public class IssueDAOTest {

//	@Test
//	public void addIssue() throws IssueDAOException, ProjectException, UserException {
//		Issue issue = new Epic("Task"); 
//		if(issue instanceof Epic){
//			((Epic)issue).setName("Epic");
//		}
//		Project project = new Project("someProject");
//		project.setId(1);
//		User user =  new User("someEmail@abv.bg", "somePassword11");
//		user.setId(1);
//		
//		IssueDAO.createIssue(issue, project, user);
//	}
//	
//	@Test
//	public void getIssueByIdTest() throws IssueDAOException{
//		Issue issue = IssueDAO.getIssueById(27);
//		System.err.println("Get issue by id");
//		System.out.println(issue);
//	}
//	
//	@Test
//	public void getIssuesByProjectTest() throws IssueDAOException, ProjectException{
//		Project project = new Project("someProject");
//		project.setId(1);
//		
//		Set<Issue> result = IssueDAO.getAllIssuesByProject(project);
//		
//		System.err.println("Issues by project");
//		for(Issue i : result){
//			System.out.println(i);
//		}
//	}
//	
//	@Test
//	public void getAllIssuesReportedByUser() throws IssueDAOException, UserException{
//		User user = new User("someUser@abv.bg", "somePassword1");
//		user.setId(1);
//		Set<Issue> result = IssueDAO.getAllReportedIssuesByUser(user);
//		
//		System.err.println("All issues by reporter:");
//		for(Issue i : result){
//			System.out.println(i);
//		}
//	}
//	
//	@Test
//	public void getAllIssuesByEpicTest() throws IssueDAOException{
//		Epic epic = new Epic("epic summary");
//		epic.setId(1);
//		Set<Issue> result = IssueDAO.getAllIssuesByEpic(epic);
//		
//		System.err.println("All issues by epic");
//		for(Issue i : result ){
//			System.out.println(i);
//		}
//	}
//
//	@Test
//	public void removeIssueTest() throws IssueDAOException{
//		Issue issue = new Epic("someSummary");
//		issue.setId(1);
//		IssueDAO.removeIssue(issue);
//	}
	
	@Test
	public void handToAdminTest() {
		try {
			Issue issue = new Story("summarito");
			issue.setId(3);
			IssueDAO.handToAdmin(issue);
		} catch (Exception e) {
			StringBuilder builder = new StringBuilder();
			DAOTest.appendExceptions(builder, e);
			System.out.println(builder.toString());
		}
	}
}
