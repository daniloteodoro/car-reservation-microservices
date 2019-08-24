package com.reservation.domain.model.reservation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/***
 * Using a list of periods (Date/Time pairs), answer whether a given period is available or colides with a period from the list.
 * @author Danilo Teodoro
 *
 */
public class ReservationAvailability {
	
	private final List<Reservation> reservations;
	
	
	public ReservationAvailability(final List<Reservation> reservations) {
		super();
		this.reservations = prepareList(reservations);
	}
	
	private List<Reservation> prepareList(final List<Reservation> reservations) {
		List<Reservation> newList = new ArrayList<>(reservations);
		Collections.sort(newList);
		return Collections.unmodifiableList(newList);
	}
	
	public boolean isAvailable(final LocalDateTime start, final LocalDateTime finish) {
		
		
		
		return false;
	}
	
	public List<Reservation> getReservations() {
		return this.reservations;
	}
	
}
