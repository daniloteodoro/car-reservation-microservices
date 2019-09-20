package com.reservation.domain.model.car.exceptions;

public class CarRentalRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 6919198169384223846L;

	public CarRentalRuntimeException() {
		super();
	}

	public CarRentalRuntimeException(String message) {
		super(message);
	}

	public CarRentalRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}
