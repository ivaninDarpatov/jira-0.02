package com.godzilla.model;

import com.godzilla.model.exceptions.IssueException;

public class Bug extends Issue {

	public Bug(String summary) throws IssueException{
		super(summary);
	}
}
