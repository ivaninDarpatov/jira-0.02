package com.godzilla.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.godzilla.model.DBConnection.DBConnection;
import com.godzilla.model.Issue;
import com.godzilla.model.Project;
import com.godzilla.model.Sprint;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.SprintDAOException;
import com.godzilla.model.exceptions.SprintException;

public class SprintDAO {
	private static final String FIND_SPRINT_ID_BY_NAME_SQL = "SELECT sprint_id FROM sprints WHERE sprint_name = ?;";
	private static final String GET_SPRINT_BY_ID_SQL = "SELECT * FROM sprints WHERE sprint_id = ?;";
	private static final String REMOVE_SPRINT_SQL = "DELETE FROM sprints WHERE sprint_id = ?;";
	private static final String GET_SPRINTS_BY_PROJECT_ID_SQL = "SELECT * FROM sprints WHERE project_id = ?;";
	private static final String ADD_SPRINT_SQL = "INSERT INTO sprints VALUES(null, ? , ? , 'someDate' , 'someDate', ?, ?);";

	@SuppressWarnings("unused")
	public static Sprint getSprintById(int sprintId) throws SprintDAOException {
		if (sprintId < 1) {
			throw new SprintDAOException("sprint id cannot be 0");
		}
		
		Sprint result = null;
		Connection connection = DBConnection.getInstance().getConnection();
		Set<Issue> issues = new HashSet<Issue>();
		LocalDateTime startingDate;
		LocalDateTime endDate;
		String name;
		String goal;
		boolean isActive;
		
		try {
			PreparedStatement getSprintByIdPS = connection.prepareStatement(GET_SPRINT_BY_ID_SQL);
			getSprintByIdPS.setInt(1, sprintId);
			
			ResultSet getSprintByIdRS = getSprintByIdPS.executeQuery();
			
			if (getSprintByIdRS.next()) {
				name = getSprintByIdRS.getString("sprint_name");
				goal = getSprintByIdRS.getString("sprint_goal");
				isActive = (getSprintByIdRS.getInt("is_active") == 1) ? true : false;
				result = new Sprint(name, goal);
//				startingDate = IssueDAO.getLocalDateTimeFromString(getSprintByIdRS.getString("starting_date"));
//				endDate = IssueDAO.getLocalDateTimeFromString(getSprintByIdRS.getString("end_date"));
//				
//				result.setStartingDate(startingDate);
//				result.setEndDate(endDate);
				result.setId(sprintId);
				result.setIsActive(isActive);
				
				issues = IssueDAO.getAllIssuesBySprint(result);
				
				for (Issue issue : issues) {
					result.addIssue(issue);
				}
			}
		} catch (SQLException e) {
			throw new SprintDAOException(e.getMessage());
		} catch (SprintException e) {
			throw new SprintDAOException("failed to create sprint", e);
		} catch (IssueDAOException e) {
			throw new SprintDAOException("failed to convert to LocalDateTime", e);
		}
		return result;
	}
	
	public static Set<Sprint> getAllSprintsByProject(Project project) throws SprintDAOException {
		if (project == null) {
			throw new SprintDAOException("project cannot be null");
		}

		Connection connection = DBConnection.getInstance().getConnection();
		int projectId = project.getId();

		Set<Sprint> result = new HashSet<Sprint>();

		try {
			PreparedStatement selectAllSprintsByProjectId = connection.prepareStatement(GET_SPRINTS_BY_PROJECT_ID_SQL);
			selectAllSprintsByProjectId.setInt(1, projectId);

			ResultSet rs = selectAllSprintsByProjectId.executeQuery();

			while (rs.next()) {
				Sprint sprint = null;
				int sprintId = rs.getInt("sprint_id");
				
				sprint = SprintDAO.getSprintById(sprintId);
				
				result.add(sprint);
			}

		} catch (SQLException e) {
			throw new SprintDAOException(e.getMessage());
		}

		return result;
	}

	public static void addSprint(Sprint sprintToAdd, Project project) throws SprintDAOException {
		if (sprintToAdd == null || project == null) {
			throw new SprintDAOException("can't add sprint, to do so you need a valid project and sprint model");
		}
		
		Connection connection = DBConnection.getInstance().getConnection();
//		String startingDate = sprintToAdd.getStartingDate().toLocalDate().toString() +
//								" " + 
//								sprintToAdd.getStartingDate().toLocalTime().toString();
//		String endDate = sprintToAdd.getEndDate().toLocalDate().toString() +
//							" " +
//							sprintToAdd.getEndDate().toLocalTime().toString();
		
		try {
			PreparedStatement insertSprint = connection.prepareStatement((ADD_SPRINT_SQL), Statement.RETURN_GENERATED_KEYS);
			
			insertSprint.setString(1, sprintToAdd.getName());
			insertSprint.setString(2, sprintToAdd.getSpintGoal());
//			insertSprint.setString(3, startingDate);
//			insertSprint.setString(4, endDate);
			insertSprint.setInt(3, (sprintToAdd.isActive()) ? 1 : 0);
			insertSprint.setInt(4, project.getId());
			

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
	
	public static int getSprintIdByName(String sprintName) throws SprintDAOException {
		if (sprintName == null || sprintName.length() == 0) {
			throw new SprintDAOException("Sprint name cannot be empty or null");
		}
		
		Connection connection = DBConnection.getInstance().getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(FIND_SPRINT_ID_BY_NAME_SQL);
			ps.setString(1, sprintName);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new SprintDAOException(e.getMessage());
		}
		
		return 0;
	}
}
