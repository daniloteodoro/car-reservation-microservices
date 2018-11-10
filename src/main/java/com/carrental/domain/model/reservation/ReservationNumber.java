package com.carrental.domain.model.reservation;

import com.carrental.shared.ValueObject;
import com.carrental.util.StringUtils;

public class ReservationNumber implements ValueObject {
	
	private static final long serialVersionUID = 5407616353851887002L;
	
	private final String value;
	
	
	public ReservationNumber(final String data) {
		super();
		this.value = StringUtils.requireNonEmpty(data, "Reservation Number must not be null");
	}
	
	public static ReservationNumber of(final String data) {
		return new ReservationNumber(data);
	}
	
	// Simple constructor for persistence and serializers
	protected ReservationNumber() {
		super();
		this.value = "";
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return this.value;
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
		
		ReservationNumber other = (ReservationNumber) obj;
		return value.equalsIgnoreCase(other.value);
	}

}
