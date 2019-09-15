package com.reservation.domain.model.reservation;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.reservation.domain.model.car.Category;
import com.reservation.domain.model.car.CategoryAvailability;
import com.reservation.domain.model.car.ExtraProduct;
import com.reservation.domain.model.car.InsuranceType;
import com.reservation.domain.model.customer.Customer;
import com.reservation.domain.model.reservation.exceptions.CategoryUnavailableException;
import com.reservation.domain.model.reservation.exceptions.ReservationAlreadyConfirmedException;
import com.reservation.domain.model.reservation.exceptions.ReservationException;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/***
 * A reservation (or booking?) represents a car in a category being used (not available for new rentals) by a customer during a certain period in a given location.
 * @author Danilo Teodoro
 *
 */

@Entity
public class Reservation implements com.reservation.domain.model.shared.Entity {

	private static final long serialVersionUID = -4965748075816786448L;

	@EmbeddedId
	private ReservationNumber reservationNumber;
	@NotNull
	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	private Customer customer;
	@NotNull
	@ManyToOne(optional = false)
	private Category category;

	@ElementCollection(targetClass = ExtraProduct.class)
	@CollectionTable(
			name = "RESERVATION_EXTRAPRODUCT",
			joinColumns = @JoinColumn(name = "reservation_id")
	)
	@Column(name = "extra_product", nullable = false)
	@Enumerated(value = EnumType.STRING)
	private Set<ExtraProduct> extras = new HashSet<>();

	@NotNull
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private InsuranceType insurance = InsuranceType.STANDARD_INSURANCE;
	@NotNull
	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	private City pickupLocation;
	@NotNull
	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	private City dropOffLocation;

	@NotNull
	@Column(nullable = false)
	private LocalDateTime pickupDateTime;
	@NotNull
	@Column(nullable = false)
	private LocalDateTime dropOffDateTime;

	@NotNull
	@Column(nullable = false)
	private final LocalDateTime createdAt = LocalDateTime.now();

	private LocalDateTime confirmedAt;
	private OrderId order;
	
	// TODO: Reservation date/time + expired convenience method?
	// TODO: Destructure category information (embed)
	// TODO: Create Builder

	protected Reservation() {
		super();
		// used only by ORMs and serializers
	}

	// Cars available in a certain category, for a given location and period =>
	public Reservation(Customer customer, CategoryAvailability category) throws CategoryUnavailableException {
		super();
		
		Objects.requireNonNull(customer, "Invalid customer");
		Objects.requireNonNull(category, "Invalid category");

		category.checkAvailable();

		this.customer = customer;
		this.category = category.getInformation();
		this.pickupLocation = category.getPickupLocation();
		this.pickupDateTime = category.getPickupDateTime();
		this.dropOffLocation = category.getDropOffLocation();
		this.dropOffDateTime = category.getDropOffDateTime();

		this.reservationNumber = ReservationNumber.of(UUID.randomUUID().toString());
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

	public void replaceExtraProductsWith(ExtraProduct[] extras) {
		clearExtras();
		Arrays.stream(extras).forEach(this::addExtraProduct);
	}

	public void removeExtraProduct(ExtraProduct extra) {
		Objects.requireNonNull(extra, "Extra product must not be null");
		this.extras.remove(extra);
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
		Long rentalDays = ChronoUnit.DAYS.between(pickupDateTime.toLocalDate(), dropOffDateTime.toLocalDate());
		return rentalDays;
	}
	
	public OrderId confirm(ConfirmableReservation onConfirmReservation) throws ReservationException {
		if (getCustomer().isAnonymous())
			throw new ReservationException("No customer associated to this reservation");
		if (isConfirmed())
			throw new ReservationException("This reservation has already been confirmed");

		OrderId newOrder;
		try {
			newOrder = onConfirmReservation.submit(this);
		} catch (ReservationAlreadyConfirmedException e) {
			newOrder = e.getOrderId();
		}

		this.confirmedAt = LocalDateTime.now();
		this.order = newOrder;

		return this.order;
	}

	public boolean isConfirmed() {
		return confirmedAt != null;
	}

	// Simple getters and setters
	public ReservationNumber getReservationNumber() {
		return reservationNumber;
	}
	public LocalDateTime getCreatedAt() { return createdAt;	}
	public LocalDateTime getConfirmedAt() { return confirmedAt; }
	public OrderId getOrder() { return order; }
	public Category getCategory() {
		return category;
	}
	public Customer getCustomer() {
		return customer;
	}
	public Iterator<ExtraProduct> getExtras() {
		return extras.iterator();
	}
	public City getPickupLocation() {
		return pickupLocation;
	}
	public LocalDateTime getPickupDateTime() {
		return pickupDateTime;
	}
	public City getDropOffLocation() {
		return dropOffLocation;
	}
	public LocalDateTime getDropOffDateTime() {
		return dropOffDateTime;
	}
	public InsuranceType getInsurance() {
		return insurance;
	}

	public void setInsurance(InsuranceType insurance) {
		this.insurance = insurance;
	}
	public void setCustomer(Customer customer) throws ReservationException {
		if (customer == null)
			throw new ReservationException("Cannot set a null or invalid customer to reservation");
		this.customer = customer;
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
	
}
