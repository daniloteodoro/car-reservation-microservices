package com.reservation.domain.model.reservation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.reservation.domain.model.car.exceptions.CarRentalException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReservationException extends CarRentalException {

	private static final long serialVersionUID = -5490248715359705416L;

	public ReservationException() {
		super();
	}

	public ReservationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ReservationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReservationException(String message) {
		super(message);
	}

	public ReservationException(Throwable cause) {
		super(cause);
	}
	
}
