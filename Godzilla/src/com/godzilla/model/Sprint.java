package com.godzilla.model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.godzilla.model.exceptions.SprintException;

public class Sprint {
	private int id;
	private String name;
	private String sprintGoal;
	private LocalDateTime startingDate;
	private LocalDateTime endDate;
	private Set<Issue> issues;

	public Sprint(String name, String sprintGoal) throws SprintException {
		this.setName(name);
		this.setSprintGoal(sprintGoal);
		this.issues = new HashSet<Issue>();
	}
	
	public void addIssue(Issue issueToAdd) throws SprintException {
		if (issueToAdd != null) {
			this.issues.add(issueToAdd);
		} else {
			throw new SprintException("cannot add issue with a null value");
		}
	}

	public void setName(String name) throws SprintException {
		if (name != null && !name.trim().equals("")) {
			this.name = name;
		} else {
			throw new SprintException("sprint name cannot be null");
		}
	}

	public void setSprintGoal(String goal) throws SprintException {
		if (goal != null) {
			this.sprintGoal = goal;
		} else {
			throw new SprintException("sprint goal cannot be null");
		}
	}

	public void setStartingDate(LocalDateTime startingDate) throws SprintException {
		if (startingDate != null) {
			this.startingDate = startingDate;
		} else {
			throw new SprintException("starting date cannot be null");
		}
	}

	public void setEndDate(LocalDateTime endDate) throws SprintException {
		if (endDate != null) {
			this.endDate = endDate;
		} else {
			throw new SprintException("end date cannot be null");
		}
	}

	public void setId(int sprintId) throws SprintException {
		if (sprintId > 0) {
			this.id = sprintId;
		} else {
			throw new SprintException("sprint's id cannot be 0");
		}
	}

	public String getName() {
		return this.name;
	}

	public String getSpintGoal() {
		return this.sprintGoal;
	}
	
	public LocalDateTime getStartingDate() {
		return this.startingDate;
	}
	
	public LocalDateTime getEndDate() {
		return this.endDate;
	}

	public int getId() {
		return this.id;
	}
	
	public Set<Issue> getIssues() {
		return Collections.unmodifiableSet(this.issues);
	}

	@Override
	public String toString() {
		return "Sprint [" +
				"\n\tid=" + id + 
				",\n\t name=" + name + 
				",\n\t sprintGoal=" + sprintGoal + 
				",\n\t startingDate=" + startingDate + 
				",\n\t endDate=" + endDate + 
				",\n\t issues=" + issues + 
				"\n]\n";
	}

}
