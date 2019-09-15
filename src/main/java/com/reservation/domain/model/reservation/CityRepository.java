package com.reservation.domain.model.reservation;

import java.util.Optional;

import com.reservation.domain.model.reservation.exceptions.CityFormatException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CityRepository extends JpaRepository<City, Long> {
	
	/***
	 * Find first city which matches the string provided in the format as explained below.
	 * @param cityNameAndCountryCode String representing the city and country in the following format: cityname-CountryCode. Example: Rotterdam-NL or New York-US.
	 * @return The city found or null, wrapped in an Optional construct.
	 */
	// TODO: Change to use simple query annotation
	@Query("from City where lower(name || '-' || country) like lower(:cityNameAndCountryCode)")
	Optional<City> findByNameAndCountryCode(String cityNameAndCountryCode);

}
