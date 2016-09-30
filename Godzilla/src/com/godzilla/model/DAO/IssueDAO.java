package com.godzilla.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.godzilla.DBConnection.DBConnection;
import com.godzilla.model.*;
import com.godzilla.model.enums.IssuePriority;
import com.godzilla.model.enums.IssueState;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.UserDAOException;

public class IssueDAO {
	// da dobavim epics_id v issues v bazata
	private static final String FIND_ISSUES_BY_EPIC_ID_SQL = "SELECT id "
															+ "FROM issues "
															+ "WHERE epics_id = ?;";
	private static final String FIND_ISSUES_BY_PROJECT_ID_SQL = "SELECT id "
															+ "FROM issues "
															+ "WHERE projects_id = ?;";
	private static final String IS_BUG_SQL = "SELECT id "
			+ "FROM bugs "
			+ "WHERE id = ?;";
	private static final String IS_TASK_SQL = "SELECT id "
			+ "FROM tasks "
			+ "WHERE id = ?;";
	private static final String IS_STORY_SQL = "SELECT id "
			+ "FROM stories "
			+ "WHERE id = ?;";
	private static final String IS_EPIC_SQL = "SELECT id "
			+ "FROM epics "
			+ "WHERE id = ?;";
	private static final String REMOVE_ISSUE_SQL = "DELETE FROM issues "
												+ "WHERE id = ?;";
	private static final String CREATE_ISSUE_SQL = "INSERT INTO issues "
												+ "VALUES (null, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW(), null);";
	private static final String SELECT_ISSUE_BY_REPORTER_SQL = "Select id,Summary,Description,"
			+ "workflow_state_id,Priorities_id,"
			+ "Date_created,Date_last_modified"
			+ " from issues where Reported_by_id = ?;";
	private static final String FIND_ISSUE_BY_ID_SQL = "SELECT * "
													+ "FROM issues "
													+ "WHERE id = ?;";
	
	public static void createIssue(Issue toCreate, Project project, User reporter) throws IssueDAOException {
		Connection connection = DBConnection.getInstance().getConnection();
		
		String summary = toCreate.getSummary();
		String description = toCreate.getDescription();
		int priorityId = toCreate.getPriority().getValue();
		int stateId = toCreate.getState().getValue();
		int projectId = project.getId();
		int reporterId = reporter.getId();
		int assigneeId = reporterId;
		
		try {
			PreparedStatement ps = connection.prepareStatement(CREATE_ISSUE_SQL, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, summary);
			ps.setString(2, description);
			ps.setInt(3, projectId);
			ps.setInt(4, stateId);
			ps.setInt(5, priorityId);
			ps.setInt(6, reporterId);
			ps.setInt(7, assigneeId);

			if (ps.executeUpdate() > 0) {
				ResultSet rs = ps.getGeneratedKeys();

				rs.next();
				toCreate.setId(rs.getInt(1));
			} else {
				throw new IssueDAOException("failed to create issue");
			}
		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}
	}
	
	public static Set<Issue> getAllIssuesByProject(Project project) throws IssueDAOException {
		if (project == null) {
			throw new IssueDAOException("couldnt find project");
		}
		
		int projectId = project.getId();
		Set<Issue> result = new HashSet<Issue>();
		
		Connection connection = DBConnection.getInstance().getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(FIND_ISSUES_BY_PROJECT_ID_SQL);
			ps.setInt(1, projectId);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				int issueId = rs.getInt(1);
				result.add(getIssueById(issueId));
			}
			
		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}
		
		return result;
	}
	
	public static Issue getIssueById(int issueId) throws IssueDAOException {
		Connection connection = DBConnection.getInstance().getConnection();
		Issue issue = null;
		
		try {			
			PreparedStatement ps = connection.prepareStatement(FIND_ISSUE_BY_ID_SQL);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				String summary = rs.getString("summary");
				String description = rs.getString("description");
				int priorityId = rs.getInt("priorities_id");
				int stateId = rs.getInt("workflow_states_id");
				//get date created
				//get date last modified
				
				IssuePriority priority = IssuePriority.getTypeById(priorityId);
				IssueState state = IssueState.getTypeById(stateId);
						
				String type = IssueDAO.getIssueType(issueId);
				switch (type) {
				case "bug": 
					issue = new Bug(summary);
					break;
				case "task":
					issue = new Task(summary);
					break;
				case "story":
					issue = new Story(summary);
					break;
				case "epic":
					issue = new Epic(summary);
					issue.setId(issueId);
					((Epic)issue).setName(rs.getString("name"));
					Set<Issue> issues = IssueDAO.getAllIssuesByEpic((Epic)issue);
					
					for (Issue entry : issues) {
						((Epic)issue).addIssue(entry);
					}
					
					break;
				}
				
				if (issue == null) {
					throw new IssueDAOException("couldnt find issue");
				}
				
				if (description != null) {
					issue.setDescription(description);
				}
				issue.setPriority(priority);
				issue.setState(state);
				// set date created
				// set date last modified
				
				issue.setId(issueId);
			}
			
		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}
		
		return issue;
	}
	
	public static Set<Issue> getAllIssuesByEpic(Epic epic) throws IssueDAOException {
		if (epic == null) {
			throw new IssueDAOException("couldnt find epic");
		}
		
		int epicId = epic.getId();
		Set<Issue> result = new HashSet<Issue>();
		
		Connection connection = DBConnection.getInstance().getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(FIND_ISSUES_BY_EPIC_ID_SQL);
			ps.setInt(1, epicId);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				int issueId = rs.getInt(1);
				result.add(IssueDAO.getIssueById(issueId));
			}
			
		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}
		
		return result;
	}
	
	public static void removeIssue(Issue toRemove) throws IssueDAOException {
		Connection connection = DBConnection.getInstance().getConnection();
		if (toRemove == null) {
			throw new IssueDAOException("invalid issue to remove");
		}
		
		int issueId = toRemove.getId();
		
		try {
			PreparedStatement ps = connection.prepareStatement(REMOVE_ISSUE_SQL);
			ps.setInt(1, issueId);
			
			if (ps.executeUpdate() < 1) {
				throw new IssueDAOException("failed to remove issue");
			}
			
		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}
	}
	
	public static Set<Issue> getAllReportedIssuesByUser(User user) throws IssueDAOException{
		Set<Issue> reportedByThatUser = new HashSet<Issue>();
		Connection connection = DBConnection.getInstance().getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(SELECT_ISSUE_BY_REPORTER_SQL);
			
			ps.setInt(1, user.getId());
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				int issueId = rs.getInt(1);
				Issue issueToAdd = IssueDAO.getIssueById(issueId);
				reportedByThatUser.add(issueToAdd);		
			}
		
		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		} catch (IssueDAOException e) {
			throw e;
		}
		
		return reportedByThatUser;
	}
		
	private static String getIssueType(int issueId) throws IssueDAOException {
		if (IssueDAO.isBug(issueId)) {
			return "bug";
		}
		
		if (IssueDAO.isTask(issueId)) {
			return "task";
		}
		
		if (IssueDAO.isStory(issueId)) {
			return "story";
		}
		
		if (IssueDAO.isEpic(issueId)) {
			return "epic";
		}
		
		return null;
	}
	
	private static boolean isBug(int issueId) throws IssueDAOException {
		Connection connection = DBConnection.getInstance().getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(IS_BUG_SQL);
			ps.setInt(1, issueId);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				return true;
			}
			
		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}
		
		return false;
	}
	
	private static boolean isTask(int issueId) throws IssueDAOException {
		Connection connection = DBConnection.getInstance().getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(IS_TASK_SQL);
			ps.setInt(1, issueId);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				return true;
			}
			
		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}
		
		return false;
	}
	
	private static boolean isStory(int issueId) throws IssueDAOException {
		Connection connection = DBConnection.getInstance().getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(IS_STORY_SQL);
			ps.setInt(1, issueId);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				return true;
			}
			
		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}
		
		return false;
	}
	
	private static boolean isEpic(int issueId) throws IssueDAOException {
		Connection connection = DBConnection.getInstance().getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(IS_EPIC_SQL);
			ps.setInt(1, issueId);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				return true;
			}
			
		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}
		
		return false;
	}
}
