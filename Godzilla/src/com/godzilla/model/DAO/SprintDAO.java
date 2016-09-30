package com.godzilla.model.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.godzilla.DBConnection.DBConnection;
import com.godzilla.model.Project;
import com.godzilla.model.Sprint;
import com.godzilla.model.exceptions.SprintDAOException;
import com.godzilla.model.exceptions.SprintException;

public class SprintDAO {
	private static final String SELECT_ALL_SPRINTS_BY_PROJECT_ID = "SELECT * FROM sprints "
																+ "WHERE project_id = ? ";
	private static final String INSERT_SPRINT_SQL = "INSERT INTO sprints "
													+ "VALUES(null, ? , ? , now() , now() , ?);";
	
	
	
	public static Set<Sprint> getAllSprintsByProject(Project project) throws SprintDAOException {
		if (project == null) {
			throw new SprintDAOException("couldnt find project");
		}
		
		Connection connection = DBConnection.getInstance().getConnection();
		int projectId = project.getId();
		
		Set<Sprint> result = new HashSet<Sprint>();
		
		try {
			PreparedStatement selectAllSprintsByProjectId = connection.prepareStatement(SELECT_ALL_SPRINTS_BY_PROJECT_ID);
			
			selectAllSprintsByProjectId.setInt(1, projectId);
			
			ResultSet rs = selectAllSprintsByProjectId.executeQuery();
			
			while(rs.next()){
				Sprint sprint = null;
				int sprintId = rs.getInt(1);
				String name = rs.getString(2);
				String goal = rs.getString(3);
				Date startingDate = rs.getDate(4);
				Date lastModifiedDate = rs.getDate(5);
				
				sprint = new Sprint(name);
				sprint.setId(sprintId);
				sprint.setSprintGoal(goal);
				//TODO: da vidq kak shte setvame starting date i lastModified
				
				result.add(sprint);
			}
			
			//TODO: da vidq kak shte konstruirame Sprint
		} catch (SQLException e) {
			throw new SprintDAOException(e.getMessage());
		} catch (SprintException e) {
			throw new SprintDAOException(e.getMessage());
		}
		
		
		return result;
	}

	
	public static void addSprint(Sprint sprintToAdd,Project project) throws SprintDAOException{
		Connection connection = DBConnection.getInstance().getConnection();
		
		try {
			PreparedStatement insertSprint = connection.prepareStatement(INSERT_SPRINT_SQL,Statement.RETURN_GENERATED_KEYS);
			
			insertSprint.setString(1, sprintToAdd.getName());
			insertSprint.setString(2, sprintToAdd.getSpintGoal());
			insertSprint.setInt(3, project.getId());
			
			insertSprint.executeUpdate();
			
			int sprintId;
			ResultSet generatedKeys = insertSprint.getGeneratedKeys();
			
			if(generatedKeys.next()){
				sprintId = generatedKeys.getInt(1);
				sprintToAdd.setId(sprintId);
			}else{
				throw new SprintDAOException("Failed to create Sprint");
			}
			
		} catch (SQLException e) {
			throw new SprintDAOException(e.getMessage());
		}
	}
}
