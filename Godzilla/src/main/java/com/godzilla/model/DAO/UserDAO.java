package com.godzilla.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.godzilla.model.DBConnection.DBConnection;
import com.godzilla.model.Company;
import com.godzilla.model.Issue;
import com.godzilla.model.User;
import com.godzilla.model.enums.Permissions;
import com.godzilla.model.exceptions.CompanyDAOException;
import com.godzilla.model.exceptions.CompanyException;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.PermissionException;
import com.godzilla.model.exceptions.UserDAOException;
import com.godzilla.model.exceptions.UserException;

public class UserDAO {
	private static final String UPDATE_PERMISSIONS_SQL = "UPDATE users SET permissions_id = ? WHERE user_id = ?;";
	private static final String CHANGE_PASSWORD_SQL = "UPDATE users SET password= md5(?) WHERE user_id= ?;";
	private static final String CHANGE_AVATAR_SQL = "UPDATE users SET avatar = ? WHERE user_id = ?;";
	private static final String CHANGE_USER_EMAIL_SQL = "UPDATE users SET email = ? WHERE user_id= ?;";
	private static final String FIND_USERS_BY_COMPANY_ID_SQL = "SELECT * FROM users WHERE company_id = ?;";
	private static final int MANAGER_PERMISSIONS = Permissions.MANAGER.ordinal() + 1;
	private static final int PROGRAMMER_PERMISSIONS = Permissions.PROGRAMMER.ordinal() + 1;
	private static final int TESTER_PERMISSIONS = Permissions.TESTER.ordinal() + 1;
	private static final String FIND_USER_ID_BY_EMAIL_SQL = "SELECT user_id FROM users WHERE email = ?;";
	private static final String FIND_USER_BY_EMAIL_PASSWORD_COMPANY_SQL = "SELECT u.user_id FROM users u JOIN companies c ON (c.company_id = u.company_id) WHERE u.email = ? AND u.password = md5(?) AND c.company_name = ?;";
	private static final String FIND_COMPANY_ID_BY_NAME_SQL = "SELECT company_id FROM companies WHERE company_name = ?;";
	private static final String REGISTER_USER_SQL = "INSERT INTO users VALUES (null, ?, md5(?), ? , ?, ?);";
	private static final String REMOVE_USER_SQL = "DELETE FROM users WHERE user_id = ?;";
	private static final String GET_USER_BY_ID_SQL = "SELECT * FROM users WHERE user_id = ?;";

	public static void registerUser(User toRegister) throws UserException, UserDAOException {
		if (toRegister != null) {
			Connection connection = DBConnection.getInstance().getConnection();
			String email = toRegister.getEmail();
			String password = toRegister.getPassword();
			String companyName = toRegister.getCompany();
			String ninjaColor = toRegister.getNinjaColor();
			int permissionsId = 0;
			int companyId;

			try {
				connection.setAutoCommit(false);
				PreparedStatement ps = connection.prepareStatement(FIND_COMPANY_ID_BY_NAME_SQL);
				ps.setString(1, companyName);

				ResultSet rs = ps.executeQuery();
				
				

				if (rs.next()) {
					companyId = rs.getInt("company_id");
					permissionsId = PROGRAMMER_PERMISSIONS;
					
				} else {
					Company newCompany;
					
					try {
						newCompany = new Company(companyName);
						CompanyDAO.createNewCompany(newCompany);
					} catch (CompanyException e1) {
						throw new UserDAOException("Couldn't create company", e1);
					} catch (CompanyDAOException e) {
						throw new UserDAOException("Failed to create company", e);
					}

					companyId = newCompany.getId();
					permissionsId = MANAGER_PERMISSIONS;
					try {
						toRegister.setPermissions(Permissions.MANAGER);
					} catch (PermissionException e) {
						throw new UserException("Invalid permissions", e);
					}
				}

				ps = connection.prepareStatement(REGISTER_USER_SQL, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, email);
				ps.setString(2, password);
				ps.setInt(3, permissionsId);
				ps.setInt(4, companyId);
				ps.setString(5, ninjaColor);

				ps.executeUpdate();

				ResultSet generatedKeys = ps.getGeneratedKeys();
				int userId = 0;
				if (generatedKeys.next()) {
					userId = generatedKeys.getInt(1);
					toRegister.setId(userId);
				} else {
					throw new UserDAOException("Failed to register user");
				}

				connection.commit();

			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					throw new UserDAOException("Failed to register user");
				}
				e.printStackTrace();
				throw new UserDAOException("Failed to register user");
			} finally {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException e) {
					throw new UserDAOException("Failed to register user");
				}
			}
		} else {
			throw new UserDAOException("Invalid user");
		}
	}

	public static boolean validateLogin(User user) throws UserDAOException {
		if (user == null) {
			throw new UserDAOException("couldn't find user");
		}
		Connection connection = DBConnection.getInstance().getConnection();

		try {
			PreparedStatement ps = connection.prepareStatement(FIND_USER_BY_EMAIL_PASSWORD_COMPANY_SQL);
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
			throw new UserDAOException("Failed to validate login");
		}
		return false;
	}
	
	public static void changeEmail(User user,String newEmail) throws UserDAOException{
		if(user == null || newEmail == null){
			throw new UserDAOException("Can't find user");
		}
		
		Connection connection = DBConnection.getInstance().getConnection();
		int id = user.getId();
		
		try {
			PreparedStatement changeEmailPS = connection.prepareStatement(CHANGE_USER_EMAIL_SQL);
			changeEmailPS.setString(1, newEmail);
			changeEmailPS.setInt(2, id);
			
			if(changeEmailPS.executeUpdate() != 1){
				throw new UserDAOException("Email was not changed");
			}
		} catch (SQLException e) {
			throw new UserDAOException("Failed to change email");
		}
	}
	
	public static void changePassword(User user,String newPassword) throws UserDAOException{
		if(user == null || newPassword == null){
			throw new UserDAOException("Can't find user");
		}
		
		Connection connection = DBConnection.getInstance().getConnection();
		int id = user.getId();
		
		try {
			PreparedStatement changeEmailPS = connection.prepareStatement(CHANGE_PASSWORD_SQL);
			changeEmailPS.setString(1, newPassword);
			changeEmailPS.setInt(2, id);
			
			if(changeEmailPS.executeUpdate() != 1){
				throw new UserDAOException("Email was not changed");
			}
		} catch (SQLException e) {
			throw new UserDAOException("Failed to change password");
		}
	}
	
	public static void changeAvatar(User user, String ninjaColor) throws UserDAOException {
		if(user == null || ninjaColor == null){
			throw new UserDAOException("Can't find user");
		}
		
		Connection connection = DBConnection.getInstance().getConnection();
		int id = user.getId();
		
		try {
			PreparedStatement ps = connection.prepareStatement(CHANGE_AVATAR_SQL);
			ps.setString(1, ninjaColor);
			ps.setInt(2, id);
			
			if (ps.executeUpdate() < 1) {
				throw new UserDAOException("Failed to change avatar");
			}
		} catch (SQLException e) {
			throw new UserDAOException(e.getMessage());
		}
	}

	public static void removeUser(User userToRemove) throws UserDAOException {
		if (userToRemove == null) {
			throw new UserDAOException("Can't find user to remove");
		}
		Connection connection = DBConnection.getInstance().getConnection();
		int userId = userToRemove.getId();

		try {
			Set<Issue> assignedIssues = IssueDAO.getAllIssuesAssignedTo(userToRemove);
			Set<Issue> reportedIssues = IssueDAO.getAllReportedIssuesByUser(userToRemove);

			if (userToRemove.isManager()) {
				for (Issue assignedIssue : assignedIssues) {
					IssueDAO.removeIssue(assignedIssue);
				}
				
				for (Issue reportedIssue : reportedIssues) {
					IssueDAO.removeIssue(reportedIssue);
				}
			}
			
			for (Issue assignedIssue : assignedIssues) {
				IssueDAO.unassignIssue(assignedIssue);
			}

			for (Issue reportedIssue : reportedIssues) {
				IssueDAO.handToAdmin(reportedIssue);
			}

			PreparedStatement ps = connection.prepareStatement(REMOVE_USER_SQL);
			ps.setInt(1, userId);

			if (ps.executeUpdate() < 1) {
				throw new UserDAOException("Failed to remove user");
			}
		} catch (SQLException e) {
			throw new UserDAOException("Failed to remove user");
		} catch (IssueDAOException e) {
			throw new UserDAOException("failed to get issues", e);
		}
	}

	public static User getUserById(int userId) throws UserDAOException {
		if (userId < 1) {
			throw new UserDAOException("Can't find user with that id");
		}
		User user = null;
		Connection connection = DBConnection.getInstance().getConnection();

		try {
			connection.setAutoCommit(false);
			PreparedStatement selectIdNamePasswordPermission = connection.prepareStatement(GET_USER_BY_ID_SQL);
			selectIdNamePasswordPermission.setInt(1, userId);

			ResultSet rs = selectIdNamePasswordPermission.executeQuery();

			if (rs.next()) {
			
				String email = rs.getString("email");
				String password = rs.getString("password");
				int permissions = rs.getInt("permissions_id");
				int companyId = rs.getInt("company_id");
				String ninjaColor = rs.getString("avatar");
				String companyName = CompanyDAO.getCompanyNameById(companyId);
				Permissions userPermissions = Permissions.getPermissionsById(permissions);

				user = new User(email, password, companyName);
				user.setId(userId);
				user.setPermissions(userPermissions);
				user.setNinjaColor(ninjaColor);
				

				for (Issue issue : IssueDAO.getAllReportedIssuesByUser(user)) {
					user.addIssueReportedByMe(issue);
				}

				for (Issue issue : IssueDAO.getAllIssuesAssignedTo(user)) {
					user.addIssueAssignedToMe(issue);
				}

			} else {
				throw new UserDAOException("There is no such user");
			}
		} catch (SQLException e) {
			throw new UserDAOException("Failed to find user");
		} catch (UserException e) {
			throw new UserDAOException("user not created", e);
		} catch (PermissionException e) {
			throw new UserDAOException("permissions not set", e);
		} catch (IssueDAOException e) {
			throw new UserDAOException("failed to get issues", e);
		} catch (CompanyDAOException e) {
			throw new UserDAOException("failed to get company name", e);
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				throw new UserDAOException("Failed to find user");
			}
		}
		return user;
	}

	public static Set<User> getAllUsersByCompany(Company company) throws UserDAOException {
		if (company == null) {
			throw new UserDAOException("can't find company");
		}

		Connection connection = DBConnection.getInstance().getConnection();
		Set<User> result = new HashSet<User>();
		int companyId = company.getId();

		try {
			PreparedStatement ps = connection.prepareStatement(FIND_USERS_BY_COMPANY_ID_SQL);
			ps.setInt(1, companyId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int userId = rs.getInt(1);
				User toAdd = UserDAO.getUserById(userId);

				result.add(toAdd);
			}

		} catch (SQLException e) {
			throw new UserDAOException("Failed to get users by company");
		}

		return result;
	}

	public static int getUserIdByEmail(String userEmail) throws UserDAOException {
		if (userEmail == null) {
			throw new UserDAOException("email cannot be null");
		}

		Connection connection = DBConnection.getInstance().getConnection();
		int id = 0;
		try {
			PreparedStatement selectUserWithEmail = connection.prepareStatement(FIND_USER_ID_BY_EMAIL_SQL);
			selectUserWithEmail.setString(1, userEmail);

			ResultSet rs = selectUserWithEmail.executeQuery();

			if (rs.next()) {
				id = rs.getInt(1);
			} else {
				throw new UserException("Couldn't find a user with that email");
			}
		} catch (SQLException e) {
			throw new UserDAOException("Failed to find user");
		} catch (UserException e) {
			throw new UserDAOException(e);
		}
		return id;
	}

	public static void updatePermissions(User toUpdate, Permissions newPermissions) throws UserDAOException {
		if (toUpdate == null) {
			throw new UserDAOException("user cannot be null");
		}
		
		int userId = toUpdate.getId();
		int permissionsId = newPermissions.ordinal() + 1;
		Connection connection = DBConnection.getInstance().getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(UPDATE_PERMISSIONS_SQL);
			ps.setInt(1, permissionsId);
			ps.setInt(2, userId);
			
			if (ps.executeUpdate() < 1) {
				throw new UserDAOException("failed to update user permissions");
			}
		} catch (SQLException e) {
			throw new UserDAOException(e.getMessage());
		}
		
	}
}
