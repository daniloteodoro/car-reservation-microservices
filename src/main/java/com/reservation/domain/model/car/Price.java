package com.reservation.domain.model.car;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.reservation.domain.model.car.exceptions.InvalidPriceException;
import com.reservation.shared.ValueObject;

@Embeddable
public class Price implements ValueObject {

	private static final long serialVersionUID = 5472952306657566099L;
	
	public static final Price ZERO = new Price();
	
	@Column(name="PRICE", nullable=false)
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
		
		Price other = (Price) obj;
		return value.equals(other.value);
	}
	
}
