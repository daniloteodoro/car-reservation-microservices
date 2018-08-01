package com.carrental.domain.model.reservation;

import com.carrental.domain.model.reservation.exceptions.ReservationNotFoundException;

public interface ReservationRepository {
	
	Reservation findByNumber(ReservationNumber number) throws ReservationNotFoundException;
	
	void save(Reservation reservation);

}
