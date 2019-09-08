package com.reservation.domain.model.reservation;

import com.fasterxml.jackson.annotation.JsonValue;
import com.reservation.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.reservation.domain.model.shared.ValueObject;

@Embeddable
public class ReservationNumber implements ValueObject {

	private static final long serialVersionUID = 6850717883493619865L;

	@Column(name="reservation_number", nullable=false)
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

	@JsonValue
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
