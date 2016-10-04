package com.godzilla.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.godzilla.model.exceptions.CompanyException;

public class Company {
	private int id;
	private String name;
	private Set<Project> projects;
	private Set<User> users;

	public Company(String name) throws CompanyException {
		try {
			this.setName(name);
		} catch (CompanyException e) {
			throw new CompanyException("Invalid company name", e);
		}
		this.projects = new HashSet<>();
		this.users = new HashSet<>();
	}

	public void addProject(Project projectToAdd) throws CompanyException {
		if (projectToAdd != null) {
			this.projects.add(projectToAdd);
		} else {
			throw new CompanyException("null value for project you're trying to add");
		}
	}

	public void addUser(User userToAdd) throws CompanyException {
		if (userToAdd != null) {
			this.users.add(userToAdd);
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

	public void setId(int companyId) throws CompanyException {
		if (companyId != 0) {
			this.id = companyId;
		} else {
			throw new CompanyException("company id cannot be 0");
		}
	}

	public String getName() {
		return this.name;
	}

	public int getId() {
		return this.id;
	}

	public Set<Project> getProjects() {
		return Collections.unmodifiableSet(this.projects);
	}

	public Set<User> getUsers() {
		return Collections.unmodifiableSet(this.users);
	}

	@Override
	public String toString() {
		return "Company [" +
				"\n\tid=" + id + 
				",\n\t name=" + name + 
				",\n\t projects=" + projects + 
				",\n\t users=" + users + 
				"\n]\n";
	}

}
