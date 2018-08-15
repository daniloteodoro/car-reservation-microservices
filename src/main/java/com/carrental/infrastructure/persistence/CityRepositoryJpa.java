package com.carrental.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.carrental.domain.model.reservation.City;
import com.carrental.domain.model.reservation.CityRepository;
import com.carrental.domain.model.reservation.exceptions.CityFormatException;
import com.carrental.util.StringUtils;

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
	
}



