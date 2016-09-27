package com.godzilla.model;

import java.time.LocalDateTime;
import java.util.Set;

public class Sprint {
	int id;
	String name;
	String description;
	LocalDateTime startingDate;
	LocalDateTime endDate;
	Set<Issue> issues;
}
