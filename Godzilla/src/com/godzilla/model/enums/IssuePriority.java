package com.godzilla.model.enums;

public enum IssuePriority {
	VERY_HIGH(1),
	HIGH(2),
	MEDIUM(3),
	LOW(4),
	VERY_LOW(5);
	
	private final int id;
    IssuePriority(int id) { this.id = id; }
    public int getValue() { return id; }
}
