package com.reservation.infrastructure.configuration.exceptions;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FeignServerException extends FeignException {
    private static final long serialVersionUID = 285438119894350359L;

    private String responseContent;

    public FeignServerException(int status, String message) {
        super(status, message);
        this.responseContent = "";
    }

    public FeignServerException(int status, String message, String responseContent) {
        super(status, message);
        this.responseContent = responseContent;
    }

    public String getResponseContent() { return responseContent; }

}
