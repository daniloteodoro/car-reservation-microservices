package com.carrental.domain.model.car;

public enum ExtraProduct {
	
	GPS(new Price(10.0)),
	ADDITIONAL_DRIVER(new Price(12.0)),
	CHILD_SEAT(new Price(10.0));
	
	private final Price price;
	
	ExtraProduct(Price price) {
		this.price = price;
	}
	
	public Price getPrice() {
		return this.price;
	}

}
