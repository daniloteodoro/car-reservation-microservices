package com.reservation.domain.model.car;

import com.reservation.domain.model.shared.ValueObject;

public enum ExtraProduct implements ValueObject {

	GPS(new Price(10.0)),
	ADDITIONAL_DRIVER(new Price(12.0)),
	CHILD_SEAT(new Price(10.0));

	private Price price;

	ExtraProduct(Price price) {
		this.price = price;
	}

	public Price getPrice() {
		return price;
	}

}
