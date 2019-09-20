package com.reservation.domain.model.car;

import com.reservation.domain.model.reservation.City;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
	
	List<CategoryPricing> findAll();
	
	Optional<CategoryPricing> findById(CategoryType categoryType);

	List<Category> getCategoryAvailabilities(City pickupLocation, LocalDateTime pickupDateTime, City dropOffLocation, LocalDateTime dropOffDateTime);

	Optional<Category> getCategoryAvailabilityFor(CategoryType type, City pickupLocation, LocalDateTime pickupDateTime, City dropOffLocation, LocalDateTime dropOffDateTime);

}
