package com.carrental.shared;

import com.carrental.domain.model.car.CarPrice;
import com.carrental.domain.model.car.Category;
import com.carrental.domain.model.car.CategoryType;
import com.carrental.domain.model.car.Price;

public class SampleCategories {
	
	public final static Category MEDIUMSIZED_CATEGORY = 
			new Category(CategoryType.MEDIUMSIZED, new CarPrice(40.0), Price.ZERO, new Price(18.0));

}
