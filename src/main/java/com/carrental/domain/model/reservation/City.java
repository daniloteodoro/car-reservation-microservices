package com.carrental.domain.model.reservation;

import java.util.Objects;

import com.carrental.shared.ValueObject;
import com.carrental.util.StringUtils;

public class City implements ValueObject {
	
	private final String name;
	private final Country country;
	
	
	public City(String name, Country country) {
		super();
		this.name = StringUtils.requireNonEmpty(name, "City name must not be null");
		this.country = Objects.requireNonNull(country, "City Country must not be null");
	}

	public Country getCountry() {
		return country;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + country.hashCode();
		result = prime * result + name.hashCode();
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
		
		City other = (City) obj;
		return country.equals(other.country) &&
				name.equalsIgnoreCase(other.name);
	}
	
}
