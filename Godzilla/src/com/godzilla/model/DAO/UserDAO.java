package com.godzilla.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.godzilla.DBConnection.DBConnection;
import com.godzilla.model.Company;
import com.godzilla.model.Issue;
import com.godzilla.model.User;
import com.godzilla.model.enums.IssueState;
import com.godzilla.model.enums.Permissions;
import com.godzilla.model.exceptions.CompanyDAOException;
import com.godzilla.model.exceptions.CompanyException;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.PermissionException;
import com.godzilla.model.exceptions.UserDAOException;
import com.godzilla.model.exceptions.UserException;

public class UserDAO {
	private static final String FIND_USERS_BY_COMPANY_ID_SQL = "SELECT * "
															+ "FROM users "
															+ "WHERE company_id = ?;";
	private static final int USER_PERMISSIONS = Permissions.USER.ordinal() + 1;
	private static final int ADMIN_PERMISSIONS = Permissions.ADMINISTRATOR.ordinal() + 1;
	
	private static final String FIND_USER_SQL = "SELECT u.user_id "
												+ "FROM users u "
												+ "JOIN companies c "
												+ "ON (c.company_id = u.company_id) "
												+ "WHERE u.email = ? "
												+ "AND u.password = ? "
												+ "AND c.company_name = ?;";
	private static final String FIND_COMPANY_SQL = "SELECT company_id "
												+ "FROM companies "
												+ "WHERE company_name = ?;";
	private static final String REGISTER_USER_SQL = "INSERT INTO users "
												+ "VALUES (null, ?, ?, ? , ?);";
	private static final String REMOVE_USER_SQL = "DELETE FROM users"
												+ "WHERE email = ?;";
	private static final String SELECT_ID_EMAIL_PASSWORD_PERMISSION_SQL = "Select id, email.password,Permission_id from users where id = ?;";
	
	
	public static void registerUser(User toRegister) throws UserException, UserDAOException {
		if (toRegister != null) {
			Connection connection = DBConnection.getInstance().getConnection();
			String email = toRegister.getEmail();
			String password = toRegister.getPassword();
			String company = toRegister.getCompany();
			int permissionsId = 0;
			int companyId;
			
			try {
				connection.setAutoCommit(false);
				PreparedStatement ps = connection.prepareStatement(FIND_COMPANY_SQL);
				ps.setString(1, company);
				
				ResultSet rs = ps.executeQuery();
				
				if (rs.next()) {
					companyId = rs.getInt("company_id");
					permissionsId = USER_PERMISSIONS;
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
					permissionsId = ADMIN_PERMISSIONS;
					try {
						toRegister.setPermissions(Permissions.ADMINISTRATOR);
					} catch (PermissionException e) {
						throw new UserException("invalid permission", e);
					}
				}
				
				ps = connection.prepareStatement(REGISTER_USER_SQL, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, email);
				ps.setString(2, password);
				ps.setInt(3, permissionsId);
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
	
	public static User getUserById(int id) throws UserDAOException{
		User user = null;
		Connection connection = DBConnection.getInstance().getConnection();

		try {
			connection.setAutoCommit(false);
			PreparedStatement selectIdNamePasswordPermission = connection.prepareStatement(SELECT_ID_EMAIL_PASSWORD_PERMISSION_SQL);
			selectIdNamePasswordPermission.setInt(1, id);
			
			ResultSet rs = selectIdNamePasswordPermission.executeQuery();
			
			if(rs.next()){
				int userId = rs.getInt(1);
				String email = rs.getString(2);
				String password = rs.getString(3);
				int permission = rs.getInt(4);
				Permissions userPermission = permission == 1 ? Permissions.ADMINISTRATOR : Permissions.USER;
				
				user = new User(email, password);
				user.setPermissions(userPermission);
				
				for(Issue issue : IssueDAO.getAllReportedIssuesByUser(user)){
					user.addIssuesReportedByMe(issue);
				}
				
				//TODO assigned issues
				
			}else{
				throw new UserDAOException("there is no such user");
			}
		} catch (SQLException e) {
			throw new UserDAOException(e.getMessage());
		} catch (UserException e) {
			throw new UserDAOException("user not created", e);
		} catch (PermissionException e) {
			throw new UserDAOException("permission problem", e);
		} catch (IssueDAOException e) {
			throw new UserDAOException("couldnt get issues", e);
		}finally{
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		
		return user;
	}

	public static Set<User> getAllUsersByCompany(Company company) throws UserDAOException {
		if (company == null) {
			throw new UserDAOException("cant find company");
		}
		
		Connection connection = DBConnection.getInstance().getConnection();
		Set<User> result = new HashSet<User>();
		int companyId = company.getId();
		
		try {
			PreparedStatement ps = connection.prepareStatement(FIND_USERS_BY_COMPANY_ID_SQL);
			ps.setInt(1, companyId);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				int userId = rs.getInt("user_id");
				User toAdd = UserDAO.getUserById(userId);
				
				result.add(toAdd);
			}
			
		} catch (SQLException e) {
			throw new UserDAOException(e.getMessage());
		}
		
		return result;
	}
	
	
}
