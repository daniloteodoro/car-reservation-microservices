package com.carrental.domain.model.car;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.carrental.domain.model.reservation.City;

public interface CategoryRepository {
	
	List<Category> findAll();
	
	Optional<Category> findById(CategoryType categoryType);
	
	List<CategoryFeaturingModel> availableOn(City pickupLocation, LocalDateTime pickupDateTime, City dropoffLocation, LocalDateTime dropoffDateTime);

}
