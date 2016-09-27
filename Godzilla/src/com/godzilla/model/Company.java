package com.godzilla.model;

import java.util.HashSet;
import java.util.Set;

public class Company {
	private  int id;
	private String name;
	private Set<Project> projects;
	private Set<User> users;
	
	public Company(String name){
		this.projects = new HashSet<>();
		this.users = new HashSet<>();
		this.setName(name);
	}
	
	private void setName(String name){
		if(name != null && !name.trim().equals("")){
			this.name = name;
		}//TODO: exception
	}
	
	public void addNewProject(Project newProject){
		if(newProject != null){
			this.projects.add(newProject);
		}//TODO:exception
	}
	
	public void addNewUser(User newUser){
		if(newUser != null){
			this.users.add(newUser);
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
