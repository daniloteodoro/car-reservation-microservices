package com.carrental.domain.model.reservation;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.carrental.domain.model.car.Category;
import com.carrental.domain.model.car.ExtraProduct;
import com.carrental.domain.model.car.InsuranceType;
import com.carrental.domain.model.customer.Customer;
import com.carrental.shared.Entity;

/***
 * A reservation (or booking?) represents a car occupied (not available for new rentals) by a customer during a certain period.
 * @author Danilo Teodoro
 *
 */

// TODO: Enforce invariants
public class Reservation implements Entity, Comparable<Reservation> {
	
	private static final long serialVersionUID = -7213890863182901666L;
	
	private ReservationNumber reservationNumber;
	private Customer customer;
	private Category category;
	private Set<ExtraProduct> extras = new HashSet<>();
	private InsuranceType insurance = InsuranceType.STANDARD_INSURANCE;
	private City pickupLocation;
	private LocalDateTime pickupDateTime;
	private City dropoffLocation;
	private LocalDateTime dropoffDateTime;
	// TODO: Reservation date/time + expired convenience method?
	
	// TODO: Create Builder
	public Reservation(Customer customer, Category category, City pickupLocation, LocalDateTime pickupDateTime, City dropoffLocation, LocalDateTime dropoffDateTime)  {
		super();
		
		Objects.requireNonNull(customer, "Invalid customer");
		Objects.requireNonNull(category, "Invalid category");
		Objects.requireNonNull(pickupLocation, "Invalid pickup location");
		Objects.requireNonNull(pickupDateTime, "Invalid pickup date/time");
		Objects.requireNonNull(dropoffLocation, "Invalid drop-off location");
		Objects.requireNonNull(dropoffDateTime, "Invalid drop-off date/time");
		
		// TODO: Do we need this check (and here)?  -> Create factory to return reservation and test for this
		/*
		if (!((Car)category).isAvailable(pickupLocation, pickupDateTime, dropoffLocation, dropoffDateTime)) {
			throw new CarUnavailableException();
		}
		*/
		this.customer = customer;
		this.category = category;
		this.pickupLocation = pickupLocation;
		this.pickupDateTime = pickupDateTime;
		this.dropoffLocation = dropoffLocation;
		this.dropoffDateTime = dropoffDateTime;
		
		this.reservationNumber = new ReservationNumber(UUID.randomUUID().toString());
	}
	
	public Category getCategory() {
		return category;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	
	public void addExtraProduct(ExtraProduct extra) {
		Objects.requireNonNull(extra, "Extra product must not be null");
		if (!extras.contains(extra)) {
			this.extras.add(extra);
		}
	}
	
	public void clearExtras() {
		this.extras.clear();
	}
	
	public void removeExtraProduct(ExtraProduct extra) {
		Objects.requireNonNull(extra, "Extra product must not be null");
		this.extras.remove(extra);
	}
	
	public Iterator<ExtraProduct> getExtras() {
		return extras.iterator();
	}
	
	public void setInsurance(InsuranceType insurance) {
		this.insurance = insurance;
	}
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
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
	
	public InsuranceType getInsurance() {
		return insurance;
	}
	
	public Double getTotalForInsurance() {
		return this.category.getInsurancePriceFor(insurance).forPeriod(getAmountOfDays());
	}
	
	public Double getTotalForExtras() {
		double extrasVal = 0.0;
		for (ExtraProduct extra: extras) {
			extrasVal += extra.getPrice().forPeriod(getAmountOfDays());
		}
		return extrasVal;
	}
	
	public Double calculateTotal() {
		Double price = getTotalPriceForCategory();
		Double insurance = getTotalForInsurance();
		Double extras = getTotalForExtras();
		
		return price + insurance + extras;
	}
	
	private Double getTotalPriceForCategory() {
		return category.getPricePerDay().forPeriod(getAmountOfDays());
	}
	
	public Long getAmountOfDays() {
		Long rentalDays = ChronoUnit.DAYS.between(pickupDateTime.toLocalDate(), dropoffDateTime.toLocalDate());
		return rentalDays;
	}
	
	public ReservationNumber getReservationNumber() {
		return reservationNumber;
	}
	
	@Override
	public int hashCode() {
		return reservationNumber.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Reservation other = (Reservation) obj;
		return this.reservationNumber.equals(other.reservationNumber);
	}
	
	@Override
	public String toString() {
		return this.reservationNumber.toString();
	}
	
	@Override
	public int compareTo(Reservation another) {
		return this.pickupDateTime.compareTo(another.pickupDateTime);
	}

	
}





