package com.reservation.domain.model.shared;

import com.reservation.domain.model.car.Brand;
import com.reservation.domain.model.car.Model;

public class SampleModels {
	
	public final static Model VW_GOLF =
			new Model(new Brand("VW"), "Golf", SampleCategories.MEDIUMSIZED_CATEGORY);

}
