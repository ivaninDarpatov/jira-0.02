package com.godzilla.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.godzilla.model.enums.IssueLinkType;
import com.godzilla.model.enums.IssuePriority;
import com.godzilla.model.enums.IssueState;
import com.godzilla.model.exceptions.IssueException;

public abstract class Issue {
	private int id;
	private String summary;
	private String description;
	private IssuePriority priority;
	private IssueState state;
	private LocalDateTime dateCreated;
	private LocalDateTime dateLastModified;
	private Map<IssueLinkType, Set<Issue>> linkedIssues;

	public Issue(String summary) throws IssueException {
		this.setSummary(summary);
		this.setPriority(IssuePriority.MEDIUM);
		this.setState(IssueState.TO_DO);
		this.initializeDates();
		this.initializeLinkedIssues();
	}

	private void initializeDates() {
		this.dateCreated = LocalDateTime.now();
		this.dateLastModified = this.dateCreated;
	}
	
	private void initializeLinkedIssues() {
		this.linkedIssues = new HashMap<IssueLinkType, Set<Issue>>();
		
		for (IssueLinkType linkType : IssueLinkType.values()) {
			Set<Issue> issuesSet = new HashSet<Issue>();
			
			this.linkedIssues.put(linkType, issuesSet);
		}
	}
	
	public void linkToIssue(IssueLinkType linkType, Issue linkedIssue) {
		Set<Issue> linkedIssues = this.linkedIssues.get(linkType);
		
		if (linkedIssues.contains(linkedIssue)) {
			return;
		}
		
		linkedIssues.add(linkedIssue);
		
		IssueLinkType oppositeLinkType = IssueLinkType.getOppositeLinkType(linkType);
		linkedIssue.linkToIssue(oppositeLinkType, this);
	}

	public void setSummary(String summary) throws IssueException {
		if (summary != null && summary.length() > 0) {
			this.summary = summary;
		} else {
			throw new IssueException("issue's summary cannot be null");
		}
	}

	public void setDescription(String description) {
		if (description != null) {
			this.description = description;
		} else {
			this.description = "";
		}
	}

	public void setPriority(IssuePriority priority) throws IssueException {
		if (priority != null) {
			this.priority = priority;
		} else {
			throw new IssueException("issue priority cannot be null");
		}
	}

	public void setState(IssueState state) throws IssueException {
		if (state != null) {
			this.state = state;
		} else {
			throw new IssueException("issue state cannot be null");
		}
	}
	
	public void setDateCreated(LocalDateTime dateTime) throws IssueException {
		if (dateTime != null) {
			this.dateCreated = dateTime;
		} else {
			throw new IssueException("date created cannot be null");
		}
	}
	
	public void setDateLastModified(LocalDateTime dateTime) throws IssueException {
		if (dateTime != null) {
			this.dateLastModified = dateTime;
		} else {
			throw new IssueException("date last modified cannot be null");
		}
	}

	public void setId(int issueId) throws IssueException {
		if (issueId > 0) {
			this.id = issueId;
		} else {
			throw new IssueException("issue's id cannot be 0");
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

	public static String getIssueType(Issue issue) {
		if (issue instanceof Task) {
			return "task";
		}
		if (issue instanceof Bug) {
			return "bug";
		}
		if (issue instanceof Epic) {
			return "epic";
		}
		if (issue instanceof Story) {
			return "story";
		}

		return null;
	}

	@Override
	public String toString() {
		return "Issue [" + 
				"\n\tid=" + id + 
				",\n\t summary=" + summary + 
				",\n\t description=" + description + 
				",\n\t priority=" + priority + 
				",\n\t state=" + state + 
				",\n\t dateCreated=" + dateCreated + 
				",\n\t dateLastModified=" + dateLastModified + 
				"\n]\n" ;
	}
}