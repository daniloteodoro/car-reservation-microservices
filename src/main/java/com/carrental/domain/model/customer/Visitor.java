package com.carrental.domain.model.customer;

import java.time.LocalDateTime;

import com.carrental.domain.model.car.Car;
import com.carrental.domain.model.reservation.City;
import com.carrental.domain.model.reservation.Reservation;
import com.carrental.domain.model.reservation.exceptions.CarUnavailableException;
import com.carrental.shared.ValueObject;

public class Visitor implements ValueObject, Customer {

	private static final long serialVersionUID = -4673063649974226277L;
	
	private String fullName;
	private String email;
	private String phoneNumber;
	private String address;
	private City city;
	
	
	public Reservation select(Car car, City pickupPoint, LocalDateTime start, City dropOffPoint, LocalDateTime finish) throws CarUnavailableException {
		Reservation newReservation = new Reservation(this, car, pickupPoint, start, dropOffPoint, finish);
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
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
	
	@Override
	public String toString() {
		return this.fullName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Visitor other = (Visitor) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equalsIgnoreCase(other.address))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equalsIgnoreCase(other.email))
			return false;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equalsIgnoreCase(other.fullName))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		return true;
	}
	
	

}


