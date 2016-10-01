package com.godzilla.model;

import java.util.HashSet;
import java.util.Set;

import com.godzilla.model.exceptions.ProjectException;

public class Project {
	private int id;
	private String name;
	private Set<Issue> issues;
	private Set<Sprint> sprints;

	public Project(String name) throws ProjectException {
		this.issues = new HashSet<>();
		this.sprints = new HashSet<>();
		this.setName(name);
	}

	private void setName(String name) throws ProjectException {
		if (name != null && !name.trim().equals("")) {
			this.name = name;
		} else {
			throw new ProjectException("project name cannot be null");
		}
	}

	public void addIssue(Issue issueToAdd) throws ProjectException {
		if (issueToAdd != null) {
			issues.add(issueToAdd);
		} else {
			throw new ProjectException("you cannot add an issue with value null to your project");
		}
	}

	public void addSprint(Sprint sprintToAdd) throws ProjectException {
		if (sprintToAdd != null) {
			sprints.add(sprintToAdd);
		} else {
			throw new ProjectException("you cannot add a sprint with value null to your project");
		}
	}

	public void setId(int id) throws ProjectException {
		if (id > 0) {
			this.id = id;
		} else {
			throw new ProjectException("project's id cannot be 0");
		}
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + ", issues=" + issues + ", sprints=" + sprints + "]";
	}
}
