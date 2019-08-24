package com.carrental.domain.model.reservation.exceptions;

import com.carrental.domain.model.car.exceptions.CarRentalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExtraProductUnavailableException extends CarRentalException {

	private static final long serialVersionUID = -6614127744974131275L;


	public ExtraProductUnavailableException() {
		super();
	}

	public ExtraProductUnavailableException(String message, Throwable cause, boolean enableSuppression,
                                            boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ExtraProductUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExtraProductUnavailableException(String message) {
		super(message);
	}

	public ExtraProductUnavailableException(Throwable cause) {
		super(cause);
	}


}
