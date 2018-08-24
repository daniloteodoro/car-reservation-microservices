package com.carrental.domain.model.car;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.carrental.shared.ValueObject;
import com.carrental.util.StringUtils;

@Embeddable
public class Category implements ValueObject {
	
	private static final long serialVersionUID = -6083285778312724846L;
	
	public static final Category ECONOMIC = new Category("ECONOMIC");
	public static final Category COMPACT = new Category("COMPACT");
	public static final Category MEDIUMSIZED = new Category("MEDIUMSIZED");
	public static final Category CONFORT = new Category("CONFORT");
	public static final Category PREMIUM = new Category("PREMIUM");
	public static final Category VAN = new Category("VAN");
	public static final Category SPORT = new Category("SPORT");
	
	@Column(name="CATEGORY", nullable=false)
	private final String description;
	//private final Car featuringCar;
	
	
	public Category(final String description) {
		super();
		this.description = StringUtils.requireNonEmpty(description, "Brand description must not be null");
	}
	
	// Simple constructor for persistence and serializers
	protected Category() {
		super();
		this.description = "";
	}
	
	public String getDescription() {
		return description;
	}
	/*
	public Car getFeaturingCar() {
		return featuringCar;
	}
	*/
	
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
		
		Category other = (Category) obj;
		return description.equalsIgnoreCase(other.description);
	}
}


