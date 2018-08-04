package com.carrental.domain.model.reservation;

import java.util.Objects;

import com.carrental.domain.model.reservation.exceptions.CityFormatException;
import com.carrental.shared.ValueObject;
import com.carrental.util.StringUtils;

public class City implements ValueObject {
	
	private static final long serialVersionUID = 6858119231143736985L;
	
	private final String name;
	private final Country country;
	
	
	public City(String name, Country country) {
		super();
		this.name = StringUtils.requireNonEmpty(name, "City name must not be null");
		this.country = Objects.requireNonNull(country, "City Country must not be null");
	}
	
	// Simple constructor for persistence and serializers
	protected City() {
		super();
		this.name = "";
		this.country = Country.NETHERLANDS;
	}
	
	/***
	 * Take a string in a format "<city name>-<country code>" and tries to convert to a city with a proper description and country
	 * @param cityAndCountry string in a format 'cityName-countryCode'
	 * @return A new city with a proper description and country
	 */
	public static City parse(String cityAndCountry) {
		String cityStr = StringUtils.requireNonEmpty(cityAndCountry, () -> new CityFormatException("City name must not be null"));
		int countryPos = cityStr.lastIndexOf("-") + 1;
		if (!cityStr.contains("-")) {
			throw new CityFormatException(String.format("Cannot parse city '%s' - no country information!", cityAndCountry));
		}
		if (cityStr.length() < countryPos + Country.COUNTRY_CODE_LENGTH) {
			throw new CityFormatException(String.format("Cannot parse city '%s' - no country information!", cityAndCountry));
		}
		if (countryPos - 1 < 1) {
			throw new CityFormatException("City name must not be null");
		}
		
		String countryCode = cityStr.substring(countryPos, countryPos + Country.COUNTRY_CODE_LENGTH);
		String cityPart = cityStr.substring(0, countryPos - 1);
		
		Country country;
		try {
			country = Country.fromCountryCode(countryCode);
		} catch (RuntimeException e) {
			throw new CityFormatException(e.getMessage(), e);
		}
		
		return new City(cityPart, country);		
	}
	
	public String getName() {
		return this.name;
	}
	
	public Country getCountry() {
		return country;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + country.hashCode();
		result = prime * result + name.hashCode();
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
		
		City other = (City) obj;
		return country.equals(other.country) &&
				name.equalsIgnoreCase(other.name);
	}
	
}
