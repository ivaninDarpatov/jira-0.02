package com.godzilla.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.godzilla.DBConnection.DBConnection;
import com.godzilla.model.Project;
import com.godzilla.model.exceptions.CompanyDAOException;
import com.godzilla.model.exceptions.ProjectDAOException;

public class ProjectDAO {
	
	public static final String SELECT_NAME_FROM_PROJECTS = "SELECT name from projects";
	public static final String INSERT_INTO_PROJECTS = "Insert into projects VALUES(? , ? , ?);";

	public static void addProject(Project newProject,String companyName) throws ProjectDAOException{
		Connection connection = DBConnection.getInstance().getConnection();
		
		try {
			if(!CompanyDAO.isThereCompanyWithTheSameName(companyName)){
				throw new ProjectDAOException("unknow company to add project to");
			}
		} catch (CompanyDAOException e1) {
			throw new ProjectDAOException(e1.getMessage());
		}
		
		if(isThereProjectWithTheSameName(newProject.getName())){
			throw new ProjectDAOException("Project allready exists");
		}
		
		int companyId;
		try {
			companyId = CompanyDAO.getIdOfCompanyWithName(companyName);
		} catch (CompanyDAOException e1) {
			throw new ProjectDAOException(e1.getMessage());
		}
		
		try {
			PreparedStatement insertIntoProjects = connection.prepareStatement(INSERT_INTO_PROJECTS,Statement.RETURN_GENERATED_KEYS);
			insertIntoProjects.setInt(1, 0);
			insertIntoProjects.setString(2, newProject.getName());
			insertIntoProjects.setInt(3, companyId);
			insertIntoProjects.executeUpdate();
			
			ResultSet rs = insertIntoProjects.getGeneratedKeys();
			
			if(rs.next()){
				newProject.setId(rs.getInt(1));
			}else{
				throw new ProjectDAOException("Failed to create project");
			}
			
		} catch (SQLException e) {
			throw new ProjectDAOException(e.getMessage());
		}
		
	}
	
	public void deleteProject(String projectName) throws ProjectDAOException{
		if(isThereProjectWithTheSameName(projectName)){
			Connection connection = DBConnection.getInstance().getConnection();
			
			//PreparedStatement deleteProject = connection.prepareStatement(sql)
			
			
		}else{
			throw new ProjectDAOException("There is no project with that name");
		}
	}
	
	
	public static boolean isThereProjectWithTheSameName(String projectName) throws ProjectDAOException{
		Connection connection = DBConnection.getInstance().getConnection();
		Statement selectStatement;
		ResultSet rs = null;
		try {
			selectStatement = connection.createStatement();
			rs = selectStatement.executeQuery(SELECT_NAME_FROM_PROJECTS);
			
			while(rs.next()){
				if( rs.getString(1).equals(projectName)){
					return true;
				}
			}
		} catch (SQLException e) {
			throw new ProjectDAOException(e.getMessage());
		}
		return false;
	}
}
