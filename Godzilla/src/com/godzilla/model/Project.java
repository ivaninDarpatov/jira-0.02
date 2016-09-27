package com.godzilla.model;

import java.util.HashSet;
import java.util.Set;

public class Project {
	private int id;
	private String name;
	private Set<Issue> issues;
	private Set<Sprint> sprints;
	
	
	public Project(String name,String companyName){
		this.issues = new HashSet<>();
		this.sprints = new HashSet<>();
		
		this.setName(name);
	}

	private void setName(String name){
		if(name != null && !name.trim().equals("")){
			this.name = name;
		}//TODO: exception
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName(){
		return this.name;
	}
}
