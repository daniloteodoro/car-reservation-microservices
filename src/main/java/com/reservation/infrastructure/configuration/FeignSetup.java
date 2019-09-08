package com.reservation.infrastructure.configuration;

import com.reservation.infrastructure.configuration.exceptions.FeignClientException;
import com.reservation.infrastructure.configuration.exceptions.FeignServerException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

import static java.lang.String.format;

@Configuration
public class FeignSetup implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        String message = format("status %s reading %s", response.status(), methodKey);
        String bodyContent = "";
        try {
            if (response.body() != null) {
                bodyContent = Util.toString(response.body().asReader());
            }
        } catch (IOException ignored) { // NOPMD
        }
        if (response.status() >= 400 && response.status() <= 499) {
            return new FeignClientException(response.status(), message, bodyContent);
        } else if (response.status() >= 500 && response.status() <= 599) {
            return new FeignServerException(response.status(), message, bodyContent);
        }

        return defaultErrorDecoder.decode(methodKey, response);
    }

//    @Bean
//    public FeignSetup getCustomErrorDecoder() {
//        return new FeignSetup();
//    }

}
