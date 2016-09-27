package com.godzilla.model.DAO;

import java.sql.Connection;
import java.time.LocalDateTime;

import com.godzilla.DBConnection.DBConnection;
import com.godzilla.model.Issue;

public class IssueDAO {
	private static final String CREATE_ISSUE_SQL = "INSERT INTO issues "
												+ "VALUES (null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	public static void createIssue(Issue toCreate) {
		Connection connection = DBConnection.getInstance().getConnection();
		
		int issueId;
		String summary;
		String description;
		int projectId, stateId,
			priority, reportedById,
			assignedToId, sprintId;
		LocalDateTime dateCreated, dateLastModified;
	}
}
