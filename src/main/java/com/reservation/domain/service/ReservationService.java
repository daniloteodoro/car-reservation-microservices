package com.reservation.domain.service;

import com.reservation.domain.model.car.CategoryType;
import com.reservation.domain.model.car.exceptions.CategoryNotFoundException;
import com.reservation.domain.model.car.exceptions.CustomerNotFoundException;
import com.reservation.domain.model.reservation.City;
import com.reservation.domain.model.reservation.ConfirmableReservation;
import com.reservation.domain.model.reservation.OrderId;
import com.reservation.domain.model.reservation.Reservation;
import com.reservation.domain.model.reservation.exceptions.CategoryUnavailableException;
import com.reservation.domain.model.reservation.exceptions.CustomerException;
import com.reservation.domain.model.reservation.exceptions.ReservationException;

import java.time.LocalDateTime;

public interface ReservationService {

    ConfirmableReservation submit();

    /**
     * Start a new reservation for an anonymous customer
     * @param type
     * @param pickupLocation
     * @param start
     * @param dropOffLocation
     * @param finish
     * @return
     * @throws CategoryNotFoundException
     * @throws CustomerNotFoundException
     * @throws CategoryUnavailableException
     */
    Reservation startReservationAnonymously(CategoryType type, City pickupLocation, LocalDateTime start, City dropOffLocation, LocalDateTime finish) throws CategoryNotFoundException, CustomerNotFoundException, ReservationException;

    Reservation setCustomer(Reservation reservation, String fullName, String email, String phoneNumber, String address, City city) throws CustomerException, ReservationException;

    OrderId confirm(Reservation reservation) throws ReservationException;

}
