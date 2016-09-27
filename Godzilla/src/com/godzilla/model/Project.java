package com.godzilla.model;

import java.util.HashSet;
import java.util.Set;

import com.godzilla.model.exceptions.ProjectException;

public class Project {
	private int id;
	private String name;
//	private Set<Issue> issues;
//	private Set<Sprint> sprints;
	
	
	public Project(String name) throws ProjectException{
//		this.issues = new HashSet<>();
//		this.sprints = new HashSet<>();
		
		try {
			this.setName(name);
		} catch (ProjectException e) {
			throw new ProjectException("Invalud project name", e);
		}
	}

	private void setName(String name) throws ProjectException{
		if(name != null && !name.trim().equals("")){
			this.name = name;
		}else{
			throw new ProjectException("project name value null");
		}
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
