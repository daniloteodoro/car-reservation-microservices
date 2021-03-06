package com.reservation.domain.model.reservation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CarUnavailableException extends ReservationException {

	private static final long serialVersionUID = -6614127744974131275L;

	public CarUnavailableException() {
		super();
	}
	public CarUnavailableException(String message) {
		super(message);
	}

}
