package com.reservation.domain.model.customer;

import java.time.LocalDateTime;

import com.reservation.domain.model.car.Category;
import com.reservation.domain.model.reservation.City;
import com.reservation.domain.model.reservation.Order;
import com.reservation.domain.model.reservation.Reservation;
import com.reservation.domain.model.reservation.exceptions.CarUnavailableException;
import com.reservation.domain.model.reservation.exceptions.ReservationException;
import com.reservation.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/***
 * Represents a person booking a reservation.
 * @author Danilo Teodoro
 *
 */
@Entity
public class Customer implements com.reservation.shared.Entity {

	private static final long serialVersionUID = -7213890863182901666L;

	@Id
	private Long id = null;
	private String fullName = "";
	private String email = "";
	private String phoneNumber = "";
	private String address = "";
	@ManyToOne
	private City city = null;

	public static final Customer EMPTY = new Customer();

	protected Customer() {
		super();
	}

	public Reservation select(Category category, City pickupPoint, LocalDateTime start, City dropOffPoint, LocalDateTime finish) throws CarUnavailableException {
		return new Reservation(this, category, pickupPoint, start, dropOffPoint, finish);
	}

	public Order payAtStore(Reservation reservation) throws ReservationException {
		StringUtils.requireNonEmpty(getFullName(), () -> new ReservationException("Full name is required before paying"));
		StringUtils.requireNonEmpty(getAddress(), () -> new ReservationException("Address is required before paying"));
		StringUtils.requireNonEmpty(getPhoneNumber(), () -> new ReservationException("Phone Number is required before paying"));
		StringUtils.requireNonEmpty(getEmail(), () -> new ReservationException("E-mail is required before paying"));
		if (getCity() == null) {
			throw new ReservationException("City is required before paying");
		}
		return new Order(reservation);
	}

	public Boolean isValid() {
		return !StringUtils.isEmpty(getFullName()) &&
				!StringUtils.isEmpty(getAddress()) &&
				!StringUtils.isEmpty(getPhoneNumber()) &&
				!StringUtils.isEmpty(getEmail()) &&
				(getCity() != null);
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

		Customer other = (Customer) obj;
		return id.equals(other.id);
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
}
