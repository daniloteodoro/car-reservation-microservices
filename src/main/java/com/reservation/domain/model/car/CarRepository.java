package com.reservation.domain.model.car;

import java.time.LocalDateTime;

import com.reservation.domain.model.reservation.City;

public interface CarRepository {
	
	AvailableCarList basedOn(City pickupLocation, LocalDateTime pickupDateTime, City dropoffLocation, LocalDateTime dropoffDateTime);
	
}
