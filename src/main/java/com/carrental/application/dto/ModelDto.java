package com.carrental.application.dto;

import com.carrental.domain.model.car.Model;

public class ModelDto {
	
	private String brand;
	private String description;
	private CategoryDto category;
	
	
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
