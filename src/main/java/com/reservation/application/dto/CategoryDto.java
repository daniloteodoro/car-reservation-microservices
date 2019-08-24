package com.reservation.application.dto;

import com.reservation.domain.model.car.Category;
import com.reservation.domain.model.car.CategoryType;

public class CategoryDto {
	
	private CategoryType type;
	private Double pricePerDay;
	private Double standardInsurance;
	private Double fullInsurance;
	
	public CategoryDto(CategoryType type, Double pricePerDay, Double standardInsurance, Double fullInsurance) {
		super();
		this.type = type;
		this.pricePerDay = pricePerDay;
		this.standardInsurance = standardInsurance;
		this.fullInsurance = fullInsurance;
	}
	
	public static CategoryDto basedOn(Category category) {
		return new CategoryDto(category.getType(), category.getPricePerDay().getValue(), category.getStandardInsurance().getValue(), category.getFullInsurance().getValue());
	}

	public CategoryType getType() {
		return type;
	}

	public Double getPricePerDay() {
		return pricePerDay;
	}

	public Double getStandardInsurance() {
		return standardInsurance;
	}

	public Double getFullInsurance() {
		return fullInsurance;
	}

}
