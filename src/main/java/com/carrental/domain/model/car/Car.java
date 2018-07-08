package com.carrental.domain.model.car;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.carrental.domain.model.reservation.City;
import com.carrental.domain.model.reservation.Reservation;
import com.carrental.shared.Entity;

/***
 * Represents a car available to pickup in a given location and be dropped off in another location, during a period of time
 */
public class Car implements Entity {
	
	private LicensePlate licensePlate;
	private Model model;
	private City pickupLocation;
	private LocalDateTime pickupDateTime;
	private City dropoffLocation;
	private LocalDateTime dropoffDateTime;
	
	// TODO: Create VO class to validate price
	private Double pricePerDay;
	
	private CarRentalStore store;
	private List<Reservation> reservations = new ArrayList<>();
	// TODO: Create possible list of dropoffs?
	/* Create convenience methods to check if car can be reserved */
	
	
	public Car(LicensePlate licensePlate, Model model, City pickupLocation, LocalDateTime pickupDateTime, City dropoffLocation,
			LocalDateTime dropoffDateTime, Double pricePerDay) {
		super();
		
		this.model = Objects.requireNonNull(model, "Model must not be null");
		this.licensePlate = Objects.requireNonNull(licensePlate, "License plate must not be null");
		this.pickupLocation = Objects.requireNonNull(pickupLocation, "Pickup location must not be null");
		this.pickupDateTime = Objects.requireNonNull(pickupDateTime, "Pickup date/time must not be null");
		this.dropoffLocation = Objects.requireNonNull(dropoffLocation, "Drop-off location must not be null");
		this.dropoffDateTime = Objects.requireNonNull(dropoffDateTime, "Drop-off date/time must not be null");
		this.pricePerDay = Objects.requireNonNull(pricePerDay, "Car's price per day must not be null");
		
		if (this.pricePerDay <= 0)
			throw new RuntimeException(String.format("Invalid price for a car: %.2f", pricePerDay));
	}
	
	public double getInsurancePriceFor(InsuranceType insurance) {
		switch (insurance) {
		case FULL_INSURANCE: return getModel().getCategory().getFullInsurancePrice();
		default: return getModel().getCategory().getStandardInsurancePrice();
		}
	}
	
	public boolean is(Model model) {
		return this.getModel().equals(model);
	}
	
	public Model getModel() {
		return model;
	}
	
	public City getPickupLocation() {
		return pickupLocation;
	}
	
	public LocalDateTime getPickupDateTime() {
		return pickupDateTime;
	}
	
	public City getDropoffLocation() {
		return dropoffLocation;
	}
	
	public LocalDateTime getDropoffDateTime() {
		return dropoffDateTime;
	}
	
	public Iterator<Reservation> getReservations() {
		return reservations.iterator();
	}
	
	public CarRentalStore getStore() {
		return store;
	}
	
	public LicensePlate getLicensePlate() {
		return licensePlate;
	}
	
	public Double getPricePerDay() {
		return pricePerDay;
	}
	
	public boolean isAvailable(City pickupLlocation, LocalDateTime start, City dropoffLocation, LocalDateTime end) {
		// TODO: Implement
		return false;
	}
	
	@Override
	public int hashCode() {
		return licensePlate.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Car other = (Car) obj;
		return this.licensePlate.equals(other.licensePlate);
	}
	
	
	/***
	 * Simple builder class to help creating a new Car Available
	 */
	public static class CarBuilder {
		private LicensePlate licensePlate;
		private Model car;
		private City pickupLocation;
		private LocalDateTime pickupDateTime;
		private City dropoffLocation;
		private LocalDateTime dropoffDateTime;
		private Double pricePerDay;
		
		
		public CarBuilder withModel(Model model) {
			this.car = Objects.requireNonNull(model, "Model must not be null");
			return this;
		}
		
		public CarBuilder withPickupLocation(City pickupLocation) {
			this.pickupLocation = Objects.requireNonNull(pickupLocation, "Pickup location must not be null");;
			return this;
		}
		
		public CarBuilder withPickupDateTime(LocalDateTime pickupDateTime) {
			this.pickupDateTime = Objects.requireNonNull(pickupDateTime, "Pickup date/time must not be null");
			return this;
		}
		
		public CarBuilder withDropoffLocation(City dropoffLocation) {
			this.dropoffLocation = Objects.requireNonNull(dropoffLocation, "Drop-off location must not be null");
			return this;
		}
		
		public CarBuilder withDropoffDateTime(LocalDateTime dropoffDateTime) {
			this.dropoffDateTime = Objects.requireNonNull(dropoffDateTime, "Drop-off date/time must not be null");
			return this;
		}
		
		public CarBuilder withLicensePlate(LicensePlate licensePlate) {
			this.licensePlate = Objects.requireNonNull(licensePlate, "License plate must not be null");
			return this;
		}
		
		public CarBuilder withPricePerDay(Double pricePerDay) {
			Double newPrice = Objects.requireNonNull(pricePerDay, "Car's price per day must not be null");
			if (newPrice <= 0)
				throw new RuntimeException(String.format("Invalid price for a car: %.2f", newPrice));
			this.pricePerDay = newPrice;
			return this;
		}
		
		public Car build() {
			return new Car(this.licensePlate, this.car, this.pickupLocation, this.pickupDateTime, this.dropoffLocation, this.dropoffDateTime, this.pricePerDay);
		}
		
	}





}







