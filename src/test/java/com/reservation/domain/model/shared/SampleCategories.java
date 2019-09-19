package com.reservation.domain.model.shared;

import com.reservation.domain.model.car.*;

public class SampleCategories {
	
	public final static CategoryPricing MEDIUMSIZED_CATEGORY =
			new CategoryPricing(CategoryType.MEDIUMSIZED, new CarPrice(40.0), StandardInsurancePrice.ZERO, new FullInsurancePrice(18.0));

}
