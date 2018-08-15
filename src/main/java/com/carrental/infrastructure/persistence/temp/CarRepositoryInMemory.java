package com.carrental.infrastructure.persistence.temp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.carrental.domain.model.car.AvailableCarList;
import com.carrental.domain.model.car.Car;
import com.carrental.domain.model.car.CarRepository;
import com.carrental.domain.model.reservation.City;

@Repository
public class CarRepositoryInMemory implements CarRepository {
	
	private final EntityManager entityManager;
	
	
	public CarRepositoryInMemory(@Autowired final EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}
	
	// Get the car list right from the DB
	@SuppressWarnings("unchecked")
	public List<Car> getCarList() {
		List<Car> cars = null;
		
		try {
			Query query = entityManager.createQuery("select car from Car car");
			
			cars = query.getResultList();
			cars.stream().forEach((car) -> System.out.println(car));
			
			query = entityManager.createQuery("select c from City c");
			
			List<City> cs = query.getResultList();
			for (City city : cs) {
				System.out.println(city.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cars;
	}
	
	@Override
	@Transactional
	public AvailableCarList basedOn(City pickupLocation, LocalDateTime pickupDateTime, City dropoffLocation, LocalDateTime dropoffDateTime) {
		List<Car> cars = getCarList();
		
		// Returning a customized class instead of Collections.unmodifiableList to allow business methods to be added (e.g. findByModel)
		List<Car> foundCars = cars.stream()
			.filter((car) -> {
				return car.isAvailable(pickupLocation, pickupDateTime, dropoffLocation, dropoffDateTime);
			})
			.collect(Collectors.toList());
		
		return new AvailableCarList(foundCars);
	}
	
	

}



