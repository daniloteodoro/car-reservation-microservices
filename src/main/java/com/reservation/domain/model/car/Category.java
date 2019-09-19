package com.reservation.domain.model.car;

import com.reservation.domain.model.customer.Customer;
import com.reservation.domain.model.reservation.City;
import com.reservation.domain.model.reservation.Reservation;
import com.reservation.domain.model.reservation.exceptions.CategoryUnavailableException;
import com.reservation.domain.model.shared.ValueObject;

import java.time.LocalDateTime;
import java.util.Objects;

/***
 * Represents a car category, like "Compact" or "MediumSized", for a given location and period, and can answer whether it's available to be reserved or not.
 * A car category can contain many car models, and every car model can contain many cars. When a customer books a car they see the picture of a specific car, but
 * they are actually booking this car's category. In this case, if you search for cars in Vancouver in August/2019, you might see 3 cars, say Ford Fiesta, VW Golf, and Audi A4.
 * When you select "Ford Fiesta", you are in reality booking the "Compact" category, which means the rental store can give you a similar car like Renault Clio instead.
 *
 * The availability is defined based on total cars reserved VS. total cars available, for this category, in this period (cars are available to all locations).
 *
 * Pricing is also tied to the category: Price per day, Standard insurance price and Full insurance price. This information can be edited and reflect this change on all associated items,
 * and therefore is an entity used by this value object.
 */
public class Category implements ValueObject {

    private static final long serialVersionUID = 7121312138776084091L;

    private final CategoryType type;
    private final CategoryPricing pricingInformation;
    private final City pickupLocation;
    private final City dropOffLocation;
    private final LocalDateTime pickupDateTime;
    private final LocalDateTime dropOffDateTime;
    private final Integer total;
    private final Integer totalReserved;

    public Category(final CategoryType type, final CategoryPricing pricingInformation, final City pickupLocation, final City dropOffLocation, final LocalDateTime pickupDateTime, final LocalDateTime dropOffDateTime, Integer total, Integer totalReserved) {
        super();
        Objects.requireNonNull(type, "Invalid category type");
        Objects.requireNonNull(pricingInformation, "Invalid category pricing information");
        Objects.requireNonNull(pickupLocation, "Invalid pickup location");
        Objects.requireNonNull(pickupDateTime, "Invalid pickup date/time");
        Objects.requireNonNull(dropOffLocation, "Invalid drop-off location");
        Objects.requireNonNull(dropOffDateTime, "Invalid drop-off date/time");

        if (pickupDateTime.isAfter(dropOffDateTime)) {
            throw new RuntimeException("Pick-up date/time should be earlier than the Drop-off date/time");
        }
        if (total < 0) {
            throw new RuntimeException("Total quantity of cars in a category should be positive");
        }
        if (totalReserved < 0) {
            throw new RuntimeException("Total quantity of reserved cars should be positive");
        }
        if (totalReserved > total) {
            throw new RuntimeException("Total quantity of reserved cars cannot be greater than total quantity of cars");
        }

        this.type = type;
        this.pricingInformation = pricingInformation;
        this.pickupLocation = pickupLocation;
        this.dropOffLocation = dropOffLocation;
        this.pickupDateTime = pickupDateTime;
        this.dropOffDateTime = dropOffDateTime;
        this.total = total;
        this.totalReserved = totalReserved;
    }

    public boolean isAvailable() {
        return getTotalAvailable() > 0;
    }

    public void checkAvailable() throws CategoryUnavailableException {
        if (!this.isAvailable())
            throw new CategoryUnavailableException(String.format("Category '%s' is not available for reservation", pricingInformation.getType()));
    }

    private int getTotalAvailable() {
        int available = getTotal() - getTotalReserved();
        return Math.max(available, 0);
    }

    public CategoryType getType() { return type; }
    public CategoryPricing getPricingInformation() {
        return pricingInformation;
    }
    public City getPickupLocation() {
        return pickupLocation;
    }
    public City getDropOffLocation() {
        // TODO: Return a copy of this VO.
        return dropOffLocation;
    }
    public LocalDateTime getPickupDateTime() {
        return pickupDateTime;
    }
    public LocalDateTime getDropOffDateTime() {
        return dropOffDateTime;
    }
    public Integer getTotal() { return total; }
    public Integer getTotalReserved() { return totalReserved; }

    public Reservation reserveFor(Customer visitor) throws CategoryUnavailableException {
        checkAvailable();
        return new Reservation(visitor, this);
    }
}
