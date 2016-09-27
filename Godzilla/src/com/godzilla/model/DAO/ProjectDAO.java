package com.godzilla.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.godzilla.DBConnection.DBConnection;
import com.godzilla.model.Project;

public class ProjectDAO {
	
	public static final String SELECT_NAME_FROM_PROJECTS = "SELECT name from projects";
	public static final String INSERT_INTO_PROJECTS = "Insert into projects VALUES(? , ? , ?);";

	public static void addProject(Project newProject,String companyName){
		Connection connection = DBConnection.getInstance().getConnection();
		
		if(!CompanyDAO.isThereCompanyWithTheSameName(companyName)){
			//TODO:Throw exception
			return;
		}
		
		if(isThereProjectWithTheSameName(newProject.getName())){
			//TODO: throw exception
			return;
		}
		
		int companyId = CompanyDAO.getIdOfCompanyWithName(companyName);
		
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
				//TODO: exception
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
//	public void deleteProject(String projectName){
//		if(isThereProjectWithTheSameName(projectName)){
//			
//		}
//	}
	
	
	public static boolean isThereProjectWithTheSameName(String projectName){
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
			//TODO:
			e.printStackTrace();
		}
		return false;
	}
}
