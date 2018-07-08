package com.carrental.domain.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.carrental.domain.model.car.Brand;
import com.carrental.domain.model.car.Car;
import com.carrental.domain.model.car.Category;
import com.carrental.domain.model.car.LicensePlate;
import com.carrental.domain.model.car.Model;
import com.carrental.domain.model.car.Car.CarBuilder;
import com.carrental.domain.model.reservation.City;
import com.carrental.domain.model.reservation.Country;

// TODO: 
public class SearchAvailableCarsSampleDataStore {
	
	
	public static List<Car> getCarList() {
		List<Car> cars = new ArrayList<>();
		
		Car availableGolf = new CarBuilder()
				.withModel(new Model(new Brand("VW"), "Golf", Category.MEDIUMSIZED))
				.withPickupLocation(new City("Rotterdam", Country.NETHERLANDS))
				.withPickupDateTime(LocalDateTime.of(2018, 07, 01, 8, 00))
				.withDropoffLocation(new City("Rotterdam", Country.NETHERLANDS))
				.withDropoffDateTime(LocalDateTime.of(2018, 07, 30, 18, 00))
				.withLicensePlate(new LicensePlate("AB-1234"))
				.withPricePerDay(50.0)
				.build();
		cars.add(availableGolf);
		
		/*
		cars.add(new Car("VW", "Golf"));
		cars.add(new Car("VW", "Jetta"));
		cars.add(new Car("VW", "Passat"));
		
		cars.add(new Car("Ford", "Focus"));
		cars.add(new Car("Ford", "Fusion"));
		cars.add(new Car("Ford", "Mustang"));
		
		cars.add(new Car("Honda", "Civic"));
		
		cars.add(new Car("Citroen", "C3"));
		cars.add(new Car("Citroen", "C4"));
		
		cars.add(new Car("Audi", "A1"));
		cars.add(new Car("Audi", "A3"));
		cars.add(new Car("Audi", "A4"));
		cars.add(new Car("Audi", "A6"));
		*/
		
		Car availableTesla = new CarBuilder()
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

}






