package com.godzilla.UnitTests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.junit.Test;

import com.godzilla.DBConnection.DBConnection;
import com.godzilla.model.Issue;
import com.godzilla.model.Project;
import com.godzilla.model.Story;
import com.godzilla.model.User;
import com.godzilla.model.DAO.IssueDAO;
import com.godzilla.model.DAO.StaticValuesInitializerDAO;
import com.godzilla.model.DAO.UserDAO;
import com.godzilla.model.exceptions.IssueDAOException;
import com.godzilla.model.exceptions.ProjectException;
import com.godzilla.model.exceptions.UserException;

@SuppressWarnings("all")
public class DAOTest {
	
	public static void printExceptionMessages(Throwable ex) {
		StringBuilder builder = new StringBuilder();
		appendExceptions(builder, ex);
		System.out.println(builder.toString());
	}

	public static void appendExceptions(StringBuilder builder,Throwable ex){
		
		builder.append(ex.getMessage() + "\n");
		
		if(ex.getCause() == null){
			return;
		}

		Throwable cause = ex.getCause();
		
		appendExceptions(builder, cause);
		
	}
	
	
	@Test
	public void test() {
		LocalDateTime dateTime = LocalDateTime.now();
		LocalDate date = dateTime.toLocalDate();
		LocalTime time = dateTime.toLocalTime();
		String dateTimeString = date.toString() + " " + time.toString();
		System.out.println(dateTimeString);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS:nnn");
		LocalDateTime dateTime1 = LocalDateTime.parse(dateTimeString, formatter);
		System.out.println(dateTime1.toString());
	}

}
