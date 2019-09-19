package com.reservation.domain.model.car;

import com.reservation.domain.model.car.exceptions.InvalidPriceException;
import com.reservation.domain.model.shared.ValueObject;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class FullInsurancePrice implements ValueObject {

	private static final long serialVersionUID = 3264452369247187812L;

	public static final FullInsurancePrice ZERO = new FullInsurancePrice();

	@Column(name="full_insurance_price", nullable=false)
	private final Double value;


	public FullInsurancePrice(final Double price) {
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
	protected FullInsurancePrice() {
		super();
		this.value = 0.0;
	}

	public Double getValue() { return this.value; }

	public Double forPeriod(long totalDays) {
		return getValue() * totalDays;
	}

	public Price toPrice() {
		return new Price(getValue());
	}

	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FullInsurancePrice that = (FullInsurancePrice) o;
		return value.equals(that.value);
	}

}
