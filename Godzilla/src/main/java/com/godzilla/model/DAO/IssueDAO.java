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
import com.godzilla.model.exceptions.EpicException;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.IssueException;

public class IssueDAO {
	private static final String ASSIGN_ISSUE_SQL = "UPDATE issues SET assignee_id = ? WHERE issue_id = ?;";
	private static final String GET_SPRINT_ID_FOR_ISSUE_SQL = "SELECT sprint_id FROM issues WHERE issue_id = ?;";
	private static final String ADD_ISSUE_TO_EPIC_SQL = "UPDATE issues SET epic_id = ? WHERE issue_id = ?;";
	private static final String ADD_ISSUE_TO_SPRINT_SQL = "UPDATE issues SET sprint_id = ? WHERE issue_id = ?;";
	private static final String HAND_ISSUE_TO_ADMIN_SQL = "UPDATE issues SET reporter_id = ? WHERE issue_id = ?;";
	private static final String FIND_COMPANY_FOR_PROJECT_SQL = "SELECT c.* FROM companies c JOIN projects p ON (c.company_id = p.company_id) WHERE p.project_id = ?;";
	private static final String FIND_PROJECT_FOR_ISSUE_SQL = "SELECT p.* FROM projects p JOIN issues i ON (i.project_id = p.project_id) WHERE i.issue_id = ?;";
	private static final String FIND_ADMIN_FOR_COMPANY_SQL = "SELECT * FROM users WHERE permissions_id = 1 AND company_id = ?;";
	private static final String GET_ISSUES_ASSIGNED_TO_USER_SQL = "SELECT issue_id, summary, description, state_id, priority, date_created, date_last_modified FROM issues WHERE assignee_id = ?;";
	private static final String UNASSIGN_ISSUE_SQL = "UPDATE issues SET assignee_id = null WHERE issue_id = ?;";
	private static final String REMOVE_ISSUE_FROM_SPRINT_SQL = "UPDATE issues SET sprint_id = null WHERE issue_id = ?;";
	private static final String ADD_BUG_SQL = "INSERT INTO bugs VALUES(? , null);";
	private static final String ADD_TASK_SQL = "INSERT INTO tasks VALUES(?, null);";
	private static final String ADD_EPIC_SQL = "INSERT INTO epics VALUES(? , ?);";
	private static final String ADD_STORY_SQL = "INSERT INTO stories VALUES(? , null);";
	private static final String FIND_ISSUES_BY_EPIC_ID_SQL = "SELECT issue_id FROM issues WHERE epic_id = ?;";
	private static final String FIND_ISSUES_BY_PROJECT_ID_SQL = "SELECT issue_id FROM issues WHERE project_id = ?;";
	private static final String IS_BUG_SQL = "SELECT bug_id FROM bugs WHERE bug_id = ?;";
	private static final String IS_TASK_SQL = "SELECT task_id FROM tasks WHERE task_id = ?;";
	private static final String IS_STORY_SQL = "SELECT story_id FROM stories WHERE story_id = ?;";
	private static final String IS_EPIC_SQL = "SELECT epic_id FROM epics WHERE epic_id = ?;";
	private static final String REMOVE_ISSUE_SQL = "DELETE FROM issues WHERE issue_id = ?;";
	private static final String CREATE_ISSUE_SQL = "INSERT INTO issues VALUES (null, ?, ?, ?, ?, ?, ?, ?, 'someDate', 'someDate', null, null);";
	private static final String FIND_ISSUE_BY_REPORTER_SQL = "SELECT issue_id, summary, description, state_id, priority, date_created, date_last_modified FROM issues WHERE reporter_id = ?;";
	private static final String FIND_ISSUE_BY_ID_SQL = "SELECT * FROM issues WHERE issue_id = ?;";
	private static final String FIND_ISSUES_BY_SPRINT_SQL = "SELECT * FROM issues WHERE sprint_id = ?;";

	public static void createIssue(Issue toCreate, Project project, User reporter) throws IssueDAOException {
		if (toCreate == null || project == null || reporter == null) {
			throw new IssueDAOException("cannot create issue, required: project, reporter and issue model");
		}

		String issueType = Issue.getIssueType(toCreate);

		Connection connection = DBConnection.getInstance().getConnection();
		int issueId;

		String summary = toCreate.getSummary();
		String description = toCreate.getDescription();
		int priorityId = toCreate.getPriority().getValue();
		int stateId = toCreate.getState().getValue();
		int projectId = project.getId();
		int reporterId = reporter.getId();
		int assigneeId = reporterId;
//		String dateTimeCreated = toCreate.getDateCreated().toLocalDate().toString() + 
//								" " + 
//								toCreate.getDateCreated().toLocalTime().toString();
//		String dateTimeLastModified = toCreate.getDateLastModified().toLocalDate().toString() + 
//									" " + 
//									toCreate.getDateLastModified().toLocalTime().toString();

		try {
			connection.setAutoCommit(false);
			PreparedStatement insertIntoIssues = connection.prepareStatement(CREATE_ISSUE_SQL, Statement.RETURN_GENERATED_KEYS);
			insertIntoIssues.setString(1, summary);
			insertIntoIssues.setString(2, description);
			insertIntoIssues.setInt(3, projectId);
			insertIntoIssues.setInt(4, stateId);
			insertIntoIssues.setInt(5, priorityId);
			insertIntoIssues.setInt(6, reporterId);
			insertIntoIssues.setInt(7, assigneeId);
//			insertIntoIssues.setString(8, dateTimeCreated);
//			insertIntoIssues.setString(9, dateTimeLastModified);


			if (insertIntoIssues.executeUpdate() > 0) {
				ResultSet rs = insertIntoIssues.getGeneratedKeys();

				if (rs.next()) {
					issueId = rs.getInt(1);
					toCreate.setId(issueId);
				} else {
					throw new IssueDAOException("could not set id of an issue");
				}

			} else {
				throw new IssueDAOException("failed to create issue");
			}

			PreparedStatement insertIntoBugTaskEpicOrStory = null;

			switch (issueType) {
			case "bug":
				insertIntoBugTaskEpicOrStory = connection.prepareStatement(ADD_BUG_SQL);
				insertIntoBugTaskEpicOrStory.setInt(1, issueId);
				break;
			case "task":
				insertIntoBugTaskEpicOrStory = connection.prepareStatement(ADD_TASK_SQL);
				insertIntoBugTaskEpicOrStory.setInt(1, issueId);
				break;
			case "epic":
				insertIntoBugTaskEpicOrStory = connection.prepareStatement(ADD_EPIC_SQL);
				insertIntoBugTaskEpicOrStory.setInt(1, issueId);
				insertIntoBugTaskEpicOrStory.setString(2, ((Epic) toCreate).getName());
				PreparedStatement addIssueToEpicPS = connection.prepareStatement(ADD_ISSUE_TO_EPIC_SQL);
				addIssueToEpicPS.executeUpdate();
				break;
			case "story":
				insertIntoBugTaskEpicOrStory = connection.prepareStatement(ADD_STORY_SQL);
				insertIntoBugTaskEpicOrStory.setInt(1, issueId);
				break;
			default:
				break;
			}

			if (insertIntoBugTaskEpicOrStory.executeUpdate() < 1) {
				throw new IssueDAOException("failed to insert issue properly");
			}

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
				String summary = rs.getString("summary");
				String description = rs.getString("description");
				int priorityId = rs.getInt("priority");
				int stateId = rs.getInt("state_id");
//				LocalDateTime dateCreated = getLocalDateTimeFromString(rs.getString("date_created"));
//				LocalDateTime dateLastModified = getLocalDateTimeFromString(rs.getString("date_last_modified"));
				IssuePriority priority = IssuePriority.getTypeById(priorityId);
				IssueState state = IssueState.getTypeById(stateId);

				String type = IssueDAO.getIssueType(issueId);

				switch (type) {
				case "bug":
					issue = new Bug(summary);
					break;
				case "task":
					issue = new Task(summary);
					break;
				case "story":
					issue = new Story(summary);
					break;
				case "epic":
					issue = new Epic(summary,"epic");
					issue.setId(issueId);
					Set<Issue> issues = IssueDAO.getAllIssuesByEpic((Epic) issue);

					for (Issue entry : issues) {
						((Epic) issue).addIssue(entry);
					}

					break;
				}

				if (issue == null) {
					throw new IssueDAOException("couldn't find issue");
				}


				issue.setDescription(description);
				issue.setPriority(priority);
				issue.setState(state);
//				issue.setDateCreated(dateCreated);
//				issue.setDateLastModified(dateLastModified);
				
				issue.setId(issueId);
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
			String table = IssueDAO.getIssueType(issueId).equals("story") ? "storie" : IssueDAO.getIssueType(issueId);

			final String DELETE_BUG_TASK_STORY_SQL = "DELETE FROM " + table + "s" + " WHERE " + issueType + "_id = ?;";
			PreparedStatement ps = connection.prepareStatement(DELETE_BUG_TASK_STORY_SQL);
			ps.setInt(1, issueId);

			if (ps.executeUpdate() < 1) {
				throw new IssueDAOException("failed to remove " + issueType);
			}

			ps = connection.prepareStatement(REMOVE_ISSUE_SQL);
			ps.setInt(1, issueId);

			if (ps.executeUpdate() < 1) {
				throw new IssueDAOException("failed to remove issue");
			}

			connection.commit();

		} catch (SQLException e) {
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

	private static String getIssueType(int issueId) throws IssueDAOException {
		if (issueId < 1) {
			throw new IssueDAOException("cannot find issue with that id");
		}

		if (IssueDAO.isBug(issueId)) {
			return "bug";
		}

		if (IssueDAO.isTask(issueId)) {
			return "task";
		}

		if (IssueDAO.isStory(issueId)) {
			return "story";
		}

		if (IssueDAO.isEpic(issueId)) {
			return "epic";
		}

		return null;
	}

	private static boolean isBug(int issueId) throws IssueDAOException {
		if (issueId < 1) {
			throw new IssueDAOException("cannot find issue with that id");
		}
		Connection connection = DBConnection.getInstance().getConnection();

		try {
			PreparedStatement ps = connection.prepareStatement(IS_BUG_SQL);
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

	private static boolean isTask(int issueId) throws IssueDAOException {
		if (issueId < 1) {
			throw new IssueDAOException("cannot find issue with that id");
		}
		Connection connection = DBConnection.getInstance().getConnection();

		try {
			PreparedStatement ps = connection.prepareStatement(IS_TASK_SQL);
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

	private static boolean isStory(int issueId) throws IssueDAOException {
		if (issueId < 1) {
			throw new IssueDAOException("cannot find issue with that id");
		}
		Connection connection = DBConnection.getInstance().getConnection();

		try {
			PreparedStatement ps = connection.prepareStatement(IS_STORY_SQL);
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
		if(!isIssueInSprint(issueToSetFree)){
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
	
	private static boolean isIssueInSprint(Issue issue) throws IssueDAOException{
		if(issue == null){
			throw new IssueDAOException("issue cannot be null");
		}
		
		Connection connection = DBConnection.getInstance().getConnection();
		
		PreparedStatement getIssueSprint;
		try {
			getIssueSprint = connection.prepareStatement(GET_SPRINT_ID_FOR_ISSUE_SQL);
			getIssueSprint.setInt(1, issue.getId());
			
			ResultSet rs = getIssueSprint.executeQuery();
			
			if(rs.next()){
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
			
			if(addIssueToEpicPS.executeUpdate() < 1){
				throw new IssueDAOException("Failed to add issue in epic");
			}
			
		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}
		
	}
	
	public static void addIssueToSprint(Issue issue,Sprint sprint) throws IssueDAOException{
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
			
			if(addIssueToEpicPS.executeUpdate() < 1){
				throw new IssueDAOException("Failed to add issue in epic");
			}
			
		} catch (SQLException e) {
			throw new IssueDAOException(e.getMessage());
		}
		
	}
}
