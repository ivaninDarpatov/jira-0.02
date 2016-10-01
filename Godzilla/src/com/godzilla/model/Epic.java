package com.godzilla.model;

import java.util.Set;

public class Epic extends Issue {
	String name;
	Set<Issue> issues;
	
	public Epic(String summary){
		super(summary);
	}
	
	public void setName(String name) {
		if (name != null && name.length() > 0) {
			this.name = name;
		}
	}
	
	public void addIssue(Issue toAdd) {
		if (toAdd != null) {
			this.issues.add(toAdd);
		}
	}
	
	public String getName(){
		return this.name;
	}
}
