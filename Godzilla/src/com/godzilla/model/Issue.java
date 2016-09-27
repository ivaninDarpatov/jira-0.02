package com.godzilla.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import com.godzilla.model.enums.IssueLinkType;
import com.godzilla.model.enums.IssueState;

public abstract class Issue {
	int id;
	String summary;
	String description;
	IssueState state;
	LocalDateTime dateCreated;
	LocalDateTime dateLastModified;
	Map<IssueLinkType, Set<Issue>> linkedIssues;
	
	public Issue(String summary) {
		this.setSummary(summary);
		this.initializeDates();
	}
	
	public Issue(String summary, String description) {
		this(summary);
		this.setDescription(description);
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
	
	private void setDescription(String description) {
		if (description != null && description.length() > 0) {
			this.description = description;
		}
	}
}
