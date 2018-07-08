package com.carrental.domain.model.car;

import java.util.Objects;

import com.carrental.domain.model.reservation.City;
import com.carrental.shared.ValueObject;
import com.carrental.util.StringUtils;

public class CarRentalStore implements ValueObject {
	
	private final City city;
	private final String address;
	
	
	public CarRentalStore(City city, String address) {
		super();
		this.city = Objects.requireNonNull(city, "City must not be null");
		this.address = StringUtils.requireNonEmpty(address, "Address must not be null");
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




