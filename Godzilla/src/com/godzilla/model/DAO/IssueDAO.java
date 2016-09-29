package com.godzilla.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import com.godzilla.DBConnection.DBConnection;
import com.godzilla.model.*;
import com.godzilla.model.enums.IssuePriority;
import com.godzilla.model.enums.IssueState;
import com.godzilla.model.exceptions.IssueDAOException;

public class IssueDAO {
	private static final String REMOVE_ISSUE_SQL = "DELETE FROM issues "
												+ "WHERE id = ?;";
	private static final String CREATE_ISSUE_SQL = "INSERT INTO issues "
												+ "VALUES (null, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW(), null);";
	
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
}
