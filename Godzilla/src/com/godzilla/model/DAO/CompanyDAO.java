package com.godzilla.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import com.godzilla.DBConnection.DBConnection;
import com.godzilla.model.Company;
import com.godzilla.model.Project;
import com.godzilla.model.User;
import com.godzilla.model.exceptions.CompanyDAOException;
import com.godzilla.model.exceptions.CompanyException;
import com.godzilla.model.exceptions.ProjectDAOException;
import com.godzilla.model.exceptions.UserDAOException;

public class CompanyDAO {

	private static final String REMOVE_COMPANY_SQL = "DELETE FROM companies " + "WHERE company_id = ?;";
	private static final String SELECT_COMPANY_BY_ID_SQL = "Select * from companies where company_id = ?";
	private static final String FIND_COMPANY_ID_BY_NAME = "SELECT company_id from companies where company_name =  ?;";
	private static final String SELECT_NAME_FROM_COMPANIES = "SELECT company_name from companies";
	private static final String INSERT_INTO_COMPANIES = "INSERT INTO companies VALUES(? , ? );";

	public static void createNewCompany(Company newCompany) throws CompanyDAOException {
		if (isThereCompanyWithTheSameName(newCompany.getName())) {
			throw new CompanyDAOException("company's name is taken");
		}

		Connection connection = DBConnection.getInstance().getConnection();
		try {
			PreparedStatement insertIntoCompanies = connection.prepareStatement(INSERT_INTO_COMPANIES,
					Statement.RETURN_GENERATED_KEYS);

			insertIntoCompanies.setInt(1, 0);
			insertIntoCompanies.setString(2, newCompany.getName());
			insertIntoCompanies.executeUpdate();

			ResultSet rs = insertIntoCompanies.getGeneratedKeys();

			if (rs.next()) {
				newCompany.setId(rs.getInt(1));
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
		Connection connection = DBConnection.getInstance().getConnection();
		Statement selectStatement;
		ResultSet rs = null;
		try {
			selectStatement = connection.createStatement();
			rs = selectStatement.executeQuery(SELECT_NAME_FROM_COMPANIES);

			while (rs.next()) {
				if (rs.getString(1).equals(companyName)) {
					return true;
				}
			}
		} catch (SQLException e) {
			throw new CompanyDAOException(e.getMessage());
		}
		return false;
	}

	public static int getIdOfCompanyWithName(String companyName) throws CompanyDAOException {

		Connection connection = DBConnection.getInstance().getConnection();
		int id = 0;
		try {
			PreparedStatement selectCompanyWithName = connection.prepareStatement(FIND_COMPANY_ID_BY_NAME);
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
			selectCompanyById = connection.prepareStatement(SELECT_COMPANY_BY_ID_SQL);
			selectCompanyById.setInt(1, companyId);
			ResultSet rs = selectCompanyById.executeQuery();
			if (rs.next()) {
				company = new Company(rs.getString(2));
				company.setId(companyId);

				Set<Project> projects = ProjectDAO.getAllProjectsByCompany(company);
				Set<User> users = UserDAO.getAllUsersByCompany(company);
				
				for (Project project : projects) {
					company.addNewProject(project);
				}
				
				for (User user : users) {
					company.addNewUser(user);
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
				UserDAO.remmoveUser(userToRemove);
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

}
