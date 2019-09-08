package com.reservation.domain.model.car;

import com.reservation.domain.model.customer.Customer;
import com.reservation.domain.model.reservation.City;
import com.reservation.domain.model.reservation.Reservation;
import com.reservation.domain.model.reservation.exceptions.CategoryUnavailableException;

import java.time.LocalDateTime;
import java.util.Objects;

/***
 * Represents a car category like COMPACT for a given location and period, and can answer whether it's available to be reserved or not.
 */
public class CategoryAvailability {

    private final CategoryWithReservationInfo categoryWithReservationInfo;
    private final City pickupLocation;
    private final City dropOffLocation;
    private final LocalDateTime pickupDateTime;
    private final LocalDateTime dropOffDateTime;


    public CategoryAvailability(CategoryWithReservationInfo categoryWithReservationInfo, City pickupLocation, City dropOffLocation, LocalDateTime pickupDateTime, LocalDateTime dropOffDateTime) {
        super();

        Objects.requireNonNull(categoryWithReservationInfo, "Invalid category");
        Objects.requireNonNull(pickupLocation, "Invalid pickup location");
        Objects.requireNonNull(pickupDateTime, "Invalid pickup date/time");
        Objects.requireNonNull(dropOffLocation, "Invalid drop-off location");
        Objects.requireNonNull(dropOffDateTime, "Invalid drop-off date/time");

        this.categoryWithReservationInfo = categoryWithReservationInfo;
        this.pickupLocation = pickupLocation;
        this.dropOffLocation = dropOffLocation;
        this.pickupDateTime = pickupDateTime;
        this.dropOffDateTime = dropOffDateTime;
    }

    public boolean isAvailable() {
        return getTotalAvailable() > 0;
    }

    public void checkAvailable() throws CategoryUnavailableException {
        if (!this.isAvailable())
            throw new CategoryUnavailableException(String.format("Category '%s' is not available for reservation", getType()));
    }

    private int getTotalAvailable() {
        int available = categoryWithReservationInfo.getTotal() - categoryWithReservationInfo.getTotalReserved();
        return Math.max(available, 0);
    }

    public CategoryWithReservationInfo getCategoryWithReservationInfo() {
        return categoryWithReservationInfo;
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

    public CategoryType getType() {
        return categoryWithReservationInfo.getCategory().getType();
    }

    public Reservation reserve(Customer visitor) throws CategoryUnavailableException {
        checkAvailable();
        return new Reservation(visitor, this);
    }
}
