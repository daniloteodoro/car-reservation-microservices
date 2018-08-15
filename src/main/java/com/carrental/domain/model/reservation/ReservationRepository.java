package com.carrental.domain.model.reservation;

import java.util.Optional;

public interface ReservationRepository {
	
	Optional<Reservation> findByNumber(ReservationNumber number);
	
	void save(Reservation reservation);

}
