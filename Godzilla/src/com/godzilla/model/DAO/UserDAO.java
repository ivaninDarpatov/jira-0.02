package com.godzilla.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.godzilla.DBConnection.DBConnection;
import com.godzilla.model.Company;
import com.godzilla.model.User;
import com.godzilla.model.enums.Permissions;

public class UserDAO {
	private static final int USER_PERMISSIONS = Permissions.USER.ordinal() + 1;
	private static final int ADMIN_PERMISSIONS = Permissions.ADMINISTRATOR.ordinal() + 1;
	
	private static final String FIND_USER_SQL = "SELECT u.id "
												+ "FROM users u "
												+ "JOIN companies c "
												+ "ON (c.id = u.Companies_id) "
												+ "WHERE u.email = ? "
												+ "AND u.password = ? "
												+ "AND c.name = ?;";
	private static final String FIND_COMPANY_SQL = "SELECT id "
												+ "FROM companies "
												+ "WHERE name = ?;";
	private static final String REGISTER_USER_SQL = "INSERT INTO users "
												+ "VALUES (null, ?, ?, ? , ?);";
	private static final String REMOVE_USER_SQL = "Delete from users"
												+ "where email = ?;";
	
	public static void registerUser(User toRegister) {
		if (toRegister != null) {
			Connection connection = DBConnection.getInstance().getConnection();
			String email = toRegister.getEmail();
			String password = toRegister.getPassword();
			String company = toRegister.getCompany();
			int permisstionsId = 0;
			int companyId;
			
			try {
				connection.setAutoCommit(false);
				PreparedStatement ps = connection.prepareStatement(FIND_COMPANY_SQL);
				ps.setString(1, company);
				
				ResultSet rs = ps.executeQuery();
				
				if (rs.next()) {
					companyId = rs.getInt("id");
					permisstionsId = USER_PERMISSIONS;
				} else {
					Company newCompany = new Company(company);
					CompanyDAO.createNewCompany(newCompany);
					
					companyId = newCompany.getId();
					permisstionsId = ADMIN_PERMISSIONS;
					toRegister.setPermissions(Permissions.ADMINISTRATOR);
				}
				
				ps = connection.prepareStatement(REGISTER_USER_SQL, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, email);
				ps.setString(2, password);
				ps.setInt(3, permisstionsId);
				ps.setInt(4, companyId);
				
				ps.executeUpdate();
				
				ResultSet generatedKeys = ps.getGeneratedKeys();
				int userId = 0;
				if(generatedKeys.next()){
					userId = generatedKeys.getInt(1);
					toRegister.setId(userId);
				}else{
					//TODO: throw exception
					System.err.println("User was not register");
				}
				
				connection.commit();
				
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}finally {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public boolean validateLogin(User user) {
		Connection connection = DBConnection.getInstance().getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(FIND_USER_SQL);
			ps.setString(1, user.getEmail());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getCompany());			
			
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				if (rs.next()) {
					return false;
				}
				
				return true;
			}		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static void remmoveUser(User userToRemove){
		Connection connection = DBConnection.getInstance().getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(REMOVE_USER_SQL);
			
			ps.setString(1, userToRemove.getEmail());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Exception
			e.printStackTrace();
		}
	}
}
