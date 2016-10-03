package com.godzilla.model;

import com.godzilla.model.exceptions.IssueException;

public class Task extends Issue {

	public Task(String summary) throws IssueException {
		super(summary);
	}
}
