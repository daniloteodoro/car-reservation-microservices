package com.carrental.domain.model.car;

import com.carrental.shared.ValueObject;

public class CategoryFeaturingModel implements ValueObject {
	
	private static final long serialVersionUID = -4939181845185321612L;
	
	private final Category category;
	private final Model featuredModel;
	
	
	public CategoryFeaturingModel(final Category category, final Model featuredModel) {
		super();
		this.category = category;
		this.featuredModel = featuredModel;
	}
	
	// Simple constructor for ORM and serializers
	public CategoryFeaturingModel() {
		super();
		this.category = null;
		this.featuredModel = null;
	}
	
	public Category getCategory() {
		return category;
	}
	public Model getFeaturedModel() {
		return featuredModel;
	}
	
	@Override
	public String toString() {
		return String.format("%s (%s or similar)", this.category.getType().toString(), this.featuredModel.toString());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((featuredModel == null) ? 0 : featuredModel.hashCode());
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
		CategoryFeaturingModel other = (CategoryFeaturingModel) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (featuredModel == null) {
			if (other.featuredModel != null)
				return false;
		} else if (!featuredModel.equals(other.featuredModel))
			return false;
		return true;
	}
	
	
	
	

}








