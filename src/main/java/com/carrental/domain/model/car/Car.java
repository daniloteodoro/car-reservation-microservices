package com.carrental.domain.model.car;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.carrental.domain.model.reservation.City;

/***
 * Represents a car available to pickup in a given location and be dropped off in another location, during a period of time
 */
@Entity
@NamedQueries({
	@NamedQuery(name=Car.GET_SINGLE_CAR_BY_CATEGORY, query="select c from Car c where upper(c.model.category) = :CATEGORY")
})
public class Car implements com.carrental.shared.Entity {
	
	private static final long serialVersionUID = 4404795652514822852L;
	public static final String GET_SINGLE_CAR_BY_CATEGORY = "getSingleCarByCategory";
	
	@EmbeddedId
	private LicensePlate licensePlate;
	
	@ManyToOne
	@JoinColumn(name="MODEL_ID", nullable=false, updatable=false)
	private Model model;
	
	@ManyToOne
	@JoinColumn(name="PICKUP_LOCATION")
	private City pickupLocation;
	
	private LocalDateTime pickupDateTime;
	
	@ManyToOne
	@JoinColumn(name="DROPOFF_LOCATION")
	// TODO: Create possible list of dropoffs?
	private City dropoffLocation;
	
	private LocalDateTime dropoffDateTime;
	
	// TODO: Create VO class to validate price
	private Double pricePerDay;
	
	private CarRentalStore store;
	
	private Double standardInsurance;
	private Double fullInsurance;
	
	
	// TODO: Do we really need pickup/drop-off information here?
	public Car(LicensePlate licensePlate, Model model, City pickupLocation, LocalDateTime pickupDateTime, City dropoffLocation,
			LocalDateTime dropoffDateTime, Double pricePerDay, Double standardInsurance, Double fullInsurance) {
		super();
		
		this.model = Objects.requireNonNull(model, "Model must not be null");
		this.licensePlate = Objects.requireNonNull(licensePlate, "License plate must not be null");
		this.pickupLocation = Objects.requireNonNull(pickupLocation, "Pickup location must not be null");
		this.pickupDateTime = Objects.requireNonNull(pickupDateTime, "Pickup date/time must not be null");
		this.dropoffLocation = Objects.requireNonNull(dropoffLocation, "Drop-off location must not be null");
		this.dropoffDateTime = Objects.requireNonNull(dropoffDateTime, "Drop-off date/time must not be null");
		this.pricePerDay = Objects.requireNonNull(pricePerDay, "Car's price per day must not be null");
		this.standardInsurance = Objects.requireNonNull(standardInsurance, "Car's standard insurance value must not be null");
		this.fullInsurance = Objects.requireNonNull(fullInsurance, "Car's full insurance value must not be null");
		
		if (this.pricePerDay <= 0)
			throw new RuntimeException(String.format("Invalid price for a car: %.2f", pricePerDay));
		if (this.standardInsurance < 0)
			throw new RuntimeException(String.format("Invalid standard insurance value for a car: %.2f", standardInsurance));
		if (this.fullInsurance < 0)
			throw new RuntimeException(String.format("Invalid full insurance value for a car: %.2f", fullInsurance));
	}
	
	// Simple constructor for persistence and serializers
	protected Car() {
		super();
	}
	
	public double getInsurancePriceFor(InsuranceType insurance) {
		switch (insurance) {
		case FULL_INSURANCE: return this.fullInsurance;
		default: return this.standardInsurance;
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
	
	public CarRentalStore getStore() {
		return store;
	}
	
	public LicensePlate getLicensePlate() {
		return licensePlate;
	}
	
	public Double getPricePerDay() {
		return pricePerDay;
	}
	
	public Double getStandardInsurance() {
		return standardInsurance;
	}

	public Double getFullInsurance() {
		return fullInsurance;
	}
	
	public boolean isAvailable(City pickupLlocation, LocalDateTime start, City dropoffLocation, LocalDateTime end) {
		boolean isAvailable = 
				this.getPickupLocation().equals(pickupLlocation) &&
				this.getDropoffLocation().equals(dropoffLocation) &&
				this.getPickupDateTime().isBefore(start) &&
				this.getDropoffDateTime().isAfter(end);
		
		return isAvailable;
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
	
	@Override
	public String toString() {
		return this.model.toString();
	}
	
	
	/***
	 * Builder class to help creating a new Car
	 */
	public static class Builder {
		private LicensePlate licensePlate;
		private Model car;
		private City pickupLocation;
		private LocalDateTime pickupDateTime;
		private City dropoffLocation;
		private LocalDateTime dropoffDateTime;
		private Double pricePerDay;
		private Double standardInsurance;
		private Double fullInsurance;
		
		
		public Builder withModel(Model model) {
			this.car = Objects.requireNonNull(model, "Model must not be null");
			return this;
		}
		
		public Builder withPickupLocation(City pickupLocation) {
			this.pickupLocation = Objects.requireNonNull(pickupLocation, "Pickup location must not be null");;
			return this;
		}
		
		public Builder withPickupDateTime(LocalDateTime pickupDateTime) {
			this.pickupDateTime = Objects.requireNonNull(pickupDateTime, "Pickup date/time must not be null");
			return this;
		}
		
		public Builder withDropoffLocation(City dropoffLocation) {
			this.dropoffLocation = Objects.requireNonNull(dropoffLocation, "Drop-off location must not be null");
			return this;
		}
		
		public Builder withDropoffDateTime(LocalDateTime dropoffDateTime) {
			this.dropoffDateTime = Objects.requireNonNull(dropoffDateTime, "Drop-off date/time must not be null");
			return this;
		}
		
		public Builder withLicensePlate(LicensePlate licensePlate) {
			this.licensePlate = Objects.requireNonNull(licensePlate, "License plate must not be null");
			return this;
		}
		
		public Builder withPricePerDay(Double pricePerDay) {
			Double newPrice = Objects.requireNonNull(pricePerDay, "Car's price per day must not be null");
			if (newPrice <= 0)
				throw new RuntimeException(String.format("Invalid price for a car: %.2f", newPrice));
			this.pricePerDay = newPrice;
			return this;
		}
		
		// Create class to hold insurance values and move this validation logic there
		public Builder withStandardInsurance(Double standardInsurance) {
			Double newPrice = Objects.requireNonNull(standardInsurance, "Car's Standard Insurance must not be null");
			if (newPrice < 0)
				throw new RuntimeException(String.format("Invalid Standard Insurance value for a car: %.2f", newPrice));
			this.standardInsurance = newPrice;
			return this;
		}
		
		public Builder withFullInsuranceInsurance(Double fullInsurance) {
			Double newPrice = Objects.requireNonNull(fullInsurance, "Car's Full Insurance must not be null");
			if (newPrice < 0)
				throw new RuntimeException(String.format("Invalid Full Insurance value for a car: %.2f", newPrice));
			this.fullInsurance = newPrice;
			return this;
		}
		
		public Car build() {
			return new Car(this.licensePlate, this.car, this.pickupLocation, this.pickupDateTime, this.dropoffLocation, this.dropoffDateTime, this.pricePerDay,
							this.standardInsurance, this.fullInsurance);
		}
		
	}




}







