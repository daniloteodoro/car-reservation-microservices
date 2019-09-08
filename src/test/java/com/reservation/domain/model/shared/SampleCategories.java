package com.reservation.domain.model.shared;

import com.reservation.domain.model.car.CarPrice;
import com.reservation.domain.model.car.Category;
import com.reservation.domain.model.car.CategoryType;
import com.reservation.domain.model.car.Price;

public class SampleCategories {
	
	public final static Category MEDIUMSIZED_CATEGORY = 
			new Category(CategoryType.MEDIUMSIZED, new CarPrice(40.0), Price.ZERO, new Price(18.0));

}
