package com.godzilla.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.godzilla.model.exceptions.ProjectException;

public class Project {
	private int id;
	private String name;
	private Set<Issue> issues;
	private Set<Sprint> sprints;

	public Project(String name) throws ProjectException {
		this.setName(name);
		this.issues = new HashSet<>();
		this.sprints = new HashSet<>();
	}

	public void addIssue(Issue issueToAdd) throws ProjectException {
		if (issueToAdd != null) {
			issues.add(issueToAdd);
		} else {
			throw new ProjectException("you cannot add an issue with value null to your project");
		}
	}

	private void setName(String name) throws ProjectException {
		if (name != null && !name.trim().equals("")) {
			this.name = name;
		} else {
			throw new ProjectException("project name cannot be null");
		}
	}

	public void addSprint(Sprint sprintToAdd) throws ProjectException {
		if (sprintToAdd != null) {
			sprints.add(sprintToAdd);
		} else {
			throw new ProjectException("you cannot add a sprint with value null to your project");
		}
	}

	public void setId(int projectId) throws ProjectException {
		if (projectId > 0) {
			this.id = projectId;
		} else {
			throw new ProjectException("project's id cannot be 0");
		}
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}
	
	public Set<Issue> getIssues() {
		return Collections.unmodifiableSet(this.issues);
	}
	
	public Set<Sprint> getSprints() {
		return Collections.unmodifiableSet(this.sprints);
	}

	@Override
	public String toString() {
		return "Project [" +
				"\n\tid=" + id + 
				",\n\t name=" + name + 
				",\n\t issues=" + issues + 
				",\n\t sprints=" + sprints + 
				"\n]\n";
	}
}
