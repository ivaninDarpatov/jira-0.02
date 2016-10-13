package com.godzilla.model.enums;

public enum Permissions {
	MANAGER,
	PROGRAMMER,
	TESTER;
	
	public static Permissions getPermissionsById(int id) {
		switch (id) {
		case 1:
			return Permissions.MANAGER;
		case 2:
			return Permissions.PROGRAMMER;
		default:
			return Permissions.TESTER;
		}
	}
}
