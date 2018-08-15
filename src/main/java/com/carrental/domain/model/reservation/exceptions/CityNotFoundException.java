package com.carrental.domain.model.reservation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.carrental.domain.model.reservation.CarRentalException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CityNotFoundException extends CarRentalException {

	private static final long serialVersionUID = 4297245597101677210L;
	

	public CityNotFoundException() {
		super();
	}

	public CityNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public CityNotFoundException(String s) {
		super(s);
	}

	public CityNotFoundException(Throwable cause) {
		super(cause);
	}

}
