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
import com.godzilla.model.exceptions.CompanyDAOException;
import com.godzilla.model.exceptions.CompanyException;
import com.godzilla.model.exceptions.PermissionException;
import com.godzilla.model.exceptions.UserDAOException;
import com.godzilla.model.exceptions.UserException;

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
	
	public static void registerUser(User toRegister) throws UserException, UserDAOException {
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
					Company newCompany;
					try {
						newCompany = new Company(company);
					} catch (CompanyException e1) {
						throw new UserDAOException("invalid company", e1);
					}
					try {
						CompanyDAO.createNewCompany(newCompany);
					} catch (CompanyDAOException e1) {
						throw new UserDAOException("Company not created",e1);
					}
					
					companyId = newCompany.getId();
					permisstionsId = ADMIN_PERMISSIONS;
					try {
						toRegister.setPermissions(Permissions.ADMINISTRATOR);
					} catch (PermissionException e) {
						throw new UserException("invalid permission", e);
					}
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
					throw new UserDAOException("Failed to register");
				}
				
				connection.commit();
				
			} catch (SQLException e ) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					throw new UserDAOException(e1.getMessage());
				}
				throw new UserDAOException(e.getMessage());
			}finally {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException e) {
					throw new UserDAOException(e.getMessage());
				}
			}
		}
	}
	
	public static boolean validateLogin(User user) throws UserDAOException {
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
			throw new UserDAOException(e.getMessage());
		}
		
		return false;
	}
	
//	public static User getUserById(int id){
//		User user = null;
//	}
	
	public static void remmoveUser(User userToRemove) throws UserDAOException{
		Connection connection = DBConnection.getInstance().getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(REMOVE_USER_SQL);
			
			ps.setString(1, userToRemove.getEmail());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new UserDAOException(e.getMessage());
		}
	}
	
	public static User getUserById(int id){
		User user = null;
		Connection connection = DBConnection.getInstance().getConnection();
		
		PreparedStatement select
	}
}
