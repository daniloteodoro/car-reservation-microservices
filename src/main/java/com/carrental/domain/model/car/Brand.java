package com.carrental.domain.model.car;

import com.carrental.shared.ValueObject;
import com.carrental.util.StringUtils;

public class Brand implements ValueObject {
	
	private final String description;
	
	
	public Brand(final String description) {
		super();
		this.description = StringUtils.requireNonEmpty(description, "Brand description must not be null");;
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
