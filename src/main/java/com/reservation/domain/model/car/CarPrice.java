package com.reservation.domain.model.car;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.reservation.domain.model.car.exceptions.InvalidPriceException;
import com.reservation.domain.model.shared.ValueObject;

@Embeddable
public class CarPrice implements ValueObject {

	private static final long serialVersionUID = 7971578460469176709L;
	
	@Column(name="PRICE", nullable=false)
	private final Double value;
	
	
	public CarPrice(final Double price) {
		super();
		if (price == null) {
			throw new InvalidPriceException("Price must not be null.");
		}
		if (price <= 0) {
			throw new InvalidPriceException("Car price must be greater than zero.");
		}
		this.value = price;
	}
	
	// Simple constructor for ORM and serializers
	protected CarPrice() {
		super();
		this.value = 1_000_000.00;
	}
	
	public Double getValue() {
		return this.value;
	}
	
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		CarPrice other = (CarPrice) obj;
		return value.equals(other.value);
	}
	
	
}
