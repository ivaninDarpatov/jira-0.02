package com.godzilla.model;

import java.time.LocalDateTime;
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

	public Sprint(String name) throws SprintException {
		this.issues = new HashSet<Issue>();
		this.setName(name);
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

	public void setId(int id) throws SprintException {
		if (id > 0) {
			this.id = id;
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

	public int getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return "Sprint [id=" + id + ", name=" + name + ", sprintGoal=" + sprintGoal + ", startingDate=" + startingDate
				+ ", endDate=" + endDate + "]";
	}
}
