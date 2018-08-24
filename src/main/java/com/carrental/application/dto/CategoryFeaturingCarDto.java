package com.carrental.application.dto;

import com.carrental.domain.model.car.Car;
import com.carrental.domain.model.car.Category;

public class CategoryFeaturingCarDto {
	
	private Category category;
	private CarDto car;
	
	public CategoryFeaturingCarDto(Category category, CarDto car) {
		super();
		this.category = category;
		this.car = car;
	}
	
	public static CategoryFeaturingCarDto basedOn(Category category, Car car) {
		return new CategoryFeaturingCarDto(category, CarDto.basedOn(car));
	}
	
	protected CategoryFeaturingCarDto() {
		super();
	}

	public Category getCategory() {
		return category;
	}

	public CarDto getCar() {
		return car;
	}

}
