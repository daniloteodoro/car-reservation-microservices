package com.carrental.domain.model.car;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Entity
public class ExtraProduct {

	@Id
	private final Long id;
	@NotEmpty
	private String name;
	@Embedded
	private Price price;
	
//	GPS(new Price(10.0)),
//	ADDITIONAL_DRIVER(new Price(12.0)),
//	CHILD_SEAT(new Price(10.0));
	
	public ExtraProduct(Long id, String name, Price price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public Price getPrice() {
		return price;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		ExtraProduct other = (ExtraProduct) obj;
		return this.id.equals(other.id);
	}

	@Override
	public String toString() {
		return String.format("%s: %.2f", name, price);
	}
}
