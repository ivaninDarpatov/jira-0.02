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
import com.godzilla.model.Project;
import com.godzilla.model.Sprint;
import com.godzilla.model.exceptions.CompanyDAOException;
import com.godzilla.model.exceptions.CompanyException;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.ProjectDAOException;
import com.godzilla.model.exceptions.ProjectException;
import com.godzilla.model.exceptions.SprintDAOException;

public class ProjectDAO {

	private static final String REMOVE_PROJECT_SQL = "DELETE FROM projects WHERE project_id = ?;";
	private static final String FIND_PROJECT_BY_ID_SQL = "SELECT * FROM projects WHERE project_id = ?;";
	public static final String SELECT_NAME_FROM_PROJECTS = "SELECT project_name FROM projects";
	private static final String FIND_PROJECT_ID_BY_NAME = "SELECT project_id from projects where project_name =  ?;";
	public static final String INSERT_INTO_PROJECTS = "INSERT INTO projects VALUES(? , ? , ?);";
	public static final String SELECT_ALL_PROJECTS_WITH_COMPANY_ID_SQL = "SELECT * FROM projects WHERE company_id = ? ";

	public static void addProject(Project newProject, Company company) throws ProjectDAOException {
		if (company == null) {
			throw new ProjectDAOException("couldn't find company");
		}

		String companyName = company.getName();

		Connection connection = DBConnection.getInstance().getConnection();

		try {
			if (!CompanyDAO.isThereCompanyWithTheSameName(companyName)) {
				throw new ProjectDAOException("unknow company to add project to");
			}
		} catch (CompanyDAOException e1) {
			throw new ProjectDAOException(e1.getMessage());
		}

		if (isThereProjectWithTheSameName(newProject.getName())) {
			throw new ProjectDAOException("project already exists");
		}

		int companyId;
		try {
			companyId = CompanyDAO.getIdOfCompanyWithName(companyName);
		} catch (CompanyDAOException e1) {
			throw new ProjectDAOException("couldn't get id of the company with the name" + companyName, e1);
		}

		try {
			PreparedStatement insertIntoProjects = connection.prepareStatement(INSERT_INTO_PROJECTS,
					Statement.RETURN_GENERATED_KEYS);
			insertIntoProjects.setInt(1, 0);
			insertIntoProjects.setString(2, newProject.getName());
			insertIntoProjects.setInt(3, companyId);
			insertIntoProjects.executeUpdate();

			ResultSet rs = insertIntoProjects.getGeneratedKeys();

			if (rs.next()) {
				newProject.setId(rs.getInt(1));
			} else {
				throw new ProjectDAOException("failed to create project");
			}

		} catch (SQLException e) {
			throw new ProjectDAOException(e.getMessage());
		} catch (ProjectException e) {
			throw new ProjectDAOException("couldn't set project's id", e);
		}

	}

	public static boolean isThereProjectWithTheSameName(String projectName) throws ProjectDAOException {
		if (projectName == null) {
			throw new ProjectDAOException("project name cannot be null");
		}

		Connection connection = DBConnection.getInstance().getConnection();
		Statement selectStatement;
		ResultSet rs = null;
		try {
			selectStatement = connection.createStatement();
			rs = selectStatement.executeQuery(SELECT_NAME_FROM_PROJECTS);

			while (rs.next()) {
				if (rs.getString(1).equals(projectName)) {
					return true;
				}
			}
		} catch (SQLException e) {
			throw new ProjectDAOException(e.getMessage());
		}
		return false;
	}

	public static Set<Project> getAllProjectsByCompany(Company company) throws ProjectDAOException {
		if (company == null) {
			throw new ProjectDAOException("can't find company");
		}

		int companyId = company.getId();

		Set<Project> result = new HashSet<Project>();

		Connection connection = DBConnection.getInstance().getConnection();
		try {
			PreparedStatement selectProjectsByComapnyId = connection
					.prepareStatement(SELECT_ALL_PROJECTS_WITH_COMPANY_ID_SQL);
			selectProjectsByComapnyId.setInt(1, companyId);

			ResultSet rs = selectProjectsByComapnyId.executeQuery();

			while (rs.next()) {
				int projectId = rs.getInt(1);

				Project project = ProjectDAO.getProjectById(projectId);

				result.add(project);
			}
		} catch (SQLException e) {
			throw new ProjectDAOException(e.getMessage());

		}
		return result;
	}

	public static Project getProjectById(int projectId) throws ProjectDAOException {
		if (projectId < 1) {
			throw new ProjectDAOException("invalid project id");
		}

		Project toReturn = null;
		Connection connection = DBConnection.getInstance().getConnection();

		try {
			PreparedStatement ps = connection.prepareStatement(FIND_PROJECT_BY_ID_SQL);

			ps.setInt(1, projectId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				toReturn = new Project(rs.getString("project_name"));
				toReturn.setId(projectId);
				Set<Sprint> sprints = SprintDAO.getAllSprintsByProject(toReturn);
				Set<Issue> issues = IssueDAO.getAllIssuesByProject(toReturn);

				
				
				for (Sprint toAdd : sprints) {
					toReturn.addSprint(toAdd);
				}

				for (Issue toAdd : issues) {
					toReturn.addIssue(toAdd);
				}

			}

		} catch (SQLException e) {
			throw new ProjectDAOException(e.getMessage());
		} catch (ProjectException e) {
			throw new ProjectDAOException("failed to create project", e);
		} catch (SprintDAOException e) {
			throw new ProjectDAOException("failed to get sprints", e);
		} catch (IssueDAOException e) {
			e.printStackTrace();
			throw new ProjectDAOException("failed to get issues", e);
		}

		return toReturn;
	}

	public static void removeProject(Project projectToRemove) throws ProjectDAOException {
		if (projectToRemove == null) {
			throw new ProjectDAOException("can't find project to remove");
		}

		Connection connection = DBConnection.getInstance().getConnection();
		int projectId = projectToRemove.getId();

		try {
			Set<Issue> issuesToRemove = IssueDAO.getAllIssuesByProject(projectToRemove);
			Set<Sprint> sprintsToRemove = SprintDAO.getAllSprintsByProject(projectToRemove);

			for (Sprint sprintToRemove : sprintsToRemove) {
				SprintDAO.removeSprint(sprintToRemove);
			}

			for (Issue issueToRemove : issuesToRemove) {
				IssueDAO.removeIssue(issueToRemove);
			}

			PreparedStatement removeProjectPS = connection.prepareStatement(REMOVE_PROJECT_SQL);
			removeProjectPS.setInt(1, projectId);

			if (removeProjectPS.executeUpdate() < 1) {
				throw new ProjectDAOException("failed to remove project");
			}
		} catch (SQLException e) {
			throw new ProjectDAOException(e.getMessage());
		} catch (IssueDAOException e) {
			throw new ProjectDAOException("failed to get projects issues", e);
		} catch (SprintDAOException e) {
			throw new ProjectDAOException("failed to get projects sprints", e);
		}

	}
	
	public static int getProjectIdByName(String projectName) throws ProjectDAOException {
		Connection connection = DBConnection.getInstance().getConnection();
		int id = 0;
		try {
			PreparedStatement selectProjectWithName = connection.prepareStatement(FIND_PROJECT_ID_BY_NAME);
			selectProjectWithName.setString(1, projectName);

			ResultSet rs = selectProjectWithName.executeQuery();

			if (rs.next()) {
				id = rs.getInt(1);
			} else {
				throw new ProjectException("couldn't find a project with that name");
			}
		} catch (SQLException e) {
			throw new ProjectDAOException(e.getMessage());
		} catch (ProjectException e) {
			throw new ProjectDAOException(e);
		}
		return id;
	}
}
