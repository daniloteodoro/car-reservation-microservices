package com.carrental.domain.model.customer;

import java.time.LocalDateTime;

import com.carrental.domain.model.car.Car;
import com.carrental.domain.model.reservation.City;
import com.carrental.domain.model.reservation.Order;
import com.carrental.domain.model.reservation.Reservation;
import com.carrental.domain.model.reservation.ReservationException;
import com.carrental.domain.model.reservation.exceptions.CarUnavailableException;
import com.carrental.util.StringUtils;

/***
 * Represents a person booking a reservation, which could be a visitor or a registered customer.
 * @author Danilo Teodoro
 *
 */
public interface Customer {
	
	String getFullName();
	String getEmail();
	String getPhoneNumber();
	String getAddress();
	City getCity();
	
	void setFullName(String fullName);
	void setEmail(String email);
	void setPhoneNumber(String phoneNumber);
	void setAddress(String address);
	void setCity(City city);
	
	Reservation select(Car car, City pickupPoint, LocalDateTime start, City dropOffPoint, LocalDateTime finish) throws CarUnavailableException;
	
	default Order payAtStore(Reservation reservation) throws ReservationException {
		StringUtils.requireNonEmpty(getFullName(), () -> new ReservationException("Full name is required before paying"));
		StringUtils.requireNonEmpty(getAddress(), () -> new ReservationException("Address is required before paying"));
		StringUtils.requireNonEmpty(getPhoneNumber(), () -> new ReservationException("Phone Number is required before paying"));
		StringUtils.requireNonEmpty(getEmail(), () -> new ReservationException("E-mail is required before paying"));
		if (getCity() == null) {
			throw new ReservationException("City is required before paying");
		}
		return new Order(reservation);
	}

}
