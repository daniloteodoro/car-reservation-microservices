package com.carrental.application.dto;

import java.time.LocalDateTime;

import com.carrental.domain.model.car.Car;
import com.carrental.domain.model.car.CarRentalStore;
import com.carrental.domain.model.car.LicensePlate;
import com.carrental.domain.model.car.Model;
import com.carrental.domain.model.reservation.City;

public class CarDto {
	
	private LicensePlate licensePlate;
	private Model model;
	private City pickupLocation;
	private LocalDateTime pickupDateTime;
	private City dropoffLocation;
	private LocalDateTime dropoffDateTime;
	private Double standardInsurance;
	private Double fullInsurance;
	
	// TODO: Create VO class to validate price
	private Double pricePerDay;
	
	private CarRentalStore store;
	

	public CarDto(LicensePlate licensePlate, Model model, City pickupLocation, LocalDateTime pickupDateTime,
			City dropoffLocation, LocalDateTime dropoffDateTime, Double pricePerDay, CarRentalStore store,
			Double standardInsurance, Double fullInsurance) {
		super();
		this.licensePlate = licensePlate;
		this.model = model;
		this.pickupLocation = pickupLocation;
		this.pickupDateTime = pickupDateTime;
		this.dropoffLocation = dropoffLocation;
		this.dropoffDateTime = dropoffDateTime;
		this.pricePerDay = pricePerDay;
		this.store = store;
		this.standardInsurance = standardInsurance;
		this.fullInsurance = fullInsurance;
	}
	
	public static CarDto basedOn(Car car) {
		return new CarDto(car.getLicensePlate(), car.getModel(), 
						  car.getPickupLocation(), car.getPickupDateTime(), car.getDropoffLocation(), car.getDropoffDateTime(), 
						  car.getPricePerDay(), car.getStore(), car.getStandardInsurance(), car.getFullInsurance());
	}

	public LicensePlate getLicensePlate() {
		return licensePlate;
	}

	public Model getModel() {
		return model;
	}

	public City getPickupLocation() {
		return pickupLocation;
	}

	public LocalDateTime getPickupDateTime() {
		return pickupDateTime;
	}

	public City getDropoffLocation() {
		return dropoffLocation;
	}

	public LocalDateTime getDropoffDateTime() {
		return dropoffDateTime;
	}

	public Double getPricePerDay() {
		return pricePerDay;
	}

	public CarRentalStore getStore() {
		return store;
	}

	public Double getStandardInsurance() {
		return standardInsurance;
	}

	public Double getFullInsurance() {
		return fullInsurance;
	}

}
