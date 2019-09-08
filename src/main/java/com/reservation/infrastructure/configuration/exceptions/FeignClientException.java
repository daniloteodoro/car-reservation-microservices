package com.reservation.infrastructure.configuration.exceptions;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FeignClientException extends FeignException {
    private static final long serialVersionUID = 285438119894350359L;

    private String responseContent;

    public FeignClientException(int status, String message) {
        super(status, message);
        this.responseContent = "";
    }

    public FeignClientException(int status, String message, String responseContent) {
        super(status, message);
        this.responseContent = responseContent;
    }

    public String getResponseContent() { return responseContent; }

}
