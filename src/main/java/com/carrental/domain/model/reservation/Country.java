package com.carrental.domain.model.reservation;

public enum Country {
	
	BRAZIL("BR"),
	CANADA("CA"),
	NETHERLANDS("NL"),
	UNITED_STATES("US");
	
	private String countryCode;
	
	Country(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

}
