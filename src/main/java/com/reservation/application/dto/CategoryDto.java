package com.reservation.application.dto;

import com.reservation.domain.model.car.CategoryPricing;
import com.reservation.domain.model.car.CategoryType;
import com.reservation.domain.model.reservation.Reservation;

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

	public static CategoryDto basedOn(Reservation reservation) {
		return new CategoryDto(reservation.getType(), reservation.getPricePerDay().getValue(), reservation.getStandardInsurancePrice().getValue(), reservation.getFullInsurancePrice().getValue());
	}

	public static CategoryDto basedOn(CategoryPricing categoryPricing) {
		return new CategoryDto(categoryPricing.getType(), categoryPricing.getPricePerDay().getValue(), categoryPricing.getStandardInsurance().getValue(), categoryPricing.getFullInsurance().getValue());
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
