package com.carrental.domain.model.reservation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CityFormatException extends IllegalArgumentException {

	private static final long serialVersionUID = -6632642650019060811L;

	public CityFormatException() {
		super();
	}

	public CityFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public CityFormatException(String s) {
		super(s);
	}

	public CityFormatException(Throwable cause) {
		super(cause);
	}

}
