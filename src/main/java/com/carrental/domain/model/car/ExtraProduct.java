package com.carrental.domain.model.car;

public enum ExtraProduct {
	
	GPS(10.0),
	ADDITIONAL_DRIVER(12.0),
	CHILD_SEAT(10.0);
	
	private final Double price;
	
	ExtraProduct(Double price) {
		this.price = price;
	}
	
	public Double getPrice() {
		return this.price;
	}

}
