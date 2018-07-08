package com.carrental.domain.model.reservation;

import com.carrental.shared.Entity;

// TODO: Refine this entity
public class Order implements Entity {
	
	private Reservation reservation;
	
	
	public Order(Reservation reservation) {
		super();
		this.reservation = reservation;
	}
	
	public Double getTotal() {
		return this.reservation.calculateTotal();
	}

}
