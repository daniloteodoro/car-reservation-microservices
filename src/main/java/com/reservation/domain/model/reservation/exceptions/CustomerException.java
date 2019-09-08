package com.reservation.domain.model.reservation.exceptions;

import com.reservation.domain.model.car.exceptions.CarRentalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomerException extends CarRentalException {

	private static final long serialVersionUID = -5490248715359705416L;

	public CustomerException() {
		super();
	}

	public CustomerException(String message, Throwable cause, boolean enableSuppression,
                             boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CustomerException(String message, Throwable cause) {
		super(message, cause);
	}

	public CustomerException(String message) {
		super(message);
	}

	public CustomerException(Throwable cause) {
		super(cause);
	}
	
}
