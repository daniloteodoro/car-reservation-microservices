package com.carrental.domain.model.reservation;

public class CarRentalException extends Exception {

	private static final long serialVersionUID = -1962097048157848443L;

	public CarRentalException() {
		super();
	}

	public CarRentalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CarRentalException(String message, Throwable cause) {
		super(message, cause);
	}

	public CarRentalException(String message) {
		super(message);
	}

	public CarRentalException(Throwable cause) {
		super(cause);
	}
	
	

}
