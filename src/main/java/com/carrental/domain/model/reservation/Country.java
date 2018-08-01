package com.carrental.domain.model.reservation;

public enum Country {
	
	BRAZIL("BR"),
	CANADA("CA"),
	NETHERLANDS("NL"),
	UNITED_STATES("US");
	
	public final static int COUNTRY_CODE_LENGTH = 2;
	
	private String countryCode;
	
	Country(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryCode() {
		return this.countryCode;
	}
	
	public static Country fromCountryCode(String countryCode) {
		switch (countryCode.toUpperCase()) {
		case "BR": return Country.BRAZIL;
		case "CA": return Country.CANADA;
		case "NL": return Country.NETHERLANDS;
		case "US": return Country.UNITED_STATES;
		default:
			throw new RuntimeException("Unknown country code: " + countryCode);
		}
	}
	
}
