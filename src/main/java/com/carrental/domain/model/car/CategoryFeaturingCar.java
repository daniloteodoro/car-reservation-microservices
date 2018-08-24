package com.carrental.domain.model.car;

import com.carrental.shared.ValueObject;

public class CategoryFeaturingCar implements ValueObject {

	private static final long serialVersionUID = -4939181845185321612L;
	
	private final Category category;
	private final Car featuredCar;
	
	
	public CategoryFeaturingCar(final Category category, final Car featuredCar) {
		super();
		this.category = category;
		this.featuredCar = featuredCar;
	}
	
	public Category getCategory() {
		return category;
	}
	public Car getFeaturedCar() {
		return featuredCar;
	}

}
