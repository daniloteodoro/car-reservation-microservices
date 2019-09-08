package com.reservation.application.dto;

import com.reservation.domain.model.reservation.City;
import com.reservation.domain.model.reservation.Country;

public class CityDto {
	
	private String name = "";
	private Country country = Country.NL;
	
	public CityDto() {
		super();
	}
	
	public CityDto(String name, Country country) {
		super();
		this.name = name;
		this.country = country;
	}
	
	public static CityDto basedOn(City city) {
		if (city == null) {
			return null;
		}
		return new CityDto(city.getName(), city.getCountry());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return String.format("%s - %s", name, country);
	}
}
