package com.reservation.infrastructure.services;

import com.reservation.domain.model.car.exceptions.CarRentalRuntimeException;
import com.reservation.domain.model.reservation.ConfirmableReservation;
import com.reservation.domain.model.reservation.OrderId;
import com.reservation.domain.model.reservation.exceptions.ReservationAlreadyConfirmedException;
import com.reservation.domain.service.CarAuthService;
import com.reservation.domain.service.OrderService;
import com.reservation.domain.service.ReservationService;
import com.reservation.infrastructure.configuration.exceptions.FeignClientException;
import com.reservation.util.JsonUtils;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceRest implements ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationServiceRest.class);
    private final CarAuthService carAuthService;
    private final OrderService carRentalOrderService;
    private final Environment env;

    public ReservationServiceRest(CarAuthService carAuthService, OrderService carRentalOrderService, Environment env) {
        this.carAuthService = carAuthService;
        this.carRentalOrderService = carRentalOrderService;
        this.env = env;
    }

    @Override
    public ConfirmableReservation confirmReservation() {
        return reservation -> {
            String authorization = "";
            try {
                authorization = carAuthService.login(new CarAuthService.CarLoginCredentialsDto(env.getProperty("reservation-user"), env.getProperty("reservation-user-pwd")));
            } catch (Exception e) {
                logger.error(String.format("Authentication failed before completing order process for reservation '%s': %s", reservation.getReservationNumber(), e.getMessage()));
                throw new CarRentalRuntimeException(String.format("Authentication failed before completing order process for reservation: '%s'", reservation.getReservationNumber()));
            }

            if (authorization == null || !authorization.startsWith("Bearer ")) {
                throw new CarRentalRuntimeException(String.format("Authentication failed before completing order process for reservation: '%s'", reservation.getReservationNumber()));
            }

            try {
                String orderId = carRentalOrderService.submit(authorization, new OrderService.OrderDetailsDto(reservation.getReservationNumber()));
                return OrderId.of(orderId);
            } catch (FeignClientException serviceError) {
                if (serviceError.status() == HttpStatus.CONFLICT.value()) {
                    logger.warn(String.format("Reservation '%s' has already been confirmed to order '%s': %s", reservation.getReservationNumber(), serviceError.getResponseContent(), serviceError.getMessage()));
                    throw new ReservationAlreadyConfirmedException(reservation.getReservationNumber(), JsonUtils.toObject(serviceError.getResponseContent(), OrderId.class));
                } else {
                    throw new CarRentalRuntimeException(String.format("Failure placing order for reservation '%s': %s", reservation.getReservationNumber(), serviceError.getMessage()));
                }
            } catch (Exception e) {
                throw new CarRentalRuntimeException(String.format("Failure placing order for reservation '%s': %s", reservation.getReservationNumber(), e.getMessage()));
            }
        };
    }

}
