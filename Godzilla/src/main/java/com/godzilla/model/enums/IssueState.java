package com.godzilla.model.enums;

public enum IssueState {
	TO_DO(1),
	IN_PROGRESS(2),
	DONE(3);
	
	private final int id;
    IssueState(int id) { this.id = id; }
    public int getValue() { return id; }
    
	public static IssueState getTypeById(int stateId) {
		for (IssueState type : IssueState.values()) {
			if (type.getValue() == stateId) {
				return type;
			}
		}
		
		return null;
	}
	
	public static IssueState getIssueStateFromString(String issueStateAsString){
		switch (issueStateAsString) {
		case "TO_DO":
			return IssueState.TO_DO;
		case "IN_PROGRESS":
			return IssueState.IN_PROGRESS;
		case "DONE":
			return IssueState.DONE;
		default:
			return IssueState.TO_DO;
		}
	}
}
