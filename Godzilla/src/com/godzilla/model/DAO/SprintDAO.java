package com.godzilla.model.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.godzilla.DBConnection.DBConnection;
import com.godzilla.model.Issue;
import com.godzilla.model.Project;
import com.godzilla.model.Sprint;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.SprintDAOException;
import com.godzilla.model.exceptions.SprintException;

public class SprintDAO {
	private static final String REMOVE_SPRINT_SQL = "DELETE FROM sprints " + "WHERE sprint_id = ?;";
	private static final String SELECT_ALL_SPRINTS_BY_PROJECT_ID = "SELECT * FROM sprints " + "WHERE project_id = ? ";
	private static final String INSERT_SPRINT_SQL = "INSERT INTO sprints " + "VALUES(null, ? , ? , ? , ? , ?);";

	public static Set<Sprint> getAllSprintsByProject(Project project) throws SprintDAOException {
		if (project == null) {
			throw new SprintDAOException("couldn't find project");
		}

		Connection connection = DBConnection.getInstance().getConnection();
		int projectId = project.getId();

		Set<Sprint> result = new HashSet<Sprint>();

		try {
			PreparedStatement selectAllSprintsByProjectId = connection
					.prepareStatement(SELECT_ALL_SPRINTS_BY_PROJECT_ID);

			selectAllSprintsByProjectId.setInt(1, projectId);

			ResultSet rs = selectAllSprintsByProjectId.executeQuery();

			while (rs.next()) {
				Sprint sprint = null;
				int sprintId = rs.getInt(1);
				String name = rs.getString(2);
				String goal = rs.getString(3);
				LocalDateTime startingDate = IssueDAO.getLocalDateTimeFromString(rs.getString(4));
				LocalDateTime endDate = IssueDAO.getLocalDateTimeFromString(rs.getString(5));

				sprint = new Sprint(name);
				sprint.setId(sprintId);
				sprint.setSprintGoal(goal);
				sprint.setStartingDate(startingDate);
				sprint.setEndDate(endDate);

				result.add(sprint);
			}

		} catch (SQLException e) {
			throw new SprintDAOException(e.getMessage());
		} catch (SprintException e) {
			throw new SprintDAOException("couldn't create sprint", e);
		} catch (IssueDAOException e) {
			throw new SprintDAOException("failed to convert to LocalDateTime", e);
		}

		return result;
	}

	public static void addSprint(Sprint sprintToAdd, Project project) throws SprintDAOException {
		if (sprintToAdd == null || project == null) {
			throw new SprintDAOException("can't add sprint, to do so you need a valid project and sprint model");
		}
		
		Connection connection = DBConnection.getInstance().getConnection();
		String startingDate = sprintToAdd.getStartingDate().toLocalDate().toString() +
								" " + 
								sprintToAdd.getStartingDate().toLocalTime().toString();
		String endDate = sprintToAdd.getEndDate().toLocalDate().toString() +
							" " +
							sprintToAdd.getEndDate().toLocalTime().toString();
		
		try {
			PreparedStatement insertSprint = connection.prepareStatement(INSERT_SPRINT_SQL,
					Statement.RETURN_GENERATED_KEYS);
			
			insertSprint.setString(1, sprintToAdd.getName());
			insertSprint.setString(2, sprintToAdd.getSpintGoal());
			insertSprint.setString(3, startingDate);
			insertSprint.setString(4, endDate);
			insertSprint.setInt(5, project.getId());

			insertSprint.executeUpdate();

			int sprintId;
			ResultSet generatedKeys = insertSprint.getGeneratedKeys();

			if (generatedKeys.next()) {
				sprintId = generatedKeys.getInt(1);
				sprintToAdd.setId(sprintId);
			} else {
				throw new SprintDAOException("failed to create sprint");
			}

		} catch (SQLException e) {
			throw new SprintDAOException(e.getMessage());
		} catch (SprintException e) {
			throw new SprintDAOException("couldn't set sprint id", e);
		}
	}

	public static void removeSprint(Sprint sprintToRemove) throws SprintDAOException {
		if (sprintToRemove == null) {
			throw new SprintDAOException("can't find sprint to remove");
		}

		Connection connection = DBConnection.getInstance().getConnection();
		int sprintId = sprintToRemove.getId();

		try {
			Set<Issue> issuesToSetFree = IssueDAO.getAllIssuesBySprint(sprintToRemove);

			for (Issue issueToSetFree : issuesToSetFree) {
				IssueDAO.removeFromSprint(issueToSetFree);
			}

			PreparedStatement removeSprintPS = connection.prepareStatement(REMOVE_SPRINT_SQL);
			removeSprintPS.setInt(1, sprintId);

			if (removeSprintPS.executeUpdate() < 1) {
				throw new SprintDAOException("failed to remove sprint");
			}
		} catch (SQLException e) {
			throw new SprintDAOException(e.getMessage());
		} catch (IssueDAOException e) {
			throw new SprintDAOException("issues failure", e);
		}

	}
}
