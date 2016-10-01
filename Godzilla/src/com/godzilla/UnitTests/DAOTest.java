package com.godzilla.UnitTests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Test;

import com.godzilla.DBConnection.DBConnection;
import com.godzilla.model.Issue;
import com.godzilla.model.Project;
import com.godzilla.model.Story;
import com.godzilla.model.User;
import com.godzilla.model.DAO.IssueDAO;
import com.godzilla.model.DAO.UserDAO;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.ProjectException;
import com.godzilla.model.exceptions.UserException;

public class DAOTest {

	public static void appendExceptions(StringBuilder builder,Throwable ex){
		
		builder.append(ex.getMessage() + "\n");
		
		if(ex.getCause() == null){
			return;
		}

		Throwable cause = ex.getCause();
		
		appendExceptions(builder, cause);
		
	}
	
	
	@Test
	public void test() throws Exception {
		Connection connection = DBConnection.getInstance().getConnection();
		
		PreparedStatement ps = connection.prepareStatement("select Date_created from issues;");
		ResultSet rs = ps.executeQuery();
		
		//sql dateTime -> java localDateTime
		if (rs.next()) {
			Date rDate = rs.getDate(1);
			Time rTime = rs.getTime(1);
			System.out.println(rDate.toString());
			System.out.println(rTime.toString());
			
			LocalDate date = rDate.toLocalDate();
			LocalTime time = rTime.toLocalTime();
			
			LocalDateTime dateTime = LocalDateTime.of(date, time);
			
			System.out.println(date.toString() + time.toString());
			System.out.println(dateTime.toString());
		}
	}

}
