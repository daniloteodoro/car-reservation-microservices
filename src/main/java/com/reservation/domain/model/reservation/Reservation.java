package com.reservation.domain.model.reservation;

import com.reservation.domain.model.car.*;
import com.reservation.domain.model.car.exceptions.CarRentalRuntimeException;
import com.reservation.domain.model.customer.Customer;
import com.reservation.domain.model.reservation.exceptions.CategoryUnavailableException;
import com.reservation.domain.model.reservation.exceptions.ReservationAlreadyConfirmedException;
import com.reservation.domain.model.reservation.exceptions.ReservationException;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/***
 * A reservation (or booking?) represents a car in a category being used (not available for new rentals) by a customer during a certain period in a given location.
 * @author Danilo Teodoro
 *
 */

@Entity
public class Reservation implements com.reservation.domain.model.shared.Entity {

	private static final long serialVersionUID = -4965748075816786448L;

	@EmbeddedId
	private final ReservationNumber reservationNumber;

	@NotNull
	@Enumerated(value = EnumType.STRING)
	@Column(name = "category_type", nullable = false)
	private final CategoryType type;

	@NotNull
	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	private Customer customer;

	@ElementCollection(targetClass = ExtraProduct.class)
	@CollectionTable(
			name = "reservation_extraproduct",
			joinColumns = @JoinColumn(name = "reservation_number")
	)
	@Column(name = "extra_product", nullable = false)
	@Enumerated(value = EnumType.STRING)
	private Set<ExtraProduct> extras = new HashSet<>();

	@NotNull
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private InsuranceType insuranceType = InsuranceType.STANDARD_INSURANCE;

	@NotNull
	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	private final City pickupLocation;

	@NotNull
	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	private final City dropOffLocation;

	@NotNull
	@Column(nullable = false)
	private final LocalDateTime pickupDateTime;

	@NotNull
	@Column(nullable = false)
	private final LocalDateTime dropOffDateTime;

	@NotNull
	@Column(nullable = false)
	private final LocalDateTime createdAt = LocalDateTime.now();

	@NotNull
	@Embedded
	@AttributeOverride(name="value", column=@Column(name="price_per_day", nullable = false))
	private final CarPrice pricePerDay;

	@NotNull
	@Embedded
	private StandardInsurancePrice standardInsurancePrice;

	@NotNull
	@Embedded
	private FullInsurancePrice fullInsurancePrice;

	private LocalDateTime confirmedAt;
	private OrderId order;

	// TODO: Reservation date/time + expired convenience method?
	// TODO: Create Builder

	protected Reservation() {
		super();
		// used only by ORMs and serializers
		this.type = CategoryType.VAN;
		this.pricePerDay = new CarPrice(1_000_000.00);
		this.standardInsurancePrice = StandardInsurancePrice.ZERO;
		this.fullInsurancePrice = FullInsurancePrice.ZERO;
		this.reservationNumber = ReservationNumber.of("NULL");
		this.pickupLocation = City.UNKNOWN;
		this.pickupDateTime = LocalDateTime.MIN;
		this.dropOffLocation = City.UNKNOWN;
		this.dropOffDateTime = LocalDateTime.MIN;
	}

	// Cars available in a certain category, for a given location and period =>
	public Reservation(Customer customer, Category category) throws CategoryUnavailableException {
		super();

		Objects.requireNonNull(customer, "Invalid customer");
		Objects.requireNonNull(category, "Invalid category");

		category.checkAvailable();

		this.customer = customer;
		this.type = category.getType();
		this.pickupLocation = category.getPickupLocation();
		this.pickupDateTime = category.getPickupDateTime();
		this.dropOffLocation = category.getDropOffLocation();
		this.dropOffDateTime = category.getDropOffDateTime();
		this.pricePerDay = category.getPricingInformation().getPricePerDay();
		this.standardInsurancePrice = category.getPricingInformation().getStandardInsurance();
		this.fullInsurancePrice = category.getPricingInformation().getFullInsurance();

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

	// TODO: Change to Price VO
	public Double getTotalForInsurance() {
		switch (insuranceType) {
			case FULL_INSURANCE: return getFullInsurancePrice().forPeriod(getAmountOfDays());
			case STANDARD_INSURANCE: return getStandardInsurancePrice().forPeriod(getAmountOfDays());
			default:
				throw new CarRentalRuntimeException(String.format("Unknown insurance type: %s", insuranceType));
		}
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
		return getPricePerDay().forPeriod(getAmountOfDays());
	}

	public Long getAmountOfDays() {
		Long rentalDays = ChronoUnit.DAYS.between(pickupDateTime.toLocalDate(), dropOffDateTime.toLocalDate());
		return rentalDays;
	}

	public OrderId confirm(ConfirmableReservation onConfirmReservation) throws ReservationException {
		if (getPickupLocation().isUnknown() || getDropOffLocation().isUnknown())
			throw new ReservationException("Pick-up location or drop-off locations is unknown");
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
	public ReservationNumber getReservationNumber() { return reservationNumber; }
	public LocalDateTime getCreatedAt() { return createdAt;	}
	public LocalDateTime getConfirmedAt() { return confirmedAt; }
	public OrderId getOrder() { return order; }
	public Customer getCustomer() { return customer; }
	public Iterator<ExtraProduct> getExtras() { return extras.iterator(); }
	public City getPickupLocation() { return pickupLocation; }
	public LocalDateTime getPickupDateTime() { return pickupDateTime; }
	public City getDropOffLocation() { return dropOffLocation; }
	public LocalDateTime getDropOffDateTime() { return dropOffDateTime; }
	public InsuranceType getInsuranceType() { return insuranceType;	}
	public CategoryType getType() { return type; }
	public CarPrice getPricePerDay() { return pricePerDay; }
	public StandardInsurancePrice getStandardInsurancePrice() { return standardInsurancePrice;	}
	public FullInsurancePrice getFullInsurancePrice() { return fullInsurancePrice; }
	public void setInsuranceType(InsuranceType insuranceType) { this.insuranceType = insuranceType;	}
	public void setCustomer(Customer customer) throws ReservationException {
		if (customer == null)
			throw new RuntimeException("Cannot set a null or invalid customer to reservation");
		this.customer = customer;
	}

	@Override
	public int hashCode() {
		return reservationNumber.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Reservation that = (Reservation) o;
		return reservationNumber.equals(that.reservationNumber);
	}

	@Override
	public String toString() {
		return this.reservationNumber.toString();
	}

}
