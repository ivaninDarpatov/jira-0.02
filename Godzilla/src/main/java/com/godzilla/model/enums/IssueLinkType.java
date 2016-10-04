package com.godzilla.model.enums;

public enum IssueLinkType {
	IS_BLOCKED_BY, BLOCKS,
	IS_CAUSED_BY, CAUSES,
	IS_CLONED_BY, CLONES,
	IS_DUPLICATED_BY, DUPLICATES,
	RELATES_TO;
	
	public static IssueLinkType getOppositeLinkType(IssueLinkType linkType) {
		switch (linkType) {
		case IS_BLOCKED_BY:
			return BLOCKS;
		case BLOCKS:
			return IS_BLOCKED_BY;
		case IS_CAUSED_BY:
			return CAUSES;
		case CAUSES:
			return IS_CAUSED_BY;
		case IS_CLONED_BY:
			return CLONES;
		case CLONES:
			return IS_CLONED_BY;
		case IS_DUPLICATED_BY:
			return DUPLICATES;
		case DUPLICATES:
			return IS_DUPLICATED_BY;
		default:
			return RELATES_TO;
		}
	}
}
