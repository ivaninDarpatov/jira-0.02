package com.godzilla.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.godzilla.model.DBConnection.DBConnection;
import com.godzilla.model.enums.IssueState;
import com.godzilla.model.enums.Permissions;

public class StaticValuesInitializerDAO {
	private static final String INITIALIZE_PERMISSIONS_SQL = "INSERT INTO permissions VALUES (?, ?);";
	private static final String INITIALIZE_STATES_SQL = "INSERT INTO states VALUES (?, ?);";

	public static void initializeStaticValues() throws SQLException {
		try {
			StaticValuesInitializerDAO.initializeIssueStates();
			StaticValuesInitializerDAO.initializeUserPermissions();
		} catch (SQLException e) {
			throw new SQLException("failed to initialize static values", e);
		}
	}

	private static void initializeUserPermissions() throws SQLException {
		Connection connection = DBConnection.getInstance().getConnection();

		int permissionsId;
		String permissionsValue;


		for (Permissions permissions : Permissions.values()) {
			permissionsId = permissions.ordinal() + 1;
			permissionsValue = permissions.name();

			PreparedStatement ps = connection.prepareStatement(INITIALIZE_PERMISSIONS_SQL);
			
			ps.setInt(1, permissionsId);
			ps.setString(2, permissionsValue);

			ps.executeUpdate();
		}

	}

	private static void initializeIssueStates() throws SQLException {
		Connection connection = DBConnection.getInstance().getConnection();

		int stateId;
		String stateValue;


		for (IssueState state : IssueState.values()) {
			stateId = state.ordinal() + 1;
			stateValue = state.name();

			PreparedStatement ps = connection.prepareStatement(INITIALIZE_STATES_SQL);
			
			ps.setInt(1, stateId);
			ps.setString(2, stateValue);

			ps.executeUpdate();
		}
	}
}
