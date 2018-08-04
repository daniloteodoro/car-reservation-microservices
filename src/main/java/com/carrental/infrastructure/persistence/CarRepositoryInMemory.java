package com.carrental.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.carrental.domain.model.car.AvailableCarList;
import com.carrental.domain.model.car.Brand;
import com.carrental.domain.model.car.Car;
import com.carrental.domain.model.car.CarRepository;
import com.carrental.domain.model.car.Category;
import com.carrental.domain.model.car.LicensePlate;
import com.carrental.domain.model.car.Model;
import com.carrental.domain.model.reservation.City;
import com.carrental.domain.model.reservation.Country;

@Repository
public class CarRepositoryInMemory implements CarRepository {
	
	public List<Car> getCarList() {
		List<Car> cars = new ArrayList<>();
		
		Car availableGolf = new Car.Builder()
				.withModel(new Model(new Brand("VW"), "Golf", Category.MEDIUMSIZED))
				.withPickupLocation(new City("Rotterdam", Country.NETHERLANDS))
				.withPickupDateTime(LocalDateTime.of(2018, 07, 01, 8, 00))
				.withDropoffLocation(new City("Rotterdam", Country.NETHERLANDS))
				.withDropoffDateTime(LocalDateTime.of(2018, 07, 30, 18, 00))
				.withLicensePlate(new LicensePlate("AB-1234"))
				.withPricePerDay(50.0)
				.build();
		cars.add(availableGolf);
		
		Car availableTesla = new Car.Builder()
				.withModel(new Model(new Brand("Tesla"), "Model S", Category.PREMIUM))
				.withPickupLocation(new City("Rotterdam", Country.NETHERLANDS))
				.withPickupDateTime(LocalDateTime.of(2018, 07, 02, 15, 00))
				.withDropoffLocation(new City("Rotterdam", Country.NETHERLANDS))
				.withDropoffDateTime(LocalDateTime.of(2018, 07, 02, 15, 00))
				.withLicensePlate(new LicensePlate("XS-6345"))
				.withPricePerDay(200.0)
				.build();
		cars.add(availableTesla);
		
		return cars;
	}
	
	@Override
	public AvailableCarList basedOn(City pickupLocation, LocalDateTime pickupDateTime, City dropoffLocation,
			LocalDateTime dropoffDateTime) {
		List<Car> cars = getCarList();
		
		// Returning a customized class instead of Collections.unmodifiableList to allow business methods to be added (e.g. findByModel)
		List<Car> foundCars = cars.stream()
			.filter((car) -> {
				return car.isAvailable(pickupLocation, pickupDateTime, dropoffLocation, dropoffDateTime);
			}).collect(Collectors.toList());
		
		return new AvailableCarList(foundCars);
	}
	
	

}



