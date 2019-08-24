package com.reservation.domain.model.reservation.exceptions;

import com.reservation.domain.model.car.exceptions.CarRentalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCityException extends CarRentalException {

	private static final long serialVersionUID = 4297245597101677210L;
	

	public InvalidCityException() {
		super();
	}

	public InvalidCityException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidCityException(String s) {
		super(s);
	}

	public InvalidCityException(Throwable cause) {
		super(cause);
	}

}
