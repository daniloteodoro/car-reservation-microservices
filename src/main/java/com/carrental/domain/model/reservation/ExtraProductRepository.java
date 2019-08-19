package com.carrental.domain.model.reservation;

import com.carrental.domain.model.car.ExtraProduct;
import com.carrental.domain.model.reservation.exceptions.CityFormatException;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ExtraProductRepository extends CrudRepository<ExtraProduct, Long> {

	Optional<ExtraProduct> findByName(String name);
	
}
