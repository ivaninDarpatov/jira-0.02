package com.godzilla.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.godzilla.model.exceptions.SprintException;



public class Sprint {
	private int id;
//	private int projectId;
	private String name;
	private String sprintGoal;
	private LocalDateTime startingDate;
	private LocalDateTime endDate;
	private Set<Issue> issues;
	
	//Id,name, StartingDate , projectId
	public Sprint(String name) throws SprintException{
		try {
			setName(name);
		} catch (SprintException e) {
			throw new SprintException("failed to change Namge",e);
		}
		
		issues = new HashSet<Issue>(); 
	}
	
	public void setName(String name) throws SprintException{
		if(name != null && !name.trim().equals("")){
			this.name = name;
		}else{
			throw new SprintException("Invalid sprint name.");
		}
	}
	
	public void setSprintGoal(String goal) throws SprintException{
		if(goal != null){
			this.sprintGoal = goal;
		}else{
			throw new SprintException("goal value == null");
		}
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getSpintGoal(){
		return this.sprintGoal;
	}
	
	public void setId(int id){
		this.id = id;
	}

	@Override
	public String toString() {
		return "Sprint [id=" + id + ", name=" + name + ", sprintGoal=" + sprintGoal
				+ ", startingDate=" + startingDate + ", endDate=" + endDate + "]";
	}

	public int getId() {
		return this.id;
	}
}
