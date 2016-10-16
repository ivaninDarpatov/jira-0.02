package com.godzilla.model;

import java.time.LocalDateTime;
import com.godzilla.model.enums.IssuePriority;
import com.godzilla.model.enums.IssueState;
import com.godzilla.model.exceptions.IssueException;

public class Issue {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Issue other = (Issue) obj;
		if (id != other.id)
			return false;
		return true;
	}

	private int id;
	private String type;
	private String name;
	private String summary;
	private String description;
	private IssuePriority priority;
	private IssueState state;
	private LocalDateTime dateCreated;
	private LocalDateTime dateLastModified;
	public Issue(String summary,String issueType) throws IssueException {
		this.setSummary(summary);
		this.setPriority(IssuePriority.MEDIUM);
		this.setState(IssueState.TO_DO);
		this.setType(issueType);
		this.initializeDates();
	}

	private void initializeDates() {
		this.dateCreated = LocalDateTime.now();
		this.dateLastModified = this.dateCreated;
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
	
	public void setType(String type) throws IssueException {
		if (type != null) {
			this.type = type;
		} else {
			throw new IssueException("invalid issue type");
		}
	}
	
	public void setName(String name) throws IssueException{
		if (name != null && name.length() > 0) {
			this.name = name;
		} else {
			throw new IssueException("Issue's name cannot be null");
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
	
	public String getType() {
		return this.type;
	}
	
	public String getName(){
		return this.name;
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
