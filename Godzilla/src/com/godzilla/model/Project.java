package com.godzilla.model;

import java.util.HashSet;
import java.util.Set;

import com.godzilla.model.exceptions.ProjectException;

public class Project {
	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + ", issues=" + issues + ", sprints=" + sprints + "]";
	}

	private int id;
	private String name;
	private Set<Issue> issues;
	private Set<Sprint> sprints;
	
	
	public Project(String name) throws ProjectException{
		this.issues = new HashSet<>();
		this.sprints = new HashSet<>();
		
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
	
	public void addIssue(Issue issueToAdd) throws ProjectException{
		if(issueToAdd != null){
			issues.add(issueToAdd);
		}else{
			throw new ProjectException("issue value = null");
		}
	}
	
	public void addSprint(Sprint sprintToAdd) throws ProjectException{
		if(sprintToAdd != null){
			sprints.add(sprintToAdd);
		}else{
			throw new ProjectException("Sprint value == null");
		}
	}
}
