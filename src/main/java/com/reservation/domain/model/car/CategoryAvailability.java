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
 */
// TODO: Make this a proper Aggregate and then rename the old category to CategoryInformation
public class CategoryAvailability implements ValueObject {

    private static final long serialVersionUID = 7121312138776084091L;

    private final Category information;
    private final City pickupLocation;
    private final City dropOffLocation;
    private final LocalDateTime pickupDateTime;
    private final LocalDateTime dropOffDateTime;
    private final Integer total;
    private final Integer totalReserved;

    public CategoryAvailability(final Category information, final City pickupLocation, final City dropOffLocation, final LocalDateTime pickupDateTime, final LocalDateTime dropOffDateTime, Integer total, Integer totalReserved) {
        super();

        Objects.requireNonNull(information, "Invalid category type");
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

        this.information = information;
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
            throw new CategoryUnavailableException(String.format("Category '%s' is not available for reservation", information.getType()));
    }

    private int getTotalAvailable() {
        int available = getTotal() - getTotalReserved();
        return Math.max(available, 0);
    }

    public Category getInformation() {
        return information;
    }
    public City getPickupLocation() {
        return pickupLocation;
    }
    public City getDropOffLocation() {
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
