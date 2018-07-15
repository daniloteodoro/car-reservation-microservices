package com.carrental.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carrental.application.dto.CarDto;
import com.carrental.domain.model.car.CarRepository;
import com.carrental.domain.model.reservation.City;
import com.carrental.domain.model.reservation.Country;

@RestController
public class CarRentalController {
	
	private final CarRepository carRepository;
	
	@Autowired
	public CarRentalController(CarRepository carRepository) {
		super();
		this.carRepository = carRepository;
	}
	
	@GetMapping("/search_cars")
	public List<CarDto> searchCars() {
		City rotterdam = new City("Rotterdam", Country.NETHERLANDS);
		LocalDateTime start = LocalDateTime.of(2018, 7, 3, 16, 30);
		LocalDateTime finish = LocalDateTime.of(2018, 7, 8, 16, 00);
		
		List<CarDto> cars = 
				carRepository.basedOn(rotterdam, start, rotterdam, finish)
					.stream()
					.map((car) -> new CarDto(car.getLicensePlate(), car.getModel(), car.getPickupLocation(), car.getPickupDateTime(), car.getDropoffLocation(), car.getDropoffDateTime(), car.getPricePerDay(), car.getStore()))
					.collect(Collectors.toList());
		
		return cars;
	}

}











