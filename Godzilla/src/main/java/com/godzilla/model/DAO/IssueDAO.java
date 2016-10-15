package com.godzilla.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import com.godzilla.model.DBConnection.DBConnection;
import com.godzilla.model.*;
import com.godzilla.model.enums.IssuePriority;
import com.godzilla.model.enums.IssueState;
import com.godzilla.model.enums.Permissions;
import com.godzilla.model.exceptions.EpicException;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.IssueException;
import com.google.gson.Gson;

public class IssueDAO {
	private static final String EDIT_ISSUE_SQL = "UPDATE issues SET summary= ?, description= ?, state_id= ?, priority=? WHERE issue_id=?;";
	private static final String FIND_ISSUES_BY_STATE_SQL = "SELECT issue_id FROM issues WHERE state_id = ?;";
	private static final String SELECT_ISSUE_ID_BY_NAME_SQL = "SELECT issue_id from issues WHERE issue_name = ?;";
	private static final String FIND_FREE_ISSUES_BY_PROJECT_SQL = "SELECT issue_id FROM issues WHERE sprint_id IS NULL AND project_id = ?;";
	private static final String GET_ISSUE_TYPE_SQL = "SELECT issue_type FROM issues WHERE issue_id = ?;";
	private static final String FIND_ISSUE_EPIC_SQL = "SELECT epic_id FROM issues WHERE issue_id = ?;";
	private static final String GET_EPIC_NAME_BY_ID_SQL = "SELECT epic_name FROM epics WHERE epic_id = ?;";
	private static final String ASSIGN_ISSUE_SQL = "UPDATE issues SET assignee_id = ? WHERE issue_id = ?;";
	private static final String GET_SPRINT_ID_FOR_ISSUE_SQL = "SELECT sprint_id FROM issues WHERE issue_id = ?;";
	private static final String ADD_ISSUE_TO_EPIC_SQL = "UPDATE issues SET epic_id = ? WHERE issue_id = ?;";
	private static final String ADD_ISSUE_TO_SPRINT_SQL = "UPDATE issues SET sprint_id = ? WHERE issue_id = ?;";
	private static final String HAND_ISSUE_TO_ADMIN_SQL = "UPDATE issues SET reporter_id = ? WHERE issue_id = ?;";
	private static final String FIND_COMPANY_FOR_PROJECT_SQL = "SELECT c.* FROM companies c JOIN projects p ON (c.company_id = p.company_id) WHERE p.project_id = ?;";
	private static final String FIND_PROJECT_FOR_ISSUE_SQL = "SELECT p.* FROM projects p JOIN issues i ON (i.project_id = p.project_id) WHERE i.issue_id = ?;";
	private static final String FIND_ADMIN_FOR_COMPANY_SQL = "SELECT * FROM users WHERE permissions_id = 1 AND company_id = ?;";
	private static final String GET_ISSUES_ASSIGNED_TO_USER_SQL = "SELECT issue_id FROM issues WHERE assignee_id = ?;";
	private static final String GET_ISSUES_ASSIGNED_TO_USER_IN_PROJECT_SQL = "SELECT issue_id FROM issues WHERE assignee_id = ? AND project_id = ?;";
	private static final String UNASSIGN_ISSUE_SQL = "UPDATE issues SET assignee_id = null WHERE issue_id = ?;";
	private static final String REMOVE_ISSUE_FROM_SPRINT_SQL = "UPDATE issues SET sprint_id = null WHERE issue_id = ?;";
	private static final String REMOVE_ISSUE_FROM_EPIC_SQL = "UPDATE issues SET epic_id = null WHERE issue_id = ?;";
	private static final String ADD_EPIC_SQL = "INSERT INTO epics VALUES(? , ?);";
	private static final String FIND_ISSUES_BY_EPIC_ID_SQL = "SELECT issue_id FROM issues WHERE epic_id = ?;";
	private static final String FIND_ISSUES_BY_PROJECT_ID_SQL = "SELECT issue_id FROM issues WHERE project_id = ?;";
	private static final String IS_EPIC_SQL = "SELECT epic_id FROM epics WHERE epic_id = ?;";
	private static final String REMOVE_ISSUE_SQL = "DELETE FROM issues WHERE issue_id = ?;";
	private static final String CREATE_ISSUE_SQL = "INSERT INTO issues VALUES (null, ?, ? , ?, ?, ?, ?, ?, ?, ?, 'someDate', 'someDate', null, null);";
	private static final String FIND_ISSUE_BY_REPORTER_SQL = "SELECT issue_id FROM issues WHERE reporter_id = ?;";
	private static final String FIND_ISSUE_BY_REPORTER_IN_PROJECT_SQL = "SELECT issue_id FROM issues WHERE reporter_id = ? AND project_id = ?;";
	private static final String FIND_ISSUE_BY_ID_SQL = "SELECT * FROM issues WHERE issue_id = ?;";
	private static final String FIND_ISSUES_BY_SPRINT_SQL = "SELECT issue_id FROM issues WHERE sprint_id = ?;";

	public static void createIssue(Issue toCreate, Project project, User reporter, User assignee)
			throws IssueDAOException {
		if (toCreate == null || project == null || reporter == null) {
			throw new IssueDAOException("cannot create issue, required: project, reporter and issue model");
		}

		if (reporter.getPermissions().equals(Permissions.TESTER) && !toCreate.getType().equals("bug")) {
			throw new IssueDAOException("testers can only report bugs");
		}

		if (reporter.getPermissions().equals(Permissions.PROGRAMMER)
				&& (toCreate.getType().equals("epic") || toCreate.getType().equals("bug"))) {
			throw new IssueDAOException("programmers can only report tasks and stories");
		}

		String issueType = toCreate.getType();

		Connection connection = DBConnection.getInstance().getConnection();
		int issueId;

		String summary = toCreate.getSummary();
		int issueNumber = project.getIssues().size() + 1;
		
		
		String companyInitial = reporter.getCompany().substring(0, 2) + reporter.getCompany().substring(reporter.getCompany().length() - 1);
		String projectInitial = project.getName().substring(0, 2) + project.getName().substring(project.getName().length() - 1);
		String issueName = companyInitial.toUpperCase() + "-" + projectInitial.toUpperCase() + "-" + issueNumber;

		String description = toCreate.getDescription();
		int priorityId = toCreate.getPriority().getValue();
		int stateId = toCreate.getState().getValue();
		int projectId = project.getId();
		int reporterId = reporter.getId();
		int assigneeId = assignee.getId();
		// String dateTimeCreated =
		// toCreate.getDateCreated().toLocalDate().toString() +
		// " " +
		// toCreate.getDateCreated().toLocalTime().toString();
		// String dateTimeLastModified =
		// toCreate.getDateLastModified().toLocalDate().toString() +
		// " " +
		// toCreate.getDateLastModified().toLocalTime().toString();

		try {
			connection.setAutoCommit(false);
			PreparedStatement insertIntoIssues = connection.prepareStatement(CREATE_ISSUE_SQL,
					Statement.RETURN_GENERATED_KEYS);
			insertIntoIssues.setString(1, issueType);
			insertIntoIssues.setString(2, issueName);
			insertIntoIssues.setString(3, summary);
			insertIntoIssues.setString(4, description);
			insertIntoIssues.setInt(5, projectId);
			insertIntoIssues.setInt(6, stateId);
			insertIntoIssues.setInt(7, priorityId);
			insertIntoIssues.setInt(8, reporterId);
			insertIntoIssues.setInt(9, assigneeId);
			// insertIntoIssues.setString(8, dateTimeCreated);
			// insertIntoIssues.setString(9, dateTimeLastModified);

			if (insertIntoIssues.executeUpdate() > 0) {
				ResultSet rs = insertIntoIssues.getGeneratedKeys();

				if (rs.next()) {
					issueId = rs.getInt(1);
					toCreate.setId(issueId);
					toCreate.setName(issueName);
				} else {
					throw new IssueDAOException("could not set id of an issue");
				}

			} else {
				throw new IssueDAOException("failed to create issue");
			}

			if (issueType.equals("epic")) {
				PreparedStatement insertIntoEpic = null;
				insertIntoEpic = connection.prepareStatement(ADD_EPIC_SQL);
				insertIntoEpic.setInt(1, issueId);
				insertIntoEpic.setString(2, ((Epic) toCreate).getEpicName());

				if (insertIntoEpic.executeUpdate() < 1) {
					throw new IssueDAOException("failed to insert epic properly");
				}

				PreparedStatement addIssueToEpicPS = connection.prepareStatement(ADD_ISSUE_TO_EPIC_SQL);
				addIssueToEpicPS.setInt(1, issueId);
				addIssueToEpicPS.setInt(2, issueId);
				addIssueToEpicPS.executeUpdate();
			}

			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new IssueDAOException(e1.getMessage());
			}
			throw new IssueDAOException(e.getMessage());
		} catch (IssueException e) {
			throw new IssueDAOException("couldn't set issue's id", e);
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				throw new IssueDAOException(e.getMessage());
			}
		}
	}

	public static Set<Issue> getAllIssuesByProject(Project project) throws IssueDAOException {
		if (project == null) {
			throw new IssueDAOException("couldn't find project");
		}

		int projectId = project.getId();
		Set<Issue> result = new HashSet<Issue>();

		Connection connection = DBConnection.getInstance().getConnection();

		try {
			PreparedStatement ps = connection.prepareStatement(FIND_ISSUES_BY_PROJECT_ID_SQL);
			ps.setInt(1, projectId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int issueId = rs.getInt(1);
				Issue issueToAdd = IssueDAO.getIssueById(issueId);
				result.add(issueToAdd);
			}

		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}

		return result;
	}

	public static Issue getIssueById(int issueId) throws IssueDAOException {
		if (issueId < 1) {
			throw new IssueDAOException("cannot find issue with that id");
		}

		Connection connection = DBConnection.getInstance().getConnection();
		Issue issue = null;

		try {
			PreparedStatement ps = connection.prepareStatement(FIND_ISSUE_BY_ID_SQL);
			ps.setInt(1, issueId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				String issueType = rs.getString("issue_type");
				String issueName = rs.getString("issue_name");
				String summary = rs.getString("summary");
				String description = rs.getString("description");
				int priorityId = rs.getInt("priority");
				int stateId = rs.getInt("state_id");
				// LocalDateTime dateCreated =
				// getLocalDateTimeFromString(rs.getString("date_created"));
				// LocalDateTime dateLastModified =
				// getLocalDateTimeFromString(rs.getString("date_last_modified"));
				IssuePriority priority = IssuePriority.getTypeById(priorityId);
				IssueState state = IssueState.getTypeById(stateId);

				if (issueType.equals("epic")) {
					String epicName = IssueDAO.getEpicNameById(issueId);
					issue = new Epic(summary, epicName);
					issue.setId(issueId);
					Set<Issue> issues = IssueDAO.getAllIssuesByEpic((Epic) issue);

					for (Issue entry : issues) {
						((Epic) issue).addIssue(entry);
					}
				} else {
					issue = new Issue(summary, issueType);
				}

				issue.setName(issueName);
				issue.setDescription(description);
				issue.setPriority(priority);
				issue.setState(state);
				// issue.setDateCreated(dateCreated);
				// issue.setDateLastModified(dateLastModified);

				issue.setId(issueId);
				issue.setType(issueType);
			}

		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		} catch (IssueException e) {
			throw new IssueDAOException("could not create issue", e);
		} catch (EpicException e) {
			throw new IssueDAOException("failed to create epic", e);
		}

		return issue;
	}
	
	public static void editIssue(Issue issue) throws IssueDAOException{
		if(issue == null){
			throw new IssueDAOException("Issue cannot be null");
		}
		
		Connection connection = DBConnection.getInstance().getConnection();
		
		int issueId = issue.getId();
		String summary = issue.getSummary();
		IssuePriority priority = issue.getPriority();
		int priorityId = priority.getValue();
		IssueState state = issue.getState();
		int stateId = state.getValue();
		String description = issue.getDescription();
		
		
		try {
			PreparedStatement editIssuePS = connection.prepareStatement(EDIT_ISSUE_SQL);
			editIssuePS.setString(1, summary);
			editIssuePS.setString(2, description);
			editIssuePS.setInt(3, stateId);
			editIssuePS.setInt(4, priorityId);
			editIssuePS.setInt(5, issueId);
			
			if(editIssuePS.executeUpdate() != 1){
				throw new IssueDAOException("Could not edit issue");
			}
		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}
	}

	public static int getIssueIdByName(String issueName) throws IssueDAOException {
		if (issueName == null || issueName.length() == 0) {
			throw new IssueDAOException("name cannot be null or empty");
		}

		Connection connection = DBConnection.getInstance().getConnection();

		int issueId = 0;

		try {
			PreparedStatement selectIdByNamePS = connection.prepareStatement(SELECT_ISSUE_ID_BY_NAME_SQL);
			selectIdByNamePS.setString(1, issueName);

			ResultSet rs = selectIdByNamePS.executeQuery();

			if (rs.next()) {
				issueId = rs.getInt(1);
			} else {
				throw new IssueDAOException("Cannot find issue with that name");
			}

			return issueId;
		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}
	}

	private static String getEpicNameById(int epicId) throws IssueDAOException {
		if (epicId < 1) {
			throw new IssueDAOException("issue id cannot be 0");
		}

		String epicName = GET_ISSUE_TYPE_SQL;
		Connection connection = DBConnection.getInstance().getConnection();

		try {
			PreparedStatement getEpicNameByIdPS = connection.prepareStatement(GET_EPIC_NAME_BY_ID_SQL);
			getEpicNameByIdPS.setInt(1, epicId);

			ResultSet getEpicNameByIdRS = getEpicNameByIdPS.executeQuery();
			if (getEpicNameByIdRS.next()) {
				epicName = getEpicNameByIdRS.getString("epic_name");
			}
		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}

		return epicName;
	}

	public static Set<Issue> getAllIssuesByEpic(Epic epic) throws IssueDAOException {
		if (epic == null) {
			throw new IssueDAOException("couldn't find epic");
		}

		int epicId = epic.getId();

		Set<Issue> result = new HashSet<Issue>();

		Connection connection = DBConnection.getInstance().getConnection();

		try {
			PreparedStatement ps = connection.prepareStatement(FIND_ISSUES_BY_EPIC_ID_SQL);
			ps.setInt(1, epicId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int issueId = rs.getInt(1);
				if (IssueDAO.isEpic(issueId)) {
					continue;
				}
				Issue issueToAdd = IssueDAO.getIssueById(issueId);
				result.add(issueToAdd);
			}

		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}

		return result;
	}

	public static void removeIssue(Issue toRemove) throws IssueDAOException {
		if (toRemove == null) {
			throw new IssueDAOException("invalid issue to remove");
		}
		Connection connection = DBConnection.getInstance().getConnection();

		int issueId = toRemove.getId();

		try {
			connection.setAutoCommit(false);
			String issueType = IssueDAO.getIssueType(issueId);
			if (issueType.equals("epic")) {
				final String DELETE_EPIC_SQL = "DELETE FROM epics WHERE epic_id = ?;";
				PreparedStatement ps = connection.prepareStatement(DELETE_EPIC_SQL);
				ps.setInt(1, issueId);

				if (ps.executeUpdate() < 1) {
					throw new IssueDAOException("failed to remove " + issueType);
				}
			}

			if (IssueDAO.issueIsInEpic(toRemove)) {
				IssueDAO.removeFromEpic(toRemove);
			}

			PreparedStatement ps = connection.prepareStatement(REMOVE_ISSUE_SQL);
			ps.setInt(1, issueId);

			if (ps.executeUpdate() < 1) {
				throw new IssueDAOException("failed to remove issue");
			}

			connection.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new IssueDAOException(e1.getMessage());
			}
			throw new IssueDAOException(e.getMessage());
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				throw new IssueDAOException(e.getMessage());
			}
		}
	}

	private static void removeFromEpic(Issue toRemove) throws IssueDAOException {
		if (toRemove == null) {
			throw new IssueDAOException("can't find issue to set free");
		}
		if (!issueIsInEpic(toRemove)) {
			throw new IssueDAOException("Issue is not in epic");
		}

		Connection connection = DBConnection.getInstance().getConnection();
		int issueId = toRemove.getId();

		try {
			PreparedStatement removeIssueFromEpicPS = connection.prepareStatement(REMOVE_ISSUE_FROM_EPIC_SQL);
			removeIssueFromEpicPS.setInt(1, issueId);

			if (removeIssueFromEpicPS.executeUpdate() < 1) {
				throw new IssueDAOException("failed to remove issue from epic");
			}
		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}
	}

	private static boolean issueIsInEpic(Issue toRemove) throws IssueDAOException {
		if (toRemove == null) {
			throw new IssueDAOException("invalid issue to remove");
		}

		int issueId = toRemove.getId();
		Connection connection = DBConnection.getInstance().getConnection();

		try {
			PreparedStatement getIssueEpicPS = connection.prepareStatement(FIND_ISSUE_EPIC_SQL);
			getIssueEpicPS.setInt(1, issueId);

			ResultSet rs = getIssueEpicPS.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}

		return false;
	}

	public static Set<Issue> getAllReportedIssuesByUser(User reporter) throws IssueDAOException {
		if (reporter == null) {
			throw new IssueDAOException("couldn't find user");
		}

		Set<Issue> reportedByThatUser = new HashSet<Issue>();
		Connection connection = DBConnection.getInstance().getConnection();
		int reporterId = reporter.getId();

		try {
			PreparedStatement ps = connection.prepareStatement(FIND_ISSUE_BY_REPORTER_SQL);

			ps.setInt(1, reporterId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int issueId = rs.getInt(1);
				Issue issueToAdd = IssueDAO.getIssueById(issueId);
				reportedByThatUser.add(issueToAdd);
			}

		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}

		return reportedByThatUser;
	}

	public static Set<Issue> getAllReportedIssuesByUser(User reporter, Project project) throws IssueDAOException {
		if (reporter == null) {
			throw new IssueDAOException("couldn't find user");
		}

		Set<Issue> reportedByThatUser = new HashSet<Issue>();
		Connection connection = DBConnection.getInstance().getConnection();
		int reporterId = reporter.getId();
		int projectId = project.getId();

		try {
			PreparedStatement ps = connection.prepareStatement(FIND_ISSUE_BY_REPORTER_IN_PROJECT_SQL);

			ps.setInt(1, reporterId);
			ps.setInt(2, projectId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int issueId = rs.getInt(1);
				Issue issueToAdd = IssueDAO.getIssueById(issueId);
				reportedByThatUser.add(issueToAdd);
			}

		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}

		return reportedByThatUser;
	}

	private static String getIssueType(int issueId) throws IssueDAOException {
		if (issueId < 1) {
			throw new IssueDAOException("cannot find issue with that id");
		}

		String type = FIND_FREE_ISSUES_BY_PROJECT_SQL;
		Connection connection = DBConnection.getInstance().getConnection();

		try {
			PreparedStatement getTypePS = connection.prepareStatement(GET_ISSUE_TYPE_SQL);
			getTypePS.setInt(1, issueId);

			ResultSet getTypeRS = getTypePS.executeQuery();
			if (getTypeRS.next()) {
				type = getTypeRS.getString("issue_type");
			}
		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}

		return type;

	}

	private static boolean isEpic(int issueId) throws IssueDAOException {
		if (issueId < 1) {
			throw new IssueDAOException("cannot find issue with that id");
		}
		Connection connection = DBConnection.getInstance().getConnection();

		try {
			PreparedStatement ps = connection.prepareStatement(IS_EPIC_SQL);
			ps.setInt(1, issueId);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return true;
			}

		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}

		return false;
	}

	public static void removeFromSprint(Issue issueToSetFree) throws IssueDAOException {
		if (issueToSetFree == null) {
			throw new IssueDAOException("can't find issue to set free");
		}
		if (!isIssueInSprint(issueToSetFree)) {
			throw new IssueDAOException("Issue is not in sprint");
		}

		Connection connection = DBConnection.getInstance().getConnection();
		int issueId = issueToSetFree.getId();

		try {
			PreparedStatement removeIssueFromSprintPS = connection.prepareStatement(REMOVE_ISSUE_FROM_SPRINT_SQL);
			removeIssueFromSprintPS.setInt(1, issueId);

			if (removeIssueFromSprintPS.executeUpdate() < 1) {
				throw new IssueDAOException("failed to remove issue from sprint");
			}
		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}
	}

	private static boolean isIssueInSprint(Issue issue) throws IssueDAOException {
		if (issue == null) {
			throw new IssueDAOException("issue cannot be null");
		}

		Connection connection = DBConnection.getInstance().getConnection();

		PreparedStatement getIssueSprint;
		try {
			getIssueSprint = connection.prepareStatement(GET_SPRINT_ID_FOR_ISSUE_SQL);
			getIssueSprint.setInt(1, issue.getId());

			ResultSet rs = getIssueSprint.executeQuery();

			if (rs.next()) {
				return !(rs.getInt(1) == 0);
			}
		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}

		return false;
	}

	public static Set<Issue> getAllIssuesBySprint(Sprint sprint) throws IssueDAOException {
		if (sprint == null) {
			throw new IssueDAOException("couldn't find sprint");
		}

		int sprintId = sprint.getId();
		Set<Issue> result = new HashSet<Issue>();

		Connection connection = DBConnection.getInstance().getConnection();

		try {
			PreparedStatement ps = connection.prepareStatement(FIND_ISSUES_BY_SPRINT_SQL);
			ps.setInt(1, sprintId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int issueId = rs.getInt(1);
				Issue issueToAdd = IssueDAO.getIssueById(issueId);
				result.add(issueToAdd);
			}

		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}

		return result;
	}

	public static void assignIssue(Issue toAssign, User assignee) throws IssueDAOException {
		if (toAssign == null || assignee == null) {
			throw new IssueDAOException("issue to assign and assignee must not be null");
		}

		if (assignee.getPermissions().equals(Permissions.TESTER)) {
			throw new IssueDAOException("testers cannot be assigned issues");
		}

		if (toAssign.getType().equals("epic")) {
			throw new IssueDAOException("epics cannot be reassigned");
		}

		Connection connection = DBConnection.getInstance().getConnection();
		int issueId = toAssign.getId();
		int assigneeId = assignee.getId();

		try {
			PreparedStatement assignIssuePS = connection.prepareStatement(ASSIGN_ISSUE_SQL);
			assignIssuePS.setInt(1, assigneeId);
			assignIssuePS.setInt(2, issueId);

			if (assignIssuePS.executeUpdate() < 1) {
				throw new IssueDAOException("failed to assign issue");
			}
		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}
	}

	public static void unassignIssue(Issue assignedIssue) throws IssueDAOException {
		if (assignedIssue == null) {
			throw new IssueDAOException("couldn't find assigned issue");
		}

		Connection connection = DBConnection.getInstance().getConnection();
		int issueId = assignedIssue.getId();

		try {
			PreparedStatement unassignIssuePS = connection.prepareStatement(UNASSIGN_ISSUE_SQL);
			unassignIssuePS.setInt(1, issueId);

			if (unassignIssuePS.executeUpdate() < 1) {
				throw new IssueDAOException("failed to unassign issue");
			}

		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}
	}

	public static void handToAdmin(Issue reportedIssue) throws IssueDAOException {
		if (reportedIssue == null) {
			throw new IssueDAOException("couldn't find reported issue");
		}

		Connection connection = DBConnection.getInstance().getConnection();
		int issueId = reportedIssue.getId();

		try {
			PreparedStatement findIssueProjectPS = connection.prepareStatement(FIND_PROJECT_FOR_ISSUE_SQL);
			findIssueProjectPS.setInt(1, issueId);
			int projectId;

			ResultSet issueProjectRS = findIssueProjectPS.executeQuery();
			if (issueProjectRS.next()) {
				projectId = issueProjectRS.getInt(1);
			} else {
				throw new IssueDAOException("couldn't find issue's project");
			}

			PreparedStatement findProjectCompanyPS = connection.prepareStatement(FIND_COMPANY_FOR_PROJECT_SQL);
			findProjectCompanyPS.setInt(1, projectId);
			int companyId;

			ResultSet projectCompanyRS = findProjectCompanyPS.executeQuery();
			if (projectCompanyRS.next()) {
				companyId = projectCompanyRS.getInt(1);
			} else {
				throw new IssueDAOException("couldn't find project's company");
			}

			PreparedStatement findCompanyAdminPS = connection.prepareStatement(FIND_ADMIN_FOR_COMPANY_SQL);
			findCompanyAdminPS.setInt(1, companyId);
			int companyAdminId;

			ResultSet companyAdminRS = findCompanyAdminPS.executeQuery();
			if (companyAdminRS.next()) {
				companyAdminId = companyAdminRS.getInt(1);
				PreparedStatement handIssueToAdminPS = connection.prepareStatement(HAND_ISSUE_TO_ADMIN_SQL);
				handIssueToAdminPS.setInt(1, companyAdminId);
				handIssueToAdminPS.setInt(2, issueId);

				if (handIssueToAdminPS.executeUpdate() < 1) {
					throw new IssueDAOException("failed to hand issue to admin");
				}
			} else {
				throw new IssueDAOException("couldn't find company admin");
			}

		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}
	}

	public static Set<Issue> getAllIssuesAssignedTo(User assignee) throws IssueDAOException {
		if (assignee == null) {
			throw new IssueDAOException("couldn't find user");
		}

		Set<Issue> assignedToUser = new HashSet<Issue>();
		Connection connection = DBConnection.getInstance().getConnection();
		int assigneeId = assignee.getId();

		try {
			PreparedStatement ps = connection.prepareStatement(GET_ISSUES_ASSIGNED_TO_USER_SQL);
			ps.setInt(1, assigneeId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int issueId = rs.getInt(1);
				Issue issueToAdd = IssueDAO.getIssueById(issueId);
				assignedToUser.add(issueToAdd);
			}

		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}

		return assignedToUser;
	}

	public static Set<Issue> getAllIssuesAssignedTo(User assignee, Project project) throws IssueDAOException {
		if (assignee == null) {
			throw new IssueDAOException("couldn't find user");
		}

		Set<Issue> assignedToUser = new HashSet<Issue>();
		Connection connection = DBConnection.getInstance().getConnection();
		int assigneeId = assignee.getId();
		int projectId = project.getId();

		try {
			PreparedStatement ps = connection.prepareStatement(GET_ISSUES_ASSIGNED_TO_USER_IN_PROJECT_SQL);
			ps.setInt(1, assigneeId);
			ps.setInt(2, projectId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int issueId = rs.getInt(1);
				Issue issueToAdd = IssueDAO.getIssueById(issueId);
				assignedToUser.add(issueToAdd);
			}

		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}

		return assignedToUser;
	}

	static LocalDateTime getLocalDateTimeFromString(String dateTime) throws IssueDAOException {
		if (dateTime == null) {
			throw new IssueDAOException("cannot convert null value to LocalDateTime");
		}
		System.err.println(dateTime);
		LocalDateTime result;

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS.nnn");
		result = LocalDateTime.parse(dateTime, formatter);

		return result;
	}

	public static void addIssueToEpic(Issue issue, Epic epic) throws IssueDAOException {
		if (epic == null || issue == null) {
			throw new IssueDAOException("epic and issue cannot be null");
		}

		int epicId = epic.getId();
		int issueId = issue.getId();
		Connection connection = DBConnection.getInstance().getConnection();

		try {
			PreparedStatement addIssueToEpicPS = connection.prepareStatement(ADD_ISSUE_TO_EPIC_SQL);
			addIssueToEpicPS.setInt(1, epicId);
			addIssueToEpicPS.setInt(2, issueId);

			if (addIssueToEpicPS.executeUpdate() < 1) {
				throw new IssueDAOException("Failed to add issue in epic");
			}

		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}

	}

	public static void addIssueToSprint(Issue issue, Sprint sprint) throws IssueDAOException {
		if (sprint == null || issue == null) {
			throw new IssueDAOException("sprint and issue cannot be null");
		}

		int sprintId = sprint.getId();
		int issueId = issue.getId();
		Connection connection = DBConnection.getInstance().getConnection();

		try {
			PreparedStatement addIssueToEpicPS = connection.prepareStatement(ADD_ISSUE_TO_SPRINT_SQL);
			addIssueToEpicPS.setInt(1, sprintId);
			addIssueToEpicPS.setInt(2, issueId);

			if (addIssueToEpicPS.executeUpdate() < 1) {
				throw new IssueDAOException("Failed to add issue in epic");
			}

		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}

	}

	public static Set<Issue> getAllFreeIssuesByProject(Project project) throws IssueDAOException {
		if (project == null) {
			throw new IssueDAOException("project cannot be null");
		}

		int projectId = project.getId();
		Set<Issue> freeIssues = new HashSet<Issue>();
		Connection connection = DBConnection.getInstance().getConnection();

		try {
			PreparedStatement getFreeIssuesPS = connection.prepareStatement(FIND_FREE_ISSUES_BY_PROJECT_SQL);
			getFreeIssuesPS.setInt(1, projectId);

			ResultSet freeIssuesRS = getFreeIssuesPS.executeQuery();

			while (freeIssuesRS.next()) {
				int issueId = freeIssuesRS.getInt("issue_id");
				Issue issue = IssueDAO.getIssueById(issueId);
				freeIssues.add(issue);
			}
		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}

		return freeIssues;
	}

	public static Set<Issue> getAllIssuesFilteredBy(String issueState, String projectName, String sprintName,
			String reporterEmail, String assigneeEmail) throws IssueDAOException {
		Set<Issue> result = new HashSet<Issue>();

		Set<Issue> byState = new HashSet<Issue>();
		Set<Issue> byProject = new HashSet<Issue>();
		Set<Issue> bySprint = new HashSet<Issue>();
		Set<Issue> byReporter = new HashSet<Issue>();
		Set<Issue> byAssignee = new HashSet<Issue>();

		try {
			IssueState state = null;
			if (issueState != null && issueState.length() > 0) {
				state = IssueState.getIssueStateFromString(issueState.toLowerCase());
				byState = IssueDAO.getAllIssuesByState(state);
			}
			Project project = null;
			if (projectName != null && projectName.length() > 0) {
				project = ProjectDAO.getProjectById(ProjectDAO.getProjectIdByName(projectName));
				byProject = IssueDAO.getAllIssuesByProject(project);
			}
			Sprint sprint = null;
			if (sprintName != null && sprintName.length() > 0) {
				sprint = SprintDAO.getSprintById(SprintDAO.getSprintIdByName(sprintName));
				bySprint = IssueDAO.getAllIssuesBySprint(sprint);
			}
			User reporter = null;
			if (reporterEmail != null && reporterEmail.length() > 0) {
				reporter = UserDAO.getUserById(UserDAO.getUserIdByEmail(reporterEmail));
				byReporter = IssueDAO.getAllReportedIssuesByUser(reporter);
			}
			User assignee = null;
			if (assigneeEmail != null && assigneeEmail.length() > 0) {
				assignee = UserDAO.getUserById(UserDAO.getUserIdByEmail(assigneeEmail));
				byAssignee = IssueDAO.getAllIssuesAssignedTo(assignee);
			}

			result.addAll(byState);
			result.addAll(byProject);
			result.addAll(bySprint);
			result.addAll(byReporter);
			result.addAll(byAssignee);

		} catch (Exception e) {
			throw new IssueDAOException(e.getMessage());
		}
		return result;
	}

	public static String getAllIssuesFilteredByJSON(String issueState, String projectName, String sprintName,
			String reporterEmail, String assigneeEmail, String companyName) throws IssueDAOException {

		
		
		String JSON = "";
		try {
			Company company = CompanyDAO.getCompanyById(CompanyDAO.getIdOfCompanyWithName(companyName));
			Set<Project> companyProjects = ProjectDAO.getAllProjectsByCompany(company);
			Set<Issue> companyIssues = new HashSet<Issue>();
			for (Project project : companyProjects) {
				companyIssues.addAll(IssueDAO.getAllIssuesByProject(project));
			}

			Set<Issue> result = new HashSet<Issue>();
			Set<String> resultJSON = new HashSet<String>();

			Set<Issue> byState = companyIssues;
			Set<Issue> byProject = companyIssues;
			Set<Issue> bySprint = companyIssues;
			Set<Issue> byReporter = companyIssues;
			Set<Issue> byAssignee = companyIssues;

			IssueState state = null;
			if (issueState != null && issueState.length() > 0) {
				issueState = issueState.replace(' ', '_');
				state = IssueState.getIssueStateFromString(issueState);
				byState = IssueDAO.getAllIssuesByState(state);
			}
			Project project = null;
			if (projectName != null && projectName.length() > 0) {
				project = ProjectDAO.getProjectById(ProjectDAO.getProjectIdByName(projectName));
				byProject = IssueDAO.getAllIssuesByProject(project);
			}
			Sprint sprint = null;
			if (sprintName != null && sprintName.length() > 0) {
				sprint = SprintDAO.getSprintById(SprintDAO.getSprintIdByName(sprintName));
				bySprint = IssueDAO.getAllIssuesBySprint(sprint);
			}
			User reporter = null;
			if (reporterEmail != null && reporterEmail.length() > 0) {
				reporter = UserDAO.getUserById(UserDAO.getUserIdByEmail(reporterEmail));
				byReporter = IssueDAO.getAllReportedIssuesByUser(reporter);
			}
			User assignee = null;
			if (assigneeEmail != null && assigneeEmail.length() > 0) {
				assignee = UserDAO.getUserById(UserDAO.getUserIdByEmail(assigneeEmail));
				byAssignee = IssueDAO.getAllIssuesAssignedTo(assignee);
			}
			
			companyIssues.retainAll(byState);
			companyIssues.retainAll(byProject);
			companyIssues.retainAll(bySprint);
			companyIssues.retainAll(byReporter);
			companyIssues.retainAll(byAssignee);

			result = companyIssues;

			Gson jsonMaker = new Gson();

			for (Issue issue : result) {
				String issueJSON = jsonMaker.toJson(issue);
				resultJSON.add(issueJSON);
			}

			JSON = jsonMaker.toJson(result);
		} catch (Exception e) {
			throw new IssueDAOException(e.getMessage());
		}
		return JSON;
	}

	public static Set<Issue> getAllIssuesByState(IssueState state) throws IssueDAOException {
		if (state == null) {
			throw new IssueDAOException("issue state cannot be null");
		}
		

		Set<Issue> result = new HashSet<Issue>();

		Connection connection = DBConnection.getInstance().getConnection();
		int stateId = state.ordinal() + 1;
		try {
			PreparedStatement ps = connection.prepareStatement(FIND_ISSUES_BY_STATE_SQL);
			ps.setInt(1, stateId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int issueId = rs.getInt(1);
				result.add(IssueDAO.getIssueById(issueId));
			}
		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}

		return result;
	}
}
