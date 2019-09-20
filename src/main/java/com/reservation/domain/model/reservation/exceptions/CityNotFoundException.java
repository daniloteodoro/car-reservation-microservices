package com.reservation.domain.model.reservation.exceptions;

import com.reservation.domain.model.car.exceptions.CarRentalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CityNotFoundException extends CarRentalException {

	private static final long serialVersionUID = 5575240237410335435L;

	public CityNotFoundException() {
		super();
	}
	public CityNotFoundException(String s) { super(s); }

}
