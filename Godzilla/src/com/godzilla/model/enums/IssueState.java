package com.godzilla.model.enums;

public enum IssueState {
	TO_DO(1),
	IN_PROGRESS(2),
	DONE(3);
	
	private final int id;
    IssueState(int id) { this.id = id; }
    public int getValue() { return id; }
}
