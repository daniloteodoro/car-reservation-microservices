package com.carrental.domain.model.car;

import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import com.carrental.util.StringUtils;

@Entity
public class Model implements com.carrental.shared.Entity {
	
	private static final long serialVersionUID = 5896117340555165412L;
	
	@Id
	private final Integer id;
	@Embedded
	private final Brand brand;
	private final String description;
	@Enumerated(EnumType.STRING)
	private final Category category;
	
	
	public Model(Integer id, Brand brand, String modelDescription, Category category) {
		super();
		this.id = Objects.requireNonNull(id, "Id must not be null");
		this.brand = Objects.requireNonNull(brand, "Brand must not be null");
		this.description = StringUtils.requireNonEmpty(modelDescription, "Model description must not be null");
		this.category = Objects.requireNonNull(category, "Category must not be null");
	}
	
	public Model(Brand brand, String modelDescription, Category category) {
		super();
		this.id = -1;
		this.brand = Objects.requireNonNull(brand, "Brand must not be null");
		this.description = StringUtils.requireNonEmpty(modelDescription, "Model description must not be null");
		this.category = Objects.requireNonNull(category, "Category must not be null");
	}
	
	// Simple constructor for persistence and serializers
	protected Model() {
		super();
		this.id = null;
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
	
	public String getDescription() {
		return description;
	}

	public Integer getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return this.description;
	}
	
	@Override
	public int hashCode() {
		return this.id.hashCode();
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
		return this.id.equals(other.id);
	}


}




