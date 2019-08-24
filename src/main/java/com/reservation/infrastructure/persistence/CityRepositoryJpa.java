package com.reservation.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.reservation.domain.model.reservation.City;
import com.reservation.domain.model.reservation.CityRepository;
import com.reservation.domain.model.reservation.exceptions.CityFormatException;
import com.reservation.util.StringUtils;

@Repository
public class CityRepositoryJpa implements CityRepository {
	
	private final EntityManager entityManager;
	
	
	public CityRepositoryJpa(@Autowired final EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public Optional<City> findByNameAndCountryCode(String cityNameAndCountryCode) throws CityFormatException {
		
		String parameter = StringUtils.requireNonEmpty(cityNameAndCountryCode, () -> new CityFormatException("Invalid city / country format: " + cityNameAndCountryCode));
		Query query = entityManager.createQuery("from City where lower(name || '-' || country) like lower(:CITYCOUNTRY)");
		query.setParameter("CITYCOUNTRY", parameter);
		List<City> cities = query.getResultList();
		if (cities.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(cities.get(0));
		}
	}
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public Optional<City> findByNameAndCountryCode(String cityName, String countryCode) throws CityFormatException {
		String city = StringUtils.requireNonEmpty(cityName, () -> new CityFormatException("City name is obligatory"));
		String country = StringUtils.requireNonEmpty(countryCode, () -> new CityFormatException("Country code is obligatory"));
		Query query = entityManager.createQuery("from City where lower(name) like lower(:CITY) and lower(country) like lower(:COUNTRY)");
		query.setParameter("CITY", city);
		query.setParameter("COUNTRY", country);
		List<City> cities = query.getResultList();
		if (cities.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(cities.get(0));
		}
	}
	
}



