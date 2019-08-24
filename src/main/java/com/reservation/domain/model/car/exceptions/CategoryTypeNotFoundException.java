package com.reservation.domain.model.car.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryTypeNotFoundException extends CarRentalException {

	private static final long serialVersionUID = 429724598239832L;

	public CategoryTypeNotFoundException() {
		super("Category Type not found");
	}
	public CategoryTypeNotFoundException(String s) {
		super(s);
	}
}
