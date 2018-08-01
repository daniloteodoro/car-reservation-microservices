package com.carrental.domain.model.reservation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.carrental.domain.model.reservation.ReservationException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReservationNotFoundException extends ReservationException {

	private static final long serialVersionUID = -2502039936828883592L;

	public ReservationNotFoundException() {
		super();
	}

	public ReservationNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ReservationNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReservationNotFoundException(String message) {
		super(message);
	}

	public ReservationNotFoundException(Throwable cause) {
		super(cause);
	}

}
