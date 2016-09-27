package com.godzilla.model;

import java.util.HashSet;
import java.util.Set;

import com.godzilla.model.exceptions.CompanyException;

public class Company {
	private  int id;
	private String name;
	private Set<Project> projects;
	private Set<User> users;
	
	public Company(String name) throws CompanyException{
		this.projects = new HashSet<>();
		this.users = new HashSet<>();
		try {
			this.setName(name);
		} catch (CompanyException e) {
			throw new CompanyException("Invalid company name ",e);
		}
	}
	
	private void setName(String name) throws CompanyException{
		if(name != null && !name.trim().equals("")){
			this.name = name;
		}else{
			throw new CompanyException("name value = null");
		}
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
