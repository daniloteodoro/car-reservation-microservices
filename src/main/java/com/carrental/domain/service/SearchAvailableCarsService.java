package com.carrental.domain.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.carrental.domain.model.car.AvailableCarList;
import com.carrental.domain.model.car.Car;
import com.carrental.domain.model.reservation.City;

public class SearchAvailableCarsService {
	
	public AvailableCarList basedOn(City pickupLocation, LocalDateTime pickupDateTime, City dropoffLocation, LocalDateTime dropoffDateTime) {
		List<Car> cars = new ArrayList<>();
		
		// TODO: Get this information from Repository
		// Use in-memory temp dataset to populate car list and availability
		cars.addAll(SearchAvailableCarsSampleDataStore.getCarList());
		
		// Returning a customized class instead of Collections.unmodifiableList to allow business methods to be added (e.g. findByModel)
		List<Car> foundCars = cars.stream()
			.filter((carAvailability) -> {
			
				boolean isAvailable = 
						carAvailability.getPickupLocation().equals(pickupLocation) &&
						carAvailability.getDropoffLocation().equals(pickupLocation) &&
						carAvailability.getPickupDateTime().isBefore(pickupDateTime) &&
						carAvailability.getDropoffDateTime().isAfter(dropoffDateTime);
				
				return isAvailable;
			
			}).collect(Collectors.toList());
		
		return new AvailableCarList(foundCars);
	}

}
