package com.carrental.domain.model.car;

public enum Category {
	
	ECONOMIC('A', 0.0, 15.0),
	COMPACT('B', 0.0, 15.0),
	MEDIUMSIZED('C', 0.0, 18.0),
	CONFORT('D', 0.0, 20.0),
	PREMIUM('E', 0.0, 35.0),
	VAN('F', 0.0, 60.0),
	SPORT('G', 00.0, 45.0);
	
	private Character initials;
	private Double standardInsurance;
	private Double fullInsurance;
	
	Category(Character initials, Double standardInsurance, Double fullInsurance) {
		this.initials = initials;
		this.standardInsurance = standardInsurance;
		this.fullInsurance = fullInsurance;
	}

	public Character getInitials() {
		return this.initials;
	}
	
	public Double getStandardInsurancePrice() {
		return this.standardInsurance;
	}

	public Double getFullInsurancePrice() {
		return fullInsurance;
	}

}
