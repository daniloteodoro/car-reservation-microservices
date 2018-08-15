package com.carrental.unittest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;

import org.junit.Test;

import com.carrental.domain.model.car.Brand;
import com.carrental.domain.model.car.Car;
import com.carrental.domain.model.car.Category;
import com.carrental.domain.model.car.LicensePlate;
import com.carrental.domain.model.car.Model;
import com.carrental.domain.model.reservation.City;
import com.carrental.domain.model.reservation.Country;

public class CarTest {
	
	private City rotterdam = new City("Rotterdam", Country.NL);
	
	
	@Test
	public void testIfCarIsAvailable() {
		LocalDateTime start = LocalDateTime.of(2018, 8, 10, 10, 00);
		LocalDateTime finish = LocalDateTime.of(2018, 8, 12, 10, 00);
		
		Car car = new Car.Builder()
					.withModel(new Model(new Brand("VW"), "Golf", Category.MEDIUMSIZED))
					.withPickupLocation(rotterdam)
					.withPickupDateTime(LocalDateTime.of(2018, 8, 1, 10, 00))
					.withDropoffLocation(rotterdam)
					.withDropoffDateTime(LocalDateTime.of(2018, 8, 31, 10, 00))
					.withPricePerDay(40.0)
					.withLicensePlate(new LicensePlate("123"))
					.build();
		
		assertThat(car.isAvailable(rotterdam, start, rotterdam, finish), is(true));
	}
	
	@Test
	public void testIfCarIsUnavailable() {
		LocalDateTime start = LocalDateTime.of(2018, 8, 10, 10, 00);
		LocalDateTime finish = LocalDateTime.of(2018, 8, 12, 10, 00);
		
		Car car = new Car.Builder()
					.withModel(new Model(new Brand("VW"), "Golf", Category.MEDIUMSIZED))
					.withPickupLocation(rotterdam)
					.withPickupDateTime(LocalDateTime.of(2018, 8, 1, 10, 00))
					.withDropoffLocation(rotterdam)
					.withDropoffDateTime(LocalDateTime.of(2018, 8, 11, 10, 00))
					.withPricePerDay(40.0)
					.withLicensePlate(new LicensePlate("123"))
					.build();
		
		assertThat(car.isAvailable(rotterdam, start, rotterdam, finish), is(false));
	}

}







