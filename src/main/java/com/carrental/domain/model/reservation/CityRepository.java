package com.carrental.domain.model.reservation;

import java.util.Optional;

import com.carrental.domain.model.reservation.exceptions.CityFormatException;

public interface CityRepository {
	
	Optional<City> findByNameAndCountryCode(String cityNameAndCountryCode) throws CityFormatException;

}
