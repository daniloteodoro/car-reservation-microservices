package com.reservation.domain.model.car;

import java.util.Arrays;
import java.util.Optional;

public enum CategoryType {
	
	ECONOMIC,
	COMPACT,
	MEDIUMSIZED,
	CONFORT,
	PREMIUM,
	VAN,
	SPORT;

	public static Optional<CategoryType> findByName(String type) {
		return Arrays.stream(values())
				.filter(categoryType -> categoryType.name().equalsIgnoreCase(type))
				.findFirst();
	}
	
}
