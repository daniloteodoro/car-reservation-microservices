package com.reservation.domain.model.reservation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CategoryUnavailableException extends ReservationException {

	private static final long serialVersionUID = -5653412110270129543L;


	public CategoryUnavailableException() {
		super();
	}
	public CategoryUnavailableException(String message) {
		super(message);
	}

}
