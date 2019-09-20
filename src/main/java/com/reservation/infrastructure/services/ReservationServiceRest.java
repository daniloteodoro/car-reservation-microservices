package com.reservation.infrastructure.services;

import com.reservation.domain.model.car.Category;
import com.reservation.domain.model.car.CategoryRepository;
import com.reservation.domain.model.car.CategoryType;
import com.reservation.domain.model.car.exceptions.CarRentalRuntimeException;
import com.reservation.domain.model.car.exceptions.CategoryNotFoundException;
import com.reservation.domain.model.car.exceptions.CustomerNotFoundException;
import com.reservation.domain.model.customer.Customer;
import com.reservation.domain.model.customer.CustomerRepository;
import com.reservation.domain.model.reservation.*;
import com.reservation.domain.model.reservation.exceptions.CustomerException;
import com.reservation.domain.model.reservation.exceptions.ReservationAlreadyConfirmedException;
import com.reservation.domain.model.reservation.exceptions.ReservationException;
import com.reservation.domain.service.CarAuthService;
import com.reservation.domain.service.OrderService;
import com.reservation.domain.service.ReservationService;
import com.reservation.domain.service.dto.CarReservationDto;
import com.reservation.infrastructure.configuration.exceptions.FeignClientException;
import com.reservation.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReservationServiceRest implements ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationServiceRest.class);
    private final CarAuthService carAuthService;
    private final OrderService carRentalOrderService;
    private final Environment env;
    private final ReservationRepository reservationRepository;
    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;

    public ReservationServiceRest(CarAuthService carAuthService, OrderService carRentalOrderService, Environment env, ReservationRepository reservationRepository, CategoryRepository categoryRepository, CustomerRepository customerRepository) {
        this.carAuthService = carAuthService;
        this.carRentalOrderService = carRentalOrderService;
        this.env = env;
        this.reservationRepository = reservationRepository;
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Reservation startReservationAnonymously(CategoryType type, City pickupLocation, LocalDateTime start, City dropOffLocation, LocalDateTime finish) throws CustomerNotFoundException, CategoryNotFoundException, ReservationException {

        if (start.isAfter(finish)) {
            throw new ReservationException("Pick-up date/time should be earlier than the Drop-off date/time");
        }

        Category chosenCategory = categoryRepository.getCategoryAvailabilityFor(type, pickupLocation, start, dropOffLocation, finish)
                .orElseThrow(CategoryNotFoundException::new);

        // TODO: Support logged in user/customer?
        Customer visitor = customerRepository.getAnonymousCustomer();

        // Start a new reservation based on the chosen category
        Reservation reservation = chosenCategory.reserveFor(visitor);

        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation setCustomer(Reservation reservation, String fullName, String email, String phoneNumber, String address, City city) throws CustomerException, ReservationException {
        if (reservation == null)
            throw new RuntimeException("Reservation must not be null");

        if (reservation.getCustomer().isAnonymous()) {
            reservation.setCustomer(
                    new Customer.Builder()
                            .withFullName(fullName)
                            .withEmail(email)
                            .withPhoneNumber(phoneNumber)
                            .withAddress(address)
                            .withCity(city)
                            .build());
        } else {
            reservation.getCustomer().setFullName(fullName);
            reservation.getCustomer().setEmail(email);
            reservation.getCustomer().setPhoneNumber(phoneNumber);
            reservation.getCustomer().setAddress(address);
            reservation.getCustomer().setCity(city);
        }

        return reservationRepository.save(reservation);
    }

    @Override
    public OrderId confirm(Reservation reservation) throws ReservationException {

        OrderId orderId = reservation.confirm(submit());

        reservationRepository.save(reservation);

        return orderId;
    }

    @Override
    public ConfirmableReservation submit() {
        return reservation -> {
            String authorization = "";
            try {
                authorization = carAuthService.login(new CarAuthService.CarLoginCredentialsDto(env.getProperty("reservation-user"), env.getProperty("reservation-user-pwd")));
            } catch (Exception e) {
                logger.error(String.format("Authentication failed before completing order process for reservation '%s': %s", reservation.getReservationNumber(), e.getMessage()));
                throw new CarRentalRuntimeException(String.format("Authentication failed before completing order process for reservation: '%s'", reservation.getReservationNumber()));
            }

            if (authorization == null || !authorization.startsWith("Bearer ")) {
                throw new CarRentalRuntimeException(String.format("Authentication received invalid content (before completing order process for reservation: '%s')", reservation.getReservationNumber()));
            }

            try {
                return carRentalOrderService.submit(authorization, CarReservationDto.basedOn(reservation));
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
