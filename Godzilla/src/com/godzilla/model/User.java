package com.godzilla.model;

import java.util.HashSet;
import java.util.Set;

import com.godzilla.model.enums.Permissions;

public class User {
	int id;
	String email;
	String password;
	Permissions permissions;
	String company;
	Set<Issue> issuesAssignedTo;
	Set<Issue> issuesReportedBy;
	
	public User(String email, String password, String company) {
		this.setEmail(email);
		this.setPassword(password);
		this.setPermissions(Permissions.USER);
		this.setCompany(company);
		
		issuesAssignedTo = new HashSet<Issue>();
		issuesReportedBy = new HashSet<Issue>();
	}
	
	private static boolean validateEmail(String email) {		 
		if (!email.contains("@")) {
			return false;
		}
		
		int atCount = 0;
		for (int index = 0; index < email.length(); index++) {
			if (email.charAt(index) == '@') {
				atCount++;
			}
			if (atCount > 1) {
				return false;
			}
		}
			
		int atIndex = email.indexOf('@');
		if (!email.substring(atIndex + 1).contains(".")) {
			return false;
		}
		
		return true;
	}
	
	private static boolean containsCharacters(String toCheck) {
		for (int index = 0; index < toCheck.length(); index++) {
			if ((toCheck.charAt(index) >= 'A' && toCheck.charAt(index) <= 'Z') ||
				(toCheck.charAt(index) >= 'a' && toCheck.charAt(index) <= 'z')) {
				return true;
			}
		}
		
		return false;
	}
	
	private static boolean containsDigits(String toCheck) {
		for (int index = 0; index < toCheck.length(); index++) {
			if (toCheck.charAt(index) >= '0' && toCheck.charAt(index) <= '9') {
				return true;
			}
		}
		
		return false;
	}
	
	private static boolean validatePassword(String password) {
		if (password == null) {
			return false;
		}
		
		if (password.length() < 8) {
			return false;
		}
		
		if (!User.containsCharacters(password) || !User.containsDigits(password)) {
			return false;
		}
		
		return true;
	}
	
	private void setEmail(String email) {
		if (email != null && email.length() > 0) {
			if (User.validateEmail(email)) {
				this.email = email;
			}
		}
	}
	
	private void setPassword(String password) {
		if (password != null && password.length() > 0) {
			if (User.validatePassword(password)) {
				this.password = password;
			}
		}
	}
	
	public void setPermissions(Permissions permissions) {
		if (permissions != null) {
			this.permissions = permissions;
		}
	}
	
	private void setCompany(String company) {
		if (company != null && company.length() > 0) {
			this.company = company;
		}
	}
	
	public void setId(int id) {
		if (id > 0) {
			this.id = id;
		}
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getCompany() {
		return this.company;
	}
}
