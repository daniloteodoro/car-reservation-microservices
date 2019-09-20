package com.reservation.domain.model.reservation.exceptions;

import com.reservation.domain.model.reservation.OrderId;
import com.reservation.domain.model.reservation.ReservationNumber;

public class ReservationAlreadyConfirmedException extends ReservationException {

	private static final long serialVersionUID = -2502039936828883592L;

	private final ReservationNumber reservation;
	private final OrderId orderId;

	public ReservationAlreadyConfirmedException(ReservationNumber reservation, OrderId orderId) {
		super(String.format("Reservation '%s' has already been converted to order '%s'", reservation, orderId));
		this.reservation = reservation;
		this.orderId = orderId;
	}

	public ReservationNumber getReservation() {
		return reservation;
	}

	public OrderId getOrderId() {
		return orderId;
	}
}
