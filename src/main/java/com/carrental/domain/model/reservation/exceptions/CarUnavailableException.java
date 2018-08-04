package com.carrental.domain.model.reservation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.carrental.domain.model.reservation.ReservationException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CarUnavailableException extends ReservationException {
	
	private static final long serialVersionUID = -6614127744974131275L;
	
	
	public CarUnavailableException() {
		super();
	}

	public CarUnavailableException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CarUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}

	public CarUnavailableException(String message) {
		super(message);
	}

	public CarUnavailableException(Throwable cause) {
		super(cause);
	}


}
