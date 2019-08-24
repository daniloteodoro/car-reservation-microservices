package com.reservation.application.dto;

import com.reservation.domain.model.car.Model;

public class ModelDto {
	
	private final String brand;
	private final String description;
	private final CategoryDto category;
	
	
	public ModelDto(String brand, String description, CategoryDto category) {
		super();
		this.brand = brand;
		this.description = description;
		this.category = category;
	}
	
	public static ModelDto basedOn(Model model) {
		return new ModelDto(model.getBrand().getDescription(), model.getDescription(), CategoryDto.basedOn(model.getCategory()));
	}
	
	public String getBrand() {
		return brand;
	}
	public String getDescription() {
		return description;
	}
	public CategoryDto getCategory() {
		return category;
	}

}
