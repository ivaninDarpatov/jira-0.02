package com.godzilla.model;

import java.util.HashSet;
import java.util.Set;

import com.godzilla.model.exceptions.CompanyException;

public class Company {
	private int id;
	private String name;
	private Set<Project> projects;
	private Set<User> users;

	public Company(String name) throws CompanyException {
		this.projects = new HashSet<>();
		this.users = new HashSet<>();
		try {
			this.setName(name);
		} catch (CompanyException e) {
			throw new CompanyException("Invalid company name ", e);
		}
	}

	public void addNewProject(Project newProject) throws CompanyException {
		if (newProject != null) {
			this.projects.add(newProject);
		} else {
			throw new CompanyException("null value for project you're trying to add");
		}
	}

	public void addNewUser(User newUser) throws CompanyException {
		if (newUser != null) {
			this.users.add(newUser);
		} else {
			throw new CompanyException("null value for user you're trying to add");
		}
	}

	private void setName(String name) throws CompanyException {
		if (name != null && !name.trim().equals("")) {
			this.name = name;
		} else {
			throw new CompanyException("name cannot be null");
		}
	}

	public void setId(int id) throws CompanyException {
		if (id != 0) {
			this.id = id;
		} else {
			throw new CompanyException("company id cannot be 0");
		}
	}

	public String getName() {
		return this.name;
	}

	public int getId() {
		return id;
	}
}
