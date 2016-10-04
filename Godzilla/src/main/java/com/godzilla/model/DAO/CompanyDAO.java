package com.godzilla.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import com.godzilla.model.DBConnection.DBConnection;
import com.godzilla.model.Company;
import com.godzilla.model.Project;
import com.godzilla.model.User;
import com.godzilla.model.exceptions.CompanyDAOException;
import com.godzilla.model.exceptions.CompanyException;
import com.godzilla.model.exceptions.ProjectDAOException;
import com.godzilla.model.exceptions.UserDAOException;

public class CompanyDAO {

	private static final String GET_COMPANY_NAME_BY_ID_SQL = "SELECT company_name FROM companies WHERE company_id = ?;";
	private static final String REMOVE_COMPANY_SQL = "DELETE FROM companies WHERE company_id = ?;";
	private static final String FIND_COMPANY_BY_ID_SQL = "SELECT * FROM companies WHERE company_id = ?;";
	private static final String FIND_COMPANY_ID_BY_NAME_SQL = "SELECT company_id FROM companies WHERE company_name = ?;";
	private static final String GET_ALL_COMPANY_NAMES_SQL = "SELECT company_name FROM companies;";
	private static final String ADD_COMPANY_SQL = "INSERT INTO companies VALUES(null , ?);";

	public static void createNewCompany(Company newCompany) throws CompanyDAOException {
		if (isThereCompanyWithTheSameName(newCompany.getName())) {
			throw new CompanyDAOException("company's name is taken");
		}
		
		String companyName = newCompany.getName();

		Connection connection = DBConnection.getInstance().getConnection();
		try {
			PreparedStatement insertIntoCompanies = connection.prepareStatement(ADD_COMPANY_SQL,
					Statement.RETURN_GENERATED_KEYS);

			insertIntoCompanies.setString(1, companyName);
			insertIntoCompanies.executeUpdate();

			ResultSet rs = insertIntoCompanies.getGeneratedKeys();

			if (rs.next()) {
				int companyId = rs.getInt(1);
				newCompany.setId(companyId);
			} else {
				throw new CompanyDAOException("failed to create company");
			}
		} catch (SQLException e) {
			throw new CompanyDAOException(e.getMessage());
		} catch (CompanyException e) {
			throw new CompanyDAOException("failed to set company's id", e);
		}

	}

	public static boolean isThereCompanyWithTheSameName(String companyName) throws CompanyDAOException {
		if (companyName == null || companyName.length() == 0) {
			throw new CompanyDAOException("company's name cannot be empty or null");
		}
		
		Connection connection = DBConnection.getInstance().getConnection();
		
		try {
			PreparedStatement selectStatement = connection.prepareStatement(GET_ALL_COMPANY_NAMES_SQL);
			ResultSet rs = selectStatement.executeQuery();

			while (rs.next()) {
				String nextCompanyName = rs.getString(1);
				if (nextCompanyName.equals(companyName)) {
					return true;
				}
			}
		} catch (SQLException e) {
			throw new CompanyDAOException(e.getMessage());
		}
		return false;
	}

	public static int getIdOfCompanyWithName(String companyName) throws CompanyDAOException {
		if (companyName == null || companyName.length() == 0) {
			throw new CompanyDAOException("company's name cannot be empty or null");
		}
		
		Connection connection = DBConnection.getInstance().getConnection();
		int id = 0;
		try {
			PreparedStatement selectCompanyWithName = connection.prepareStatement(FIND_COMPANY_ID_BY_NAME_SQL);
			selectCompanyWithName.setString(1, companyName);

			ResultSet rs = selectCompanyWithName.executeQuery();

			if (rs.next()) {
				id = rs.getInt(1);
			} else {
				throw new CompanyException("couldn't find a company with that name");
			}
		} catch (SQLException e) {
			throw new CompanyDAOException(e.getMessage());
		} catch (CompanyException e) {
			throw new CompanyDAOException(e);
		}
		return id;
	}

	public static Company getCompanyById(int companyId) throws CompanyDAOException {
		if (companyId < 1) {
			throw new CompanyDAOException("can't find company with that id");
		}
		
		Connection connection = DBConnection.getInstance().getConnection();
		Company company = null;

		PreparedStatement selectCompanyById;
		try {
			selectCompanyById = connection.prepareStatement(FIND_COMPANY_BY_ID_SQL);
			selectCompanyById.setInt(1, companyId);
			ResultSet rs = selectCompanyById.executeQuery();
			if (rs.next()) {
				String companyName = rs.getString("company_name");
				company = new Company(companyName);
				company.setId(companyId);

				Set<Project> projects = ProjectDAO.getAllProjectsByCompany(company);
				Set<User> users = UserDAO.getAllUsersByCompany(company);

				for (Project project : projects) {
					company.addProject(project);
				}

				for (User user : users) {
					company.addUser(user);
				}

			} else {
				throw new CompanyDAOException("there is no company with that id");
			}
		} catch (SQLException e) {
			throw new CompanyDAOException(e.getMessage());
		} catch (CompanyException e) {
			throw new CompanyDAOException(e.getMessage());
		} catch (ProjectDAOException e) {
			throw new CompanyDAOException("failed to get company's projects", e);
		} catch (UserDAOException e) {
			e.printStackTrace();
			throw new CompanyDAOException("failed to get company's users", e);
		}

		return company;
	}

	public static void removeCompany(Company toRemove) throws CompanyDAOException {
		if (toRemove == null) {
			throw new CompanyDAOException("can't find company to remove");
		}

		Connection connection = DBConnection.getInstance().getConnection();
		int companyId = toRemove.getId();

		try {
			Set<User> usersToRemove = UserDAO.getAllUsersByCompany(toRemove);
			Set<Project> projectsToRemove = ProjectDAO.getAllProjectsByCompany(toRemove);

			for (User userToRemove : usersToRemove) {
				UserDAO.removeUser(userToRemove);
			}

			for (Project projectToRemove : projectsToRemove) {
				ProjectDAO.removeProject(projectToRemove);
			}

			PreparedStatement removeCompanyPS = connection.prepareStatement(REMOVE_COMPANY_SQL);
			removeCompanyPS.setInt(1, companyId);
			if (removeCompanyPS.executeUpdate() < 1) {
				throw new CompanyDAOException("failed to remove company");
			}

		} catch (SQLException e) {
			throw new CompanyDAOException(e.getMessage());
		} catch (UserDAOException e) {
			e.printStackTrace();
			throw new CompanyDAOException("failed to get company's users", e);
		} catch (ProjectDAOException e) {
			throw new CompanyDAOException("failed to get company's projects", e);
		}

	}

	public static String getCompanyNameById(int companyId) throws CompanyDAOException {
		if (companyId < 1) {
			throw new CompanyDAOException("company id cannot be 0");
		}
		
		Connection connection = DBConnection.getInstance().getConnection();
		String companyName = "";
		
		try {
			PreparedStatement getCompanyNameByIdPS = connection.prepareStatement(GET_COMPANY_NAME_BY_ID_SQL);
			getCompanyNameByIdPS.setInt(1, companyId);
			
			ResultSet getCompanyNameByIdRS = getCompanyNameByIdPS.executeQuery();
			if (getCompanyNameByIdRS.next()) {
				companyName = getCompanyNameByIdRS.getString(1);
			} else {
				throw new CompanyDAOException("failed to get company name");
			}
		} catch (SQLException e) {
			throw new CompanyDAOException(e.getMessage());
		}
		
		return companyName;
	}

}
