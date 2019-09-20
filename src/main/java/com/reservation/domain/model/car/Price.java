package com.reservation.domain.model.car;

import com.reservation.domain.model.car.exceptions.InvalidPriceException;
import com.reservation.domain.model.shared.ValueObject;

public class Price implements ValueObject {

	private static final long serialVersionUID = -6436912571212731200L;
	public static final Price ZERO = new Price();

	private final Double value;
	
	
	public Price(final Double price) {
		super();
		if (price == null) {
			throw new InvalidPriceException("Price must not be null.");
		}
		if (price < 0) {
			throw new InvalidPriceException("Price must be greater than or equal to zero.");
		}
		this.value = price;
	}
	
	// Simple constructor for ORM and serializers
	protected Price() {
		super();
		this.value = 0.0;
	}
	
	public Double getValue() { return this.value; }
	
	public Double forPeriod(long totalDays) {
		return getValue() * totalDays;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Price price = (Price) o;
		return value.equals(price.value);
	}

}
