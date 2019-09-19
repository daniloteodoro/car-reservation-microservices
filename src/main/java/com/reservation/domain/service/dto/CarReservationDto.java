package com.reservation.domain.service.dto;


import com.reservation.application.dto.CityDto;
import com.reservation.application.dto.CustomerDto;
import com.reservation.domain.model.car.CategoryType;
import com.reservation.domain.model.car.ExtraProduct;
import com.reservation.domain.model.car.InsuranceType;
import com.reservation.domain.model.reservation.Reservation;
import com.reservation.domain.model.reservation.ReservationNumber;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class CarReservationDto {

	private CustomerDto customer;
	private CategoryType category;
	private Set<ExtraProduct> extras = new HashSet<>();
	private InsuranceType insurance = InsuranceType.STANDARD_INSURANCE;
	private CityDto pickupLocation;
	private CityDto dropOffLocation;
	private LocalDateTime pickupDateTime;
	private LocalDateTime dropOffDateTime;
	private ReservationNumber reservationNumber;

	public CarReservationDto(CustomerDto customer, CategoryType category, Set<ExtraProduct> extras, InsuranceType insurance, CityDto pickupLocation, CityDto dropOffLocation, LocalDateTime pickupDateTime, LocalDateTime dropOffDateTime, ReservationNumber reservationNumber) {
		this.customer = customer;
		this.category = category;
		this.extras = extras;
		this.insurance = insurance;
		this.pickupLocation = pickupLocation;
		this.dropOffLocation = dropOffLocation;
		this.pickupDateTime = pickupDateTime;
		this.dropOffDateTime = dropOffDateTime;
		this.reservationNumber = reservationNumber;
	}

	public static CarReservationDto basedOn(Reservation reservation) {
		Set<ExtraProduct> extras = new HashSet<>();
		CityDto pickupLocation = CityDto.basedOn(reservation.getPickupLocation());
		CityDto dropOffLocation = CityDto.basedOn(reservation.getDropOffLocation());

		reservation.getExtras().forEachRemaining(extras::add);

		return new CarReservationDto(CustomerDto.basedOn(reservation.getCustomer()), reservation.getType(),
				extras, reservation.getInsuranceType(), pickupLocation, dropOffLocation, reservation.getPickupDateTime(), reservation.getDropOffDateTime(),
				reservation.getReservationNumber());
	}

	public CustomerDto getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerDto customer) {
		this.customer = customer;
	}
	public CategoryType getCategory() {
		return category;
	}
	public void setCategory(CategoryType category) {
		this.category = category;
	}
	public Set<ExtraProduct> getExtras() {
		return extras;
	}
	public void setExtras(Set<ExtraProduct> extras) {
		this.extras = extras;
	}
	public InsuranceType getInsurance() {
		return insurance;
	}
	public void setInsurance(InsuranceType insurance) {
		this.insurance = insurance;
	}
	public CityDto getPickupLocation() {
		return pickupLocation;
	}
	public void setPickupLocation(CityDto pickupLocation) {
		this.pickupLocation = pickupLocation;
	}
	public CityDto getDropOffLocation() {
		return dropOffLocation;
	}
	public void setDropOffLocation(CityDto dropOffLocation) {
		this.dropOffLocation = dropOffLocation;
	}
	public LocalDateTime getPickupDateTime() {
		return pickupDateTime;
	}
	public void setPickupDateTime(LocalDateTime pickupDateTime) {
		this.pickupDateTime = pickupDateTime;
	}
	public LocalDateTime getDropOffDateTime() {
		return dropOffDateTime;
	}
	public void setDropOffDateTime(LocalDateTime dropOffDateTime) {
		this.dropOffDateTime = dropOffDateTime;
	}
	public ReservationNumber getReservationNumber() {
		return reservationNumber;
	}
	public void setReservationNumber(ReservationNumber reservationNumber) {
		this.reservationNumber = reservationNumber;
	}
}
