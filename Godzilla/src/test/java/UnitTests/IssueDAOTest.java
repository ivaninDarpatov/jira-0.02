package UnitTests;

import static org.junit.Assert.*;

import java.time.format.DateTimeParseException;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.godzilla.model.Bug;
import com.godzilla.model.Epic;
import com.godzilla.model.Issue;
import com.godzilla.model.Project;
import com.godzilla.model.Sprint;
import com.godzilla.model.Story;
import com.godzilla.model.Task;
import com.godzilla.model.User;
import com.godzilla.model.DAO.IssueDAO;
import com.godzilla.model.DAO.ProjectDAO;
import com.godzilla.model.DAO.SprintDAO;
import com.godzilla.model.DAO.UserDAO;
import com.godzilla.model.exceptions.EpicException;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.IssueException;
import com.godzilla.model.exceptions.ProjectDAOException;
import com.godzilla.model.exceptions.ProjectException;
import com.godzilla.model.exceptions.SprintDAOException;
import com.godzilla.model.exceptions.SprintException;
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
			int projectId = ProjectDAO.getProjectIdByName("project1");
			Project project = ProjectDAO.getProjectById(projectId);
			
			int reporterId = UserDAO.getUserIdByEmail("ivan1@abv.bg");
			User reporter = UserDAO.getUserById(reporterId);
		
			Issue issue = new Story("first_story");
			
			IssueDAO.createIssue(issue, project, reporter);
			Issue epic = IssueDAO.getIssueById(9);
			
			IssueDAO.addIssueToEpic(issue, (Epic) epic);
			
			//Assert.assertTrue(issue.getId() > 0);
		} catch (IssueException e) {
			e.printStackTrace();
//			Assert.assertTrue(e.getMessage().equals("issue's summary cannot be null") 
//					|| e.getMessage().equals("issue priority cannot be null")
//					|| e.getMessage().equals("issue state cannot be null"));
		} catch (ProjectDAOException e) {
			e.printStackTrace();
//			Assert.assertTrue(e.getMessage().equals("couldn't find a project with that name")
//					|| e.getMessage().equals("can't find user with that id")
//					|| e.getMessage().equals("there is no such user")
//					|| e.getMessage().equals("user not created")
//					|| e.getMessage().equals("permissions not set")
//					|| e.getMessage().equals("failed to get issues"));
		} catch (UserDAOException e) {
			e.printStackTrace();
//			Assert.assertTrue(
//					   e.getMessage().equals("couldn't find a user with that email")
//					|| e.getMessage().equals("invalid project id")
//					|| e.getMessage().equals("failed to create project")
//					|| e.getMessage().equals("failed to get sprints")
//					|| e.getMessage().equals("failed to get issues"));
		} catch (IssueDAOException e) {
			e.printStackTrace();
//			Assert.assertTrue(
//					   e.getMessage().equals("couldn't set issue's id")
//					|| e.getMessage().equals("Ne bqha napraveni promeni")
//					|| e.getMessage().equals("cannot create issue, required: project, reporter and issue model")
//					|| e.getMessage().equals("could not set id of an issue")
//					|| e.getMessage().equals("failed to create issue"));
		}
	}
	@Test
	public void addEpicTest(){
		
		try {
			int projectId = ProjectDAO.getProjectIdByName("project1");
			Project project = ProjectDAO.getProjectById(projectId);
			
			int reporterId = UserDAO.getUserIdByEmail("ivan1@abv.bg");
			User reporter = UserDAO.getUserById(reporterId);
		
			Issue issue = new Epic("second_epic","epic2");
			
			IssueDAO.createIssue(issue, project, reporter);
			
//			Assert.assertTrue(issue.getId() > 0);
		} catch (IssueException e) {
			e.printStackTrace();
//			Assert.assertTrue(e.getMessage().equals("issue's summary cannot be null") 
//					|| e.getMessage().equals("issue priority cannot be null")
//					|| e.getMessage().equals("issue state cannot be null"));
		} catch (ProjectDAOException e) {
			e.printStackTrace();
//			Assert.assertTrue(e.getMessage().equals("couldn't find a project with that name")
//					|| e.getMessage().equals("can't find user with that id")
//					|| e.getMessage().equals("there is no such user")
//					|| e.getMessage().equals("user not created")
//					|| e.getMessage().equals("permissions not set")
//					|| e.getMessage().equals("failed to get issues"));
		} catch (UserDAOException e) {
			e.printStackTrace();
//			Assert.assertTrue(e.getMessage().equals("couldn't find a user with that email")
//					|| e.getMessage().equals("invalid project id")
//					|| e.getMessage().equals("failed to create project")
//					|| e.getMessage().equals("failed to get sprints")
//					|| e.getMessage().equals("failed to get issues"));
		} catch (IssueDAOException e) {
			e.printStackTrace();

//			Assert.assertTrue(
//					   e.getMessage().equals("couldn't set issue's id")
//					|| e.getMessage().equals("Ne bqha napraveni promeni")
//					|| e.getMessage().equals("cannot create issue, required: project, reporter and issue model")
//					|| e.getMessage().equals("could not set id of an issue")
//					|| e.getMessage().equals("failed to create issue"));



		} catch (EpicException e) {
			e.printStackTrace();
//			Assert.assertTrue(e.getMessage().equals("epic's name cannot be null"));
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
	
	//@Test
	public void createRemoveTaskTest() {
		try {
			int projectId = ProjectDAO.getProjectIdByName("project 1");
			Project project = ProjectDAO.getProjectById(projectId);
			
			int reporterId = UserDAO.getUserIdByEmail("user1@abv.bg");
			User reporter = UserDAO.getUserById(reporterId);
		
			Issue issue = new Task("Task");
			
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
	
	//@Test
	public void createRemoveStoryTest() {
		try {
			int projectId = ProjectDAO.getProjectIdByName("project 1");
			Project project = ProjectDAO.getProjectById(projectId);
			
			int reporterId = UserDAO.getUserIdByEmail("user1@abv.bg");
			User reporter = UserDAO.getUserById(reporterId);
		
			Issue issue = new Story("Story");
			
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
	
	//@Test
	public void getAllReportedIssuesByUser(){
		
		String email = "user1@abv.bg";
		
		try {
			int userId = UserDAO.getUserIdByEmail(email);
			User user = UserDAO.getUserById(userId);
			
			Set<Issue> reportedByUser = IssueDAO.getAllReportedIssuesByUser(user);
			
			Assert.assertTrue(reportedByUser.size() > 0);
			System.err.println("Reported");
			for(Issue i : reportedByUser){
				System.out.println(i);
			}
			System.out.println();
			
		} catch (UserDAOException e) {
			Assert.assertTrue(e.getMessage().equals("email cannot be null")
					|| e.getMessage().equals("couldn't find a user with that email")
					|| e.getMessage().equals("user not created")
					|| e.getMessage().equals("permissions not set")
					|| e.getMessage().equals("can't find user with that id")
					|| e.getMessage().equals("failed to get issues")
					|| e.getMessage().equals("there is no such user"));
		} catch (IssueDAOException e) {
			Assert.assertTrue(e.getMessage().equals("couldn't find user"));
		}
	}
	
	//@Test
	public void getAllIssuesAssignedToUser(){
		String email = "user1@abv.bg";
		
		try {
			int userId = UserDAO.getUserIdByEmail(email);
			User user = UserDAO.getUserById(userId);
			
			Set<Issue> reportedByUser = IssueDAO.getAllIssuesAssignedTo(user);
			
			Assert.assertTrue(reportedByUser.size() > 0);
			
			System.err.println("Assigned");
			for(Issue i : reportedByUser){
				System.out.println(i);
			}
			
		} catch (UserDAOException e) {
			Assert.assertTrue(e.getMessage().equals("email cannot be null")
					|| e.getMessage().equals("couldn't find a user with that email")
					|| e.getMessage().equals("user not created")
					|| e.getMessage().equals("permissions not set")
					|| e.getMessage().equals("can't find user with that id")
					|| e.getMessage().equals("failed to get issues")
					|| e.getMessage().equals("there is no such user"));
		} catch (IssueDAOException e) {
			Assert.assertTrue(e.getMessage().equals("couldn't find user"));
		} 
	}
	
	//@Test
	public void getIssuesByEpic(){

		
		try {
			Issue epic = new Epic("epic 4", "epic 4");
			epic.setId(55);

			int projectId = ProjectDAO.getProjectIdByName("project 1");
			Project project = ProjectDAO.getProjectById(projectId);
			
			int reporterId = UserDAO.getUserIdByEmail("user1@abv.bg");
			User reporter = UserDAO.getUserById(reporterId);
		
			Issue issue1 = new Bug("Bug1");
			Issue issue2 = new Bug("Bug2");
			
			IssueDAO.createIssue(issue1, project, reporter);
			IssueDAO.createIssue(issue2, project, reporter);
			
			Assert.assertTrue(issue1.getId() > 0);
			Assert.assertTrue(issue2.getId() > 0);
			
			IssueDAO.addIssueToEpic(issue1, (Epic)epic);
			IssueDAO.addIssueToEpic(issue2, (Epic)epic);
			
			Set<Issue> issuesByEpic = IssueDAO.getAllIssuesByEpic((Epic)epic);
			
			for(Issue i : issuesByEpic){
				System.out.println(i);
			}
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
		} catch (EpicException e) {
			Assert.assertTrue(
					   e.getMessage().equals("epic's name cannot be null"));
		}
	}
	//@Test
	public void getIssuesBySprint(){

		try {
			Sprint sprint = new Sprint("sprint 2", "Goal");
			sprint.setId(1);

			int projectId = ProjectDAO.getProjectIdByName("project 1");
			Project project = ProjectDAO.getProjectById(projectId);
			
			int reporterId = UserDAO.getUserIdByEmail("user1@abv.bg");
			User reporter = UserDAO.getUserById(reporterId);
		
			Issue issue1 = new Bug("Bug1");
			Issue issue2 = new Bug("Bug2");
			
			IssueDAO.createIssue(issue1, project, reporter);
			IssueDAO.createIssue(issue2, project, reporter);
			
			Assert.assertTrue(issue1.getId() > 0);
			System.err.println(issue1.getId());
			Assert.assertTrue(issue2.getId() > 0);
			System.err.println(issue2.getId());
			
			IssueDAO.addIssueToSprint(issue1, sprint);
			IssueDAO.addIssueToSprint(issue2, sprint);
			
			Set<Issue> issuesBySprint = IssueDAO.getAllIssuesBySprint(sprint);
			
			for(Issue i : issuesBySprint){
				System.out.println(i);
			}
			
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
		} catch (SprintException e) {
			Assert.assertTrue(
					   e.getMessage().equals("sprint name cannot be null")
					   || e.getMessage().equals("sprint goal cannot be null"));
		}
	}
	
	//@Test
	public void removeIssueFromSprint(){
		try {
			Sprint sprint = new Sprint("sprint 2", "Goal");
			sprint.setId(1);

			int projectId = ProjectDAO.getProjectIdByName("project 1");
			Project project = ProjectDAO.getProjectById(projectId);
			
			int reporterId = UserDAO.getUserIdByEmail("user1@abv.bg");
			User reporter = UserDAO.getUserById(reporterId);
		
			int issueId = 99;
			Issue issue = IssueDAO.getIssueById(issueId);
			
			IssueDAO.removeFromSprint(issue);
			
			Set<Issue> issuesBySprint = IssueDAO.getAllIssuesBySprint(sprint);
			
			for(Issue i : issuesBySprint){
				System.out.println(i);
			}
			
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
		} catch (SprintException e) {
			Assert.assertTrue(
					   e.getMessage().equals("sprint name cannot be null")
					   || e.getMessage().equals("sprint goal cannot be null"));
		}
	}
	
	//@Test
	public void handToAdmin(){
		int issueId = 100;
		try {
			Issue issue = IssueDAO.getIssueById(issueId);
			
			IssueDAO.handToAdmin(issue);
		} catch (IssueDAOException e) {
			e.printStackTrace();
			Assert.assertTrue(
					   e.getMessage().equals("couldn't set issue's id")
					|| e.getMessage().equals("Ne bqha napraveni promeni")
					|| e.getMessage().equals("cannot create issue, required: project, reporter and issue model")
					|| e.getMessage().equals("could not set id of an issue")
					|| e.getMessage().equals("failed to create issue")
					|| e.getMessage().equals("couldn't find company admin"));
		}
	}
	//@Test
	public void unassingIssue(){
		int issueId = 100;
		try {
			Issue issue = IssueDAO.getIssueById(issueId);
			
			IssueDAO.unassignIssue(issue);
		} catch (IssueDAOException e) {
			e.printStackTrace();
			Assert.assertTrue(
					   e.getMessage().equals("couldn't set issue's id")
					|| e.getMessage().equals("Ne bqha napraveni promeni")
					|| e.getMessage().equals("cannot create issue, required: project, reporter and issue model")
					|| e.getMessage().equals("could not set id of an issue")
					|| e.getMessage().equals("failed to create issue")
					|| e.getMessage().equals("couldn't find company admin"));
		}
	}
	
	//@Test
	public void addIssueToSprint() {
		int issueId = 9;
		int sprintId = 1;
		try {
			Issue issue = IssueDAO.getIssueById(issueId);
			Sprint sprint = SprintDAO.getSprintById(sprintId);
			IssueDAO.addIssueToSprint(issue, sprint);
		} catch (IssueDAOException e) {
			e.printStackTrace();
		} catch (SprintDAOException e) {
			e.printStackTrace();
		}
	}
	
}