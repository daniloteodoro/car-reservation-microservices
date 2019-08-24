package com.reservation.domain.model.car;

import com.reservation.domain.model.reservation.City;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
	
	List<Category> findAll();
	
	Optional<Category> findById(CategoryType categoryType);

	List<CategoryAvailability> getCategoryAvailability(City pickupLocation, LocalDateTime pickupDateTime, City dropOffLocation, LocalDateTime dropOffDateTime);

}
