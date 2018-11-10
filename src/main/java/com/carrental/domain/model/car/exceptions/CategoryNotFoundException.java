package com.carrental.domain.model.car.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.carrental.domain.model.car.exceptions.CarRentalException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends CarRentalException {

	private static final long serialVersionUID = 4297245597101677210L;
	

	public CategoryNotFoundException() {
		super();
	}

	public CategoryNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public CategoryNotFoundException(String s) {
		super(s);
	}

	public CategoryNotFoundException(Throwable cause) {
		super(cause);
	}

}
