package com.carrental.domain.model.car.exceptions;

public class InvalidPriceException extends CarRentalRuntimeException {

	private static final long serialVersionUID = -8375252871862842539L;

	public InvalidPriceException() {
		super();
	}

	public InvalidPriceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidPriceException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidPriceException(String message) {
		super(message);
	}

	public InvalidPriceException(Throwable cause) {
		super(cause);
	}

	
	
}
