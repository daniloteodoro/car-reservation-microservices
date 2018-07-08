package com.carrental.domain.model.reservation;

import com.carrental.shared.ValueObject;
import com.carrental.util.StringUtils;

public class ReservationNumber implements ValueObject {
	
	private final String data;
	
	
	public ReservationNumber(final String data) {
		super();
		this.data = StringUtils.requireNonEmpty(data, "Reservation Number must not be null");
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
		
		ReservationNumber other = (ReservationNumber) obj;
		return data.equalsIgnoreCase(other.data);
	}

}
