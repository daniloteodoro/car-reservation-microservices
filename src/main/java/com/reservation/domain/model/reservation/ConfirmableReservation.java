package com.reservation.domain.model.reservation;

import com.reservation.domain.model.reservation.exceptions.ReservationAlreadyConfirmedException;

@FunctionalInterface
public interface ConfirmableReservation {
    OrderId submit(Reservation reservation) throws ReservationAlreadyConfirmedException;
}
