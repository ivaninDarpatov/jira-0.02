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
    
    
	public static IssuePriority getTypeById(int priorityId) {
		for (IssuePriority type : IssuePriority.values()) {
			if (type.getValue() == priorityId) {
				return type;
			}
		}
		
		return null;
	}
	
	public static IssuePriority getPriorityFromString(String priorityAsString){
		switch (priorityAsString) {
		case "VERY_LOW":
			return IssuePriority.VERY_LOW;
		case "LOW":
			return IssuePriority.LOW;
		case "MEDIUM":
			return IssuePriority.MEDIUM;
		case "HIGH":
			return IssuePriority.HIGH;
		case "VERY_HIGH":
			return IssuePriority.VERY_HIGH;
		default:
			return IssuePriority.MEDIUM;
		}
	}
}
