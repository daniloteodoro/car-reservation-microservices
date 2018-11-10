package com.carrental.domain.model.customer;

import java.time.LocalDateTime;

import com.carrental.domain.model.car.Category;
import com.carrental.domain.model.reservation.City;
import com.carrental.domain.model.reservation.Reservation;
import com.carrental.domain.model.reservation.exceptions.CarUnavailableException;
import com.carrental.shared.Entity;

public class RegisteredCustomer implements Entity, Customer {
	
	private static final long serialVersionUID = -8349573377921731132L;
	
	private String id;
	private String fullName;
	private String email;
	private String phoneNumber;
	private String address;
	private City city;
	
	
	public Reservation select(Category category, City pickupPoint, LocalDateTime start, City dropOffPoint, LocalDateTime finish) throws CarUnavailableException {
		Reservation newReservation = new Reservation(this, category, pickupPoint, start, dropOffPoint, finish);
		return newReservation;
	}
	
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public City getCity() {
		return this.city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return String.format("%d: %s", this.id, this.fullName);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		RegisteredCustomer other = (RegisteredCustomer) obj;
		return id.equals(other.id);
	}

	
}



