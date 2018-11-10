package com.carrental.domain.model.reservation;

import java.util.Optional;

import com.carrental.domain.model.reservation.exceptions.CityFormatException;

public interface CityRepository {
	
	/***
	 * Find first city which matches the string provided.
	 * @param cityNameAndCountryCode String representing the city and country in the following format: cityname-CountryCode. Example: Rotterdam-NL or New York-US.
	 * @return The city found or null.
	 * @throws CityFormatException In case of null or empty parameters
	 */
	Optional<City> findByNameAndCountryCode(String cityNameAndCountryCode) throws CityFormatException;
	Optional<City> findByNameAndCountryCode(String cityName, String countryCode) throws CityFormatException;

}
