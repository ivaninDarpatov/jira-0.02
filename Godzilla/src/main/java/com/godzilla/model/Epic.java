package com.godzilla.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.godzilla.model.exceptions.EpicException;
import com.godzilla.model.exceptions.IssueException;

public class Epic extends Issue {
	private String epicName;
	private Set<Issue> issues;

	public Epic(String summary,String epicName) throws IssueException, EpicException {
		super(summary,"epic");
		this.setEpicName(epicName);
		this.issues = new HashSet<Issue>();
	}

	public void setEpicName(String name) throws EpicException {
		if (name != null && name.length() > 0) {
			this.epicName = name;
		} else {
			throw new EpicException("epic's name cannot be null");
		}
	}

	public void addIssue(Issue toAdd) throws EpicException {
		if (toAdd != null) {
			if (!(toAdd instanceof Epic)) {
				this.issues.add(toAdd);
			} else {
				throw new EpicException("cannot add epic to another epic");
			}
		} else {
			throw new EpicException("cannot add issue with value null");
		}
	}

	public String getName() {
		return this.epicName;
	}
	
	public Set<Issue> getIssues() {
		return Collections.unmodifiableSet(this.issues);
	}
}
