package com.godzilla.model;

import java.util.Set;

public class Epic extends Issue {
	String name;
	Set<Issue> issues;
	
	public Epic(String summary){
		super(summary);
	}
}
