package com.godzilla.model;

import com.godzilla.model.exceptions.IssueException;

public class Story extends Issue {

	public Story(String summary) throws IssueException{
		super(summary);
	}
}
