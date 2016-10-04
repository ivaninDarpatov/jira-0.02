package com.godzilla.model.DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	private static DBConnection instance;

	private Connection connection;

	private static final String DB_ACCESS = "jdbc:mysql://";
	private static final String DB_HOST = "localhost";
	private static final String DB_PORT = "3306";
	private static final String DB_SCHEMA = "godzilla";
	private static final String DB_PARAMETERS = "?autoReconnect=true&useSSL=false";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "ittstudents";
	private static final String DB_PATH = DB_ACCESS + DB_HOST + ":" + DB_PORT + "/" + DB_SCHEMA + DB_PARAMETERS;

	private DBConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		this.connection = DriverManager.getConnection(DB_PATH, DB_USERNAME, DB_PASSWORD);
	}

	public static DBConnection getInstance() {
		if (instance == null) {
			try {
				instance = new DBConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return instance;
	}

	public Connection getConnection() {
		return this.connection;
	}
}
