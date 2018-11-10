package com.carrental.domain.model.car;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.carrental.shared.ValueObject;
import com.carrental.util.StringUtils;

@Embeddable
public class Brand implements ValueObject {
	
	private static final long serialVersionUID = 1247274053918470317L;
	
	@Column(name="BRAND", nullable=false)
	private final String description;
	
	
	public Brand(final String description) {
		super();
		this.description = StringUtils.requireNonEmpty(description, "Brand description must not be null");
	}
	
	// Simple constructor for ORM and serializers
	protected Brand() {
		super();
		this.description = "";
	}
	
	public String getDescription() {
		return this.description;
	}
	
	@Override
	public String toString() {
		return description;
	}
	
	@Override
	public int hashCode() {
		return description.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Brand other = (Brand) obj;
		return description.equalsIgnoreCase(other.description);
	}
	
	
}
