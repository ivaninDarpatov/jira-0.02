package com.godzilla.model;

import java.util.HashSet;
import java.util.Set;

import com.godzilla.model.enums.Permissions;
import com.godzilla.model.exceptions.*;

public class User {
	private int id;
	private String email;
	private String password;
	private Permissions permissions;
	private String company;
	private Set<Issue> issuesAssignedTo;
	private Set<Issue> issuesReportedBy;

	public User(String email, String password) throws UserException {
		try {
			this.setEmail(email);
		} catch (EmailException e) {
			throw new UserException("Invalid email", e);
		}
		try {
			this.setPassword(password);
		} catch (PasswordException e) {
			throw new UserException("Invalid password", e);
		}
		try {
			this.setPermissions(Permissions.USER);
		} catch (PermissionException e) {
			throw new UserException("Invalid permissions", e);
		}

		issuesAssignedTo = new HashSet<Issue>();
		issuesReportedBy = new HashSet<Issue>();
	}

	public User(String email, String password, String company) throws UserException {
		this(email, password);
		try {
			this.setCompany(company);
		} catch (CompanyException e) {
			throw new UserException("Invalid Company", e);
		}

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
			if ((toCheck.charAt(index) >= 'A' && toCheck.charAt(index) <= 'Z')
					|| (toCheck.charAt(index) >= 'a' && toCheck.charAt(index) <= 'z')) {
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

	public void addIssuesReportedByMe(Issue issueToAdd) throws UserException {
		if (issueToAdd != null) {
			this.issuesReportedBy.add(issueToAdd);
		} else {
			throw new UserException("failed to add reported issue");
		}
	}
	
	public void addIssuesAssignedToMe(Issue issueToAdd) throws UserException {
		if (issueToAdd != null) {
			this.issuesAssignedTo.add(issueToAdd);
		} else {
			throw new UserException("failed to add assigned issue");
		}
	}

	private void setEmail(String email) throws EmailException {
		if (email != null && email.length() > 0) {
			if (User.validateEmail(email)) {
				this.email = email;
			} else {
				throw new EmailException("wrong email format");
			}
		} else {
			throw new EmailException("email cannot be null");
		}
	}

	private void setPassword(String password) throws PasswordException {
		if (password != null && password.length() > 0) {
			if (User.validatePassword(password)) {
				this.password = password;
			} else {
				throw new PasswordException("wrong password format");
			}
		} else {
			throw new PasswordException("password cannot be null");
		}
	}

	public void setPermissions(Permissions permissions) throws PermissionException {
		if (permissions != null) {
			this.permissions = permissions;
		} else {
			throw new PermissionException("permision cannot be null");
		}
	}

	private void setCompany(String company) throws CompanyException {
		if (company != null && company.length() > 0) {
			this.company = company;
		} else {
			throw new CompanyException("company's name cannot be null");
		}
	}

	public void setId(int id) throws UserException {
		if (id > 0) {
			this.id = id;
		} else {
			throw new UserException("user's id cannot be 0");
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
