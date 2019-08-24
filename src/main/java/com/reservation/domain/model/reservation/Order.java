package com.reservation.domain.model.reservation;

import com.reservation.shared.Entity;

// TODO: Refine this entity
public class Order implements Entity {
	
	private static final long serialVersionUID = -6300566354201003422L;
	
	private Reservation reservation;
	
	
	public Order(Reservation reservation) {
		super();
		this.reservation = reservation;
	}
	
	public Double getTotal() {
		return this.reservation.calculateTotal();
	}

}
