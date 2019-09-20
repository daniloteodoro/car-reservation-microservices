package com.reservation.application.exceptions;

import com.reservation.domain.model.car.exceptions.CarRentalRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SearchCarException extends CarRentalRuntimeException {

    private static final long serialVersionUID = 5385130584970833153L;

    public SearchCarException(String message) {
        super(message);
    }

}
