package com.reservation.application.dto;

import com.reservation.domain.model.car.Car;
import com.reservation.domain.model.car.LicensePlate;
import com.reservation.domain.model.car.Model;

public class CarDto {
	
	private LicensePlate licensePlate;
	private Model model;
	

	public CarDto(LicensePlate licensePlate, Model model) {
		super();
		this.licensePlate = licensePlate;
		this.model = model;
	}
	
	public static CarDto basedOn(Car car) {
		return new CarDto(car.getLicensePlate(), car.getModel());
	}

	public LicensePlate getLicensePlate() {
		return licensePlate;
	}

	public Model getModel() {
		return model;
	}

}
