package com.godzilla.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import com.godzilla.model.enums.IssueLinkType;
import com.godzilla.model.enums.IssuePriority;
import com.godzilla.model.enums.IssueState;

public abstract class Issue {
	int id;
	String summary;
	String description;
	IssuePriority priority;
	IssueState state;
	LocalDateTime dateCreated;
	LocalDateTime dateLastModified;
	Map<IssueLinkType, Set<Issue>> linkedIssues;
	
	public Issue(String summary) {
		this.setSummary(summary);
		this.setPriority(IssuePriority.MEDIUM);
		this.setState(IssueState.TO_DO);
		this.initializeDates();
	}
	
	private void initializeDates() {
		this.dateCreated = LocalDateTime.now();
		this.dateLastModified = this.dateCreated;
	}
	
	private void setSummary(String summary) {
		if (summary != null && summary.length() > 0) {
			this.summary = summary;
		}
	}
	
	public void setDescription(String description) {
		if (description != null && description.length() > 0) {
			this.description = description;
		}
	}
	
	public void setPriority(IssuePriority priority) {
		if (priority != null) {
			this.priority = priority;
		}
	}
	
	public void setState(IssueState state) {
		if (state != null) {
			this.state = state;
		}
	}

	public String getSummary() {
		return this.summary;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public IssuePriority getPriority() {
		return this.priority;
	}
	
	public IssueState getState() {
		return this.state;
	}
	
	public LocalDateTime getDateCreated() {
		return this.dateCreated;
	}
	
	public LocalDateTime getDateLastModified() {
		return this.dateLastModified;
	}
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
