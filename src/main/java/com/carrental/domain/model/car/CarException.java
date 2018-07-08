package com.carrental.domain.model.car;

import com.carrental.domain.model.reservation.CarRentalException;

public class CarException extends CarRentalException {

	private static final long serialVersionUID = -626010614175285327L;

	public CarException() {
		super();
	}

	public CarException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CarException(String message, Throwable cause) {
		super(message, cause);
	}

	public CarException(String message) {
		super(message);
	}

	public CarException(Throwable cause) {
		super(cause);
	}

}
