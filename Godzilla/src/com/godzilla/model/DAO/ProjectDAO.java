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
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.ProjectDAOException;
import com.godzilla.model.exceptions.ProjectException;
import com.godzilla.model.exceptions.SprintDAOException;

public class ProjectDAO {

	private static final String REMOVE_PROJECT_SQL = "DELETE FROM projects WHERE project_id = ?;";
	private static final String FIND_PROJECT_BY_ID_SQL = "SELECT * FROM projects WHERE project_id = ?;";
	private static final String GET_PROJECT_NAMES_SQL = "SELECT project_name FROM projects;";
	private static final String FIND_PROJECT_ID_BY_NAME_SQL = "SELECT project_id FROM projects WHERE project_name = ?;";
	private static final String ADD_PROJECT_SQL = "INSERT INTO projects VALUES(null ,? ,?);";
	private static final String FIND_ALL_PROJECTS_BY_COMPANY_ID_SQL = "SELECT * FROM projects WHERE company_id = ?;";

	public static void addProject(Project newProject, Company company) throws ProjectDAOException {
		if (company == null || newProject == null) {
			throw new ProjectDAOException("company and project must not be null");
		}
		
		int companyId = company.getId();
		String projectName = newProject.getName();
		
		Connection connection = DBConnection.getInstance().getConnection();

		try {

			PreparedStatement insertIntoProjects = connection.prepareStatement(ADD_PROJECT_SQL,
					Statement.RETURN_GENERATED_KEYS);
			insertIntoProjects.setString(1, projectName);
			insertIntoProjects.setInt(2, companyId);
			insertIntoProjects.executeUpdate();

			ResultSet rs = insertIntoProjects.getGeneratedKeys();

			if (rs.next()) {
				int projectId = rs.getInt(1);
				newProject.setId(projectId);
			} else {
				throw new ProjectDAOException("failed to create project");
			}
		} catch (SQLException e) {
			throw new ProjectDAOException(e.getMessage());
		} catch (ProjectException e) {
			throw new ProjectDAOException("couldn't set project id");
		}

	}

	public static boolean isThereProjectWithTheSameName(String projectName) throws ProjectDAOException {
		if (projectName == null) {
			throw new ProjectDAOException("project name cannot be null");
		}

		Connection connection = DBConnection.getInstance().getConnection();

		try {
			PreparedStatement selectStatement = connection.prepareStatement(GET_PROJECT_NAMES_SQL);
			ResultSet rs = selectStatement.executeQuery();

			while (rs.next()) {
				String nextProjectName = rs.getString(1);
				if (nextProjectName.equals(projectName)) {
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
			throw new ProjectDAOException("company cannot be null");
		}

		int companyId = company.getId();

		Set<Project> result = new HashSet<Project>();

		Connection connection = DBConnection.getInstance().getConnection();
		try {
			PreparedStatement selectProjectsByComapnyId = connection.prepareStatement(FIND_ALL_PROJECTS_BY_COMPANY_ID_SQL);
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
				String projectName = rs.getString("project_name");
				toReturn = new Project(projectName);
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
		if (projectName == null || projectName.length() == 0) {
			throw new ProjectDAOException("project name cannot be empty or null");
		}
		
		Connection connection = DBConnection.getInstance().getConnection();
		int id = 0;
		try {
			PreparedStatement selectProjectWithName = connection.prepareStatement(FIND_PROJECT_ID_BY_NAME_SQL);
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
