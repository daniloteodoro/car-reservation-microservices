package com.reservation.domain.model.car.exceptions;

public class InvalidPriceException extends CarRentalRuntimeException {

	private static final long serialVersionUID = -8375252871862842539L;

	public InvalidPriceException() {
		super();
	}

	public InvalidPriceException(String message) {
		super(message);
	}

}
