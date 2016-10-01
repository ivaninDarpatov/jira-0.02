package com.godzilla.model;

import java.util.Set;

import com.godzilla.model.exceptions.EpicException;
import com.godzilla.model.exceptions.IssueException;

public class Epic extends Issue {
	private String name;
	private Set<Issue> issues;

	public Epic(String summary) throws IssueException {
		super(summary);
	}

	public void setName(String name) throws EpicException {
		if (name != null && name.length() > 0) {
			this.name = name;
		} else {
			throw new EpicException("epic's name cannot be null");
		}
	}

	public void addIssue(Issue toAdd) throws EpicException {
		if (toAdd != null) {
			this.issues.add(toAdd);
		} else {
			throw new EpicException("cannot add issue with value null");
		}
	}

	public String getName() {
		return this.name;
	}
}
