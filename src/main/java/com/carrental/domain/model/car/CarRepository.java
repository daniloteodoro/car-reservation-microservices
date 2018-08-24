package com.carrental.domain.model.car;

import java.time.LocalDateTime;
import java.util.List;

import com.carrental.domain.model.reservation.City;

public interface CarRepository {
	
	AvailableCarList basedOn(City pickupLocation, LocalDateTime pickupDateTime, City dropoffLocation, LocalDateTime dropoffDateTime);
	
	List<CategoryFeaturingCar> categoryBasedOn(City pickupLocation, LocalDateTime pickupDateTime, City dropoffLocation, LocalDateTime dropoffDateTime);
	
}
