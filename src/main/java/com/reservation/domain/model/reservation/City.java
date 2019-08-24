package com.reservation.domain.model.reservation;

import com.reservation.domain.model.reservation.exceptions.CityFormatException;
import com.reservation.domain.model.reservation.exceptions.InvalidCityException;
import com.reservation.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class City implements com.reservation.shared.Entity {
	
	private static final long serialVersionUID = 6858119231143736985L;
	private static final int COUNTRY_CODE_LENGTH = 2;
	
	@Id
	private final Integer id;
	
	private final String name;
	
	@Enumerated(EnumType.STRING)
	private final Country country;
	
	
	public City(Integer id, String name, Country country) {
		super();
		this.id = Objects.requireNonNull(id, "City id must not be null");
		this.name = StringUtils.requireNonEmpty(name, "City name must not be null");
		this.country = Objects.requireNonNull(country, "City Country must not be null");
	}
	
	public City(String name, Country country) {
		super();
		this.id = -1;
		this.name = StringUtils.requireNonEmpty(name, "City name must not be null");
		this.country = Objects.requireNonNull(country, "City Country must not be null");
	}
	
	// Simple constructor for ORM and serializers
	protected City() {
		super();
		this.id = -1;
		this.name = "";
		this.country = Country.NL;
	}
	
	/***
	 * Take a string in a format "<city name>-<country code>" and tries to convert to a city with a proper description and country
	 * @param cityAndCountry string in a format 'cityName-countryCode'
	 * @return A new city with a proper description and country
	 * @throws InvalidCityException
	 */
	public static City parse(String cityAndCountry) throws InvalidCityException {
		String cityStr = StringUtils.requireNonEmpty(cityAndCountry, () -> new CityFormatException("City name must not be null"));
		int countryPos = cityStr.lastIndexOf("-") + 1;
		if (!cityStr.contains("-")) {
			throw new CityFormatException(String.format("Cannot parse city '%s' - no country information!", cityAndCountry));
		}
		if (cityStr.length() < countryPos + COUNTRY_CODE_LENGTH) {
			throw new CityFormatException(String.format("Cannot parse city '%s' - no country information!", cityAndCountry));
		}
		if (countryPos - 1 < 1) {
			throw new CityFormatException("City name must not be null");
		}
		
		String countryCode = cityStr.substring(countryPos, countryPos + COUNTRY_CODE_LENGTH).toUpperCase();
		String cityPart = cityStr.substring(0, countryPos - 1);
		
		Country country;
		try {
			country = Country.valueOf(countryCode);
		} catch (RuntimeException e) {
			throw new InvalidCityException(e.getMessage(), e);
		}
		
		return new City(-1, cityPart, country);		
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
		return this.id.hashCode();
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
		return this.id.equals(other.id);
	}

	public Integer getId() {
		return id;
	}
	
}
