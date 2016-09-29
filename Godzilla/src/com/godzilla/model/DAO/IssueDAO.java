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
	private static final String REMOVE_ISSUE_SQL = "DELETE FROM issues "
												+ "WHERE id = ?;";
	private static final String CREATE_ISSUE_SQL = "INSERT INTO issues "
												+ "VALUES (null, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW(), null);";
	private static final String SELECT_ISSUE_BY_REPORTER_SQL = "Select id,Summary,Description,"
			+ "workflow_state_id,Priorities_id,"
			+ "Date_created,Date_last_modified"
			+ " from issues where Reported_by_id = ?;";
	
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
	
	public Set<Issue> getAllReportedIssuesByUser(User user) throws UserDAOException{
		Set<Issue> reportedByThatUser = new HashSet<Issue>();
		Connection connection = DBConnection.getInstance().getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(SELECT_ISSUE_BY_REPORTER_SQL);
			
			ps.setInt(1, user.getId());
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				int issueId = rs.getInt(1);
				String summary = rs.getString(2);
				String description = rs.getString(3);
				IssueState issueState = null;
				
				int issueStateInteger = rs.getInt(5);
				if(issueStateInteger == 1){
					issueState = IssueState.TO_DO;
				}else{
					if(issueStateInteger == 2){
						issueState = IssueState.IN_PROGRESS;
					}else{
						issueState = IssueState.DONE;
					}
				}
				
				Issue issueToAdd = IssueDAO.getIssueById(issueId);
				issueToAdd.setSummary(summary);
				issueToAdd.setId(issueId);
				issueToAdd.setDescription(description);
				issueToAdd.setState(issueState);
				
				reportedByThatUser.add(issueToAdd);
				
				
				
				//TODO: da vidq kakvo shte pravq s Priority na Issueto
				//TODO: da populna v bazata priorities
				//TODO: Kakto DateCreated i DateLastModified
				
			}
		} catch (SQLException e) {
			throw new UserDAOException(e.getMessage());
		}
		
	}
}
