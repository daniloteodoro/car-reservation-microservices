package com.carrental.domain.model.car;

import com.carrental.shared.ValueObject;
import com.carrental.util.StringUtils;


public final class LicensePlate implements ValueObject {
	
	private final String data;
	
	
	public LicensePlate(final String data) {
		super();
		this.data = StringUtils.requireNonEmpty(data, "License plate description must not be empty");
	}
	
	@Override
	public String toString() {
		return this.data;
	}

	@Override
	public int hashCode() {
		return data.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		LicensePlate other = (LicensePlate) obj;
		return data.equalsIgnoreCase(other.data);
	}

}
