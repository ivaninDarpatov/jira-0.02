package com.godzilla.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.godzilla.DBConnection.DBConnection;
import com.godzilla.model.Company;

public class CompanyDAO {

	
	private static final String SELECT_NAME_FROM_COMPANIES = "SELECT name from companies";
	private static final String INSERT_INTO_COMPANIES = "INSERT INTO companies VALUES(? , ? );";

	public static void createNewCompany(Company newCompany){
		if( isThereCompanyWithTheSameName(newCompany.getName())){
			//TODO: exception
		}
		
		Connection connection = DBConnection.getInstance().getConnection();
		try {
			PreparedStatement insertIntoCompanies = connection.prepareStatement(INSERT_INTO_COMPANIES,Statement.RETURN_GENERATED_KEYS);
			
			insertIntoCompanies.setInt(1, 0);
			insertIntoCompanies.setString(2, newCompany.getName());
			insertIntoCompanies.executeUpdate();
			
			ResultSet rs = insertIntoCompanies.getGeneratedKeys();
			
			if(rs.next()){
				newCompany.setId(rs.getInt(1));
			}else{
				//TODO: exception
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static boolean isThereCompanyWithTheSameName(String companyName){
		Connection connection = DBConnection.getInstance().getConnection();
		Statement selectStatement;
		ResultSet rs = null;
		try {
			selectStatement = connection.createStatement();
			rs = selectStatement.executeQuery(SELECT_NAME_FROM_COMPANIES);
			
			while(rs.next()){
				if( rs.getString(1).equals(companyName)){
					return true;
				}
			}
		} catch (SQLException e) {
			//TODO:
			e.printStackTrace();
		}
		return false;
	}
	
	public static int getIdOfCompanyWithName(String companyName){
		
		Connection connection = DBConnection.getInstance().getConnection();
		int id = 0;
		try {
			Statement selectCompanyWithName = connection.createStatement();
			ResultSet rs = selectCompanyWithName.executeQuery("SELECT id from companies where name = " + "'" + companyName + "'");
			
			if(rs.next()){
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
}
