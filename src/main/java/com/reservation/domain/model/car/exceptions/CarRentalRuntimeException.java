package com.reservation.domain.model.car.exceptions;

public class CarRentalRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = -1962097048157848443L;
	
	public CarRentalRuntimeException() {
		super();
	}

	public CarRentalRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CarRentalRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public CarRentalRuntimeException(String message) {
		super(message);
	}

	public CarRentalRuntimeException(Throwable cause) {
		super(cause);
	}
	
	

}
