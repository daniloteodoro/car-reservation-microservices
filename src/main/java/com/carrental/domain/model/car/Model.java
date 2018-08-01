package com.carrental.domain.model.car;

import java.util.Objects;

import com.carrental.shared.ValueObject;
import com.carrental.util.StringUtils;

public class Model implements ValueObject {
	
	private final Brand brand;
	private final String description;
	private final Category category;
	
	
	public Model(Brand brand, String modelDescription, Category category) {
		super();
		this.brand = Objects.requireNonNull(brand, "Brand must not be null");
		this.description = StringUtils.requireNonEmpty(modelDescription, "Model description must not be null");
		this.category = Objects.requireNonNull(category, "Category must not be null");
	}
	
	// Simple constructor for persistence and serializers
	protected Model() {
		super();
		this.brand = null;
		this.description = "";
		this.category = Category.COMPACT;
	}
	
	public Brand getBrand() {
		return brand;
	}
	
	public Category getCategory() {
		return category;
	}
	
	@Override
	public String toString() {
		return this.description;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + brand.hashCode();
		result = prime * result + category.hashCode();
		result = prime * result + description.hashCode();
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
		
		Model other = (Model) obj;
		return this.brand.equals(other.brand) &&
				this.category.equals(other.category) &&
				this.description.equalsIgnoreCase(other.description);
	}

	public String getDescription() {
		return description;
	}
	

}




