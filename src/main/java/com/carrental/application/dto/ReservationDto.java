package com.carrental.application.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.carrental.domain.model.car.ExtraProduct;
import com.carrental.domain.model.car.InsuranceType;
import com.carrental.domain.model.reservation.Reservation;
import com.carrental.domain.model.reservation.ReservationNumber;

public class ReservationDto {
	
	private CarDto car;
	private ReservationNumber reservationNumber;
	//private Customer customer;
	private List<ExtraProduct> extras = new ArrayList<>();
	private InsuranceType insurance = InsuranceType.STANDARD_INSURANCE;
	private CityDto pickupLocation;
	private LocalDateTime pickupDateTime;
	private CityDto dropoffLocation;
	private LocalDateTime dropoffDateTime;
	
	
	public ReservationDto(CarDto car, ReservationNumber reservationNumber, List<ExtraProduct> extras,
			InsuranceType insurance, CityDto pickupLocation, LocalDateTime pickupDateTime, CityDto dropoffLocation,
			LocalDateTime dropoffDateTime) {
		super();
		this.car = car;
		this.reservationNumber = reservationNumber;
		this.extras = extras;
		this.insurance = insurance;
		this.pickupLocation = pickupLocation;
		this.pickupDateTime = pickupDateTime;
		this.dropoffLocation = dropoffLocation;
		this.dropoffDateTime = dropoffDateTime;
	}

	public static ReservationDto basedOn(Reservation reservation) {
		CarDto car = CarDto.basedOn(reservation.getCar());
		List<ExtraProduct> extras = new ArrayList<>();
		CityDto pickupLocation = CityDto.basedOn(reservation.getPickupLocation());
		CityDto dropoffLocation = CityDto.basedOn(reservation.getDropoffLocation());
		
		reservation.getExtras().forEachRemaining((item) -> extras.add(item));
		
		return new ReservationDto(car, reservation.getReservationNumber(), extras, reservation.getInsurance(), 
								  pickupLocation, reservation.getPickupDateTime(), dropoffLocation, reservation.getDropoffDateTime());
	}
	
	// Simple constructor for persistence and serializers
	protected ReservationDto() {
		super();
		this.car = null;
	}
	
	public CarDto getCar() {
		return this.car;
	}

	public ReservationNumber getReservationNumber() {
		return reservationNumber;
	}

	public List<ExtraProduct> getExtras() {
		return extras;
	}

	public InsuranceType getInsurance() {
		return insurance;
	}

	public CityDto getPickupLocation() {
		return pickupLocation;
	}

	public LocalDateTime getPickupDateTime() {
		return pickupDateTime;
	}

	public CityDto getDropoffLocation() {
		return dropoffLocation;
	}

	public LocalDateTime getDropoffDateTime() {
		return dropoffDateTime;
	}
	
}