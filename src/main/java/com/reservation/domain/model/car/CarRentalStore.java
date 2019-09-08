package com.reservation.domain.model.car;

import java.util.Objects;

import com.reservation.domain.model.reservation.City;
import com.reservation.domain.model.shared.ValueObject;
import com.reservation.util.StringUtils;

public final class CarRentalStore implements ValueObject {
	
	private static final long serialVersionUID = -4929083702695156564L;
	
	private final City city;
	private final String address;
	
	
	public CarRentalStore(City city, String address) {
		super();
		this.city = Objects.requireNonNull(city, "City must not be null");
		this.address = StringUtils.requireNonEmpty(address, "Address must not be null");
	}
	
	// Simple constructor for ORM and serializers
	protected CarRentalStore() {
		super();
		this.city = null;
		this.address = "";
	}
	
	public City getLocation() {
		return city;
	}
	public String getAddress() {
		return address;
	}

	@Override
	public String toString() {
		return String.format("%s in %s", address, city);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + address.hashCode();
		result = prime * result + city.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		CarRentalStore other = (CarRentalStore) obj;
		return address.equalsIgnoreCase(other.address) &&
				city.equals(other.city);
	}
	
	

}




