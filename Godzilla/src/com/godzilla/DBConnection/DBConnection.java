package com.godzilla.DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBConnection {

private static DBConnection instance;
	
	private Connection connection;

	private static final String DB_PORT = "3306";
	private static final String DB_HOST = "localhost";

	
	
	private DBConnection() throws Exception {
//		Scanner sc = new Scanner(System.in);
//		System.out.print("Data Base Username: ");
		String userName = "root";
//		System.out.print("Data Base password: ");
		String password = "aichetolub6747";
//		System.out.print("Data Base schema: ");
		String schema = "godziila";
//		sc.close();
		
		Class.forName("com.mysql.jdbc.Driver");
		this.connection = DriverManager.getConnection(
				"jdbc:mysql://" + DB_HOST + ":" + 
				DB_PORT + "/" + 
				schema + "?autoReconnect=true&useSSL=false", 
				userName, password);
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
		return connection;
	}
	
	public static void main(String[] args) {
		Connection connection = DBConnection.getInstance().getConnection();
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery("SELECT * from companies;");
			
			while(rs.next()){
				System.out.println(rs.getInt(1));
				System.out.println(rs.getString(2));
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
